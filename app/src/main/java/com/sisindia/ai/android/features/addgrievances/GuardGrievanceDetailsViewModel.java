package com.sisindia.ai.android.features.addgrievances;

import android.app.Application;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.work.Data;

import com.droidcommons.preference.Prefs;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.gson.Gson;
import com.sisindia.ai.android.R;
import com.sisindia.ai.android.base.IopsBaseViewModel;
import com.sisindia.ai.android.constants.NavigationConstants;
import com.sisindia.ai.android.constants.PrefConstants;
import com.sisindia.ai.android.features.issues.ActionPlanListener;
import com.sisindia.ai.android.models.AudioPlayState;
import com.sisindia.ai.android.models.AudioRecordState;
import com.sisindia.ai.android.models.LookUpType;
import com.sisindia.ai.android.room.dao.ActionPlanDao;
import com.sisindia.ai.android.room.dao.AttachmentDao;
import com.sisindia.ai.android.room.dao.EmployeeSiteDao;
import com.sisindia.ai.android.room.dao.GrievanceDao;
import com.sisindia.ai.android.room.dao.LookUpDao;
import com.sisindia.ai.android.room.entities.AttachmentEntity;
import com.sisindia.ai.android.room.entities.GrievanceEntity;
import com.sisindia.ai.android.uimodels.ActionPlanModel;
import com.sisindia.ai.android.uimodels.GrievanceCategory;
import com.sisindia.ai.android.uimodels.GuardSuggestionItem;
import com.sisindia.ai.android.uimodels.attachments.GrievanceAttachmentMetaData;
import com.sisindia.ai.android.utils.FileUtils;
import com.sisindia.ai.android.workers.GrievanceWorker;

import org.threeten.bp.Instant;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.temporal.ChronoUnit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

import static org.threeten.bp.DayOfWeek.SUNDAY;
import static org.threeten.bp.temporal.TemporalAdjusters.next;

public class GuardGrievanceDetailsViewModel extends IopsBaseViewModel {

    @Inject
    public LookUpDao lookUpDao;

    @Inject
    public EmployeeSiteDao employeeSiteDao;

    @Inject
    public ActionPlanDao actionPlanDao;

    @Inject
    public AttachmentDao attachmentDao;

    @Inject
    public GrievanceDao grievanceDao;

    public ObservableField<List<GrievanceCategory>> categoryObs = new ObservableField<>(new ArrayList<>());

    public ObservableField<List<ActionPlanModel>> actionPlanObs = new ObservableField<>(new ArrayList<>());

    public ObservableField<GuardSuggestionItem> selectedGuard = new ObservableField<>(new GuardSuggestionItem());

    public ObservableField<String> description = new ObservableField<>("");

    public ObservableField<GrievanceCategory> selectedCategoryObs = new ObservableField<>(new GrievanceCategory());

    public ObservableField<LocalDateTime> targetDate = new ObservableField<>();

    public ObservableField<AudioRecordState> isRecordedObs = new ObservableField<>(AudioRecordState.NOT_RECORDED);

    public ObservableField<AudioPlayState> btnPlayState = new ObservableField<>(AudioPlayState.PAUSE);

    public ObservableField<ActionPlanModel> selectedActionPlanObs = new ObservableField<>(new ActionPlanModel());

    public ActionPlanListener actionPlanListener = actionPlan -> {
        selectedActionPlanObs.set(actionPlan);
        if (actionPlan.id == 0) {
            return;
        }
        targetDate.set(Instant.now().plus(actionPlan.closureDays, ChronoUnit.DAYS).atZone(ZoneId.systemDefault()).toLocalDateTime());
    };
    private AttachmentEntity audioAttachment;

    private MediaPlayer mediaPlayer;

    @Inject
    public GuardGrievanceDetailsViewModel(@NonNull Application application) {
        super(application);
    }

    public void onSetTargetDateClick(View view) {
        ActionPlanModel selectedActionPlan = selectedActionPlanObs.get();
        if (selectedActionPlan == null || selectedActionPlan.id == 0) {
            showToast("Please Select Action Plan");
            return;
        }
        int selectedYear = LocalDate.now().getYear();
        int selectedMonth = LocalDate.now().getMonthValue() - 1;
        int selectedDayOfMonth = LocalDate.now().getDayOfMonth();

        final LocalDate currentSunday = LocalDate.now().with(next(SUNDAY));
        final LocalDate nextSunday = currentSunday.with(next(SUNDAY));
        Calendar maxDate = Calendar.getInstance();
        maxDate.set(Calendar.YEAR, nextSunday.getYear());
        maxDate.set(Calendar.MONTH, nextSunday.getMonthValue() - 1);
        maxDate.set(Calendar.DAY_OF_MONTH, nextSunday.getDayOfMonth());

        DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(), R.style.DatePickerTheme, (datePickerView, year, month, dayOfMonth) -> {
            LocalDateTime currentDateTime = LocalDateTime.now();
            targetDate.set(LocalDateTime.of(year, month + 1, dayOfMonth, currentDateTime.getHour(), currentDateTime.getMinute(), currentDateTime.getSecond()));
        }, selectedYear, selectedMonth, selectedDayOfMonth);

        datePickerDialog.getDatePicker().setMinDate(Instant.now().plus(1, ChronoUnit.DAYS).toEpochMilli());
        datePickerDialog.getDatePicker().setMaxDate(Instant.now().plus(selectedActionPlan.closureDays, ChronoUnit.DAYS).toEpochMilli());
        datePickerDialog.show();

        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.RED);
    }

    public void initViewModel() {
        //fetch all category from lookUp
        addDisposable(lookUpDao.fetchAllCategoryForGrievance(LookUpType.GRIEVANCE_CATEGORY.getTypeId())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(items -> categoryObs.set(items), Timber::e));
    }

    public void setSelectedGuardId(int employeeId) {
        addDisposable(employeeSiteDao.fetchGuardDetailForAddGrievance(employeeId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(item -> selectedGuard.set(item), Timber::e));
    }

    public void onCheckedChange(ChipGroup chipGroup, int check) {
        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            Chip chip = (Chip) chipGroup.getChildAt(i);
            if (chip.isChecked()) {
                GrievanceCategory category = (GrievanceCategory) chip.getTag();
                if (category != null) {
                    selectedCategoryObs.set(category);
                    getAllActionPlan(category.lookupIdentifier);
                }
                return;
            } else {
                selectedCategoryObs.set(new GrievanceCategory());
            }
        }
    }

    public void getAllActionPlan(int categoryId) {
        //fetch all action plans
        actionPlanObs.set(new ArrayList<>());
        addDisposable(actionPlanDao.fetchAllForGrievance(categoryId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(items -> {
                    if (items != null && items.size() != 0) {
                        selectedActionPlanObs.set(items.get(0));
                        targetDate.set(Instant.now().plus(items.get(0).closureDays, ChronoUnit.DAYS).atZone(ZoneId.systemDefault()).toLocalDateTime());
                    } else {
                        showToast("No action plan Found");
                    }
                }, Timber::e));
    }


    public void onAddAudioClick(View view) {
        message.what = NavigationConstants.ON_ADD_AUDIO_CLIP_CLICK;
        liveData.postValue(message);
    }

    public void onAddGrievanceClick(View view) {
        GrievanceCategory category = selectedCategoryObs.get();
        LocalDateTime selectedTargetDateTime = targetDate.get();

        if (category == null || category.id == 0) {
            showToast("Please select category");
            return;
        }

        ActionPlanModel actionPlan = selectedActionPlanObs.get();
        if (actionPlan == null || actionPlan.id == 0) {
            showToast("Please select action plan");
            return;
        }

        String descriptionText = description.get();
        if (audioAttachment == null && TextUtils.isEmpty(descriptionText)) {
            showToast("Please add Description / Record audio");
            return;
        }

        GuardSuggestionItem guard = selectedGuard.get();

        if (guard == null || guard.employeeId == 0) {
            showToast("unable to save for employee");
            return;
        }


        if (selectedTargetDateTime == null) {
            showToast("Invalid target date");
            return;
        }

        if (audioAttachment != null) {
            addDisposable(attachmentDao.insert(audioAttachment)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(attachmentId -> {
                        //AR {changing category.id to category.lookupIdentifier}
                        saveGrievance(guard.employeeId, category.lookupIdentifier, descriptionText, actionPlan.id, selectedTargetDateTime.toString(), attachmentId);
                    }, Timber::e));
        } else {
            //AR {changing category.id to category.lookupIdentifier}
            saveGrievance(guard.employeeId, category.lookupIdentifier, descriptionText, actionPlan.id, selectedTargetDateTime.toString(), 0L);
        }

    }

    private void saveGrievance(int employeeId, int categoryId, String descriptionText, int actionPlanId, String targetDate, Long attachmentId) {

        int taskId = Prefs.getInt(PrefConstants.TASK_SERVER_ID);
        int postId = Prefs.getInt(PrefConstants.CURRENT_POST);
        int siteId = Prefs.getInt(PrefConstants.CURRENT_SITE);

        GrievanceEntity item = new GrievanceEntity(employeeId, categoryId, descriptionText,
                actionPlanId, targetDate, taskId, postId, siteId, attachmentId);
        addDisposable(grievanceDao.insert(item)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(row -> {

                    Data inputData = new Data.Builder()
                            .putInt(GrievanceWorker.class.getSimpleName(),
                                    GrievanceWorker.GrievanceWorkerType.SYNC_TO_SERVER.getWorkerType())
                            .build();
                    oneTimeWorkerWithInputData(GrievanceWorker.class, inputData);

                    message.what = NavigationConstants.ON_GUARD_CHECK_GRIEVANCE_ADDED;
                    message.arg1 = row.intValue();
                    liveData.postValue(message);
                }, Timber::e));
    }

    public void onAudioRecorded(String audioFile) {
        if (TextUtils.isEmpty(audioFile)) {
            showToast("unable to save audio.. please record again.");
            isRecordedObs.set(AudioRecordState.NOT_RECORDED);
            return;
        }
        isRecordedObs.set(AudioRecordState.RECORDED);
        Uri uri = Uri.fromFile(FileUtils.getFileByPath(audioFile));
        prepareMediaPlayer(audioFile);

        audioAttachment = new AttachmentEntity(AttachmentEntity.AttachmentSourceType.DEPENDENT_GRIEVANCE);
        String fileName = audioAttachment.attachmentSourceType + "_" +
                Prefs.getInt(PrefConstants.CURRENT_SITE) + "_" + Prefs.getInt(PrefConstants.CURRENT_TASK) + "_" +
                Prefs.getInt(PrefConstants.SELECTED_EMPLOYEE_ID) + "_" +
                audioAttachment.attachmentGuid + FileUtils.THREE_GPP;
        audioAttachment.fileSize = audioFile.length();
        audioAttachment.localFilePath = uri.toString();

        addDisposable(grievanceDao.getGrievanceStorage(Prefs.getInt(PrefConstants.CURRENT_TASK))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(storagePath -> {
                    audioAttachment.storagePath = storagePath + "/" + fileName;

                    GrievanceAttachmentMetaData metaData = new GrievanceAttachmentMetaData();
                    metaData.siteId = Prefs.getInt(PrefConstants.CURRENT_SITE);
                    metaData.taskId = Prefs.getInt(PrefConstants.TASK_SERVER_ID);
                    metaData.employeeId = Prefs.getInt(PrefConstants.SELECTED_EMPLOYEE_ID);
                    metaData.employeeNo = Prefs.getString(PrefConstants.SELECTED_EMPLOYEE_NO);
                    metaData.fileSize = String.valueOf(audioAttachment.fileSize);
                    metaData.attachmentTypeId = 2;
                    metaData.attachmentSourceTypeId = 30;
                    metaData.fileName = fileName;
                    metaData.storagePath = audioAttachment.storagePath;
                    metaData.uuid=audioAttachment.attachmentGuid;

                    audioAttachment.attachmentMetaData = new Gson().toJson(metaData);
                    audioAttachment.isDone = true;
                    audioAttachment.isFileSaved = true;
                }, Timber::e));
    }

    private void prepareMediaPlayer(String audioFile) {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(audioFile);
            mediaPlayer.prepare();
            mediaPlayer.setOnCompletionListener(mp -> {
                onPlayMedia(null);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onPlayMedia(View view) {
        AudioPlayState state = btnPlayState.get();
        if (state != null) {
            switch (state) {

                case PAUSE:
                    btnPlayState.set(AudioPlayState.PLAY);
                    mediaPlayer.start();
                    break;

                case PLAY:
                    btnPlayState.set(AudioPlayState.PAUSE);
                    if (mediaPlayer != null) {
                        mediaPlayer.pause();
                    }
                    break;

            }
        }
    }

    @Override
    protected void onCleared() {

        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onCleared();
    }

}
