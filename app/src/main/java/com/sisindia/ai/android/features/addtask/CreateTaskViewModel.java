package com.sisindia.ai.android.features.addtask;

import static org.threeten.bp.DayOfWeek.SATURDAY;
import static org.threeten.bp.DayOfWeek.SUNDAY;
import static org.threeten.bp.temporal.TemporalAdjusters.next;

import android.app.Application;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.work.Data;

import com.sisindia.ai.android.base.IopsBaseViewModel;
import com.sisindia.ai.android.constants.NavigationConstants;
import com.sisindia.ai.android.models.TableSyncResponse;
import com.sisindia.ai.android.room.dao.DayCheckDao;
import com.sisindia.ai.android.room.dao.TaskDao;
import com.sisindia.ai.android.room.entities.BarrackEntity;
import com.sisindia.ai.android.room.entities.LookUpEntity;
import com.sisindia.ai.android.room.entities.SiteEntity;
import com.sisindia.ai.android.room.entities.TaskEntity;
import com.sisindia.ai.android.uimodels.TaskTypeModel;
import com.sisindia.ai.android.utils.IopsUtil;
import com.sisindia.ai.android.workers.RotaTaskWorker;

import org.threeten.bp.Instant;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.temporal.ChronoUnit;

import java.util.Calendar;
import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class CreateTaskViewModel extends IopsBaseViewModel {

    public ObservableField<SiteEntity> selectedSite = new ObservableField<>();
    public ObservableField<TaskTypeModel> selectedTaskType = new ObservableField<>();
    public ObservableField<LookUpEntity> selectedReason = new ObservableField<>();
    public ObservableField<LookUpEntity> selectedSubTaskType = new ObservableField<>();
    public ObservableField<BarrackEntity> selectedBarrack = new ObservableField<>();
    public ObservableField<LocalDateTime> taskStartDateTime = new ObservableField<>(LocalDateTime.now().plusMinutes(5));
    public ObservableField<LocalDateTime> taskEndDateTime = new ObservableField<>(LocalDateTime.now().plusMinutes(20));
//    public int countryId = Prefs.getInt(PrefConstants.COUNTRY_ID, 1);

    @Inject
    public DayCheckDao dayCheckDao;

    /*@Inject
    public SiteStrengthDao strengthDao;*/

    @Inject
    public TaskDao taskDao;

    @Inject
    public CreateTaskViewModel(@NonNull Application application) {
        super(application);
    }

    public void saveTaskClick(View view) {
        setIsLoading(true);
        SiteEntity site = selectedSite.get();
        LookUpEntity reason = selectedReason.get();
        LookUpEntity subTask = selectedSubTaskType.get();
        BarrackEntity barrack = selectedBarrack.get();
        TaskTypeModel taskType = selectedTaskType.get();
        LocalDateTime selectedStartDateTime = taskStartDateTime.get();
        LocalDateTime selectedEndDateTime = taskEndDateTime.get();
        LocalDateTime currentDateTime = LocalDateTime.now();

        if (taskType == null) {
            showToast("Please Re-Create Task.");
            setIsLoading(false);
            return;
        }

        if (taskType.id != 3 && (site == null || site.id == 0)) {
            showToast("Please select Site");
            setIsLoading(false);
            return;
        }

        if (taskType.id == 3 && barrack == null) {
            showToast("Please select Barrack");
            setIsLoading(false);
            return;
        }

//        if (countryId == 1 && (reason == null || reason.lookupIdentifier == 0)) {
        if (reason == null || reason.lookupIdentifier == 0) {
            showToast("Please select reason");
            setIsLoading(false);
            return;
        }

        if (taskType.id == 7 && subTask == null) {
            showToast("Please select Other Task Type");
            setIsLoading(false);
            return;
        }

        if (selectedStartDateTime == null) {
            showToast("Please select start date");
            setIsLoading(false);
            return;
        }

        if (selectedEndDateTime == null) {
            showToast("Please select end date");
            setIsLoading(false);
            return;
        }

        if (selectedStartDateTime.isBefore(currentDateTime) || selectedEndDateTime.isBefore(currentDateTime)) {
            showToast("Invalid Date Time");
            setIsLoading(false);
            return;
        }

        if (taskType.id == 2 && currentDateTime.isAfter(selectedStartDateTime)) {
            showToast("Invalid Start Date");
            setIsLoading(false);
            return;
        }

        if (taskType.id == 2 && currentDateTime.isAfter(selectedEndDateTime)) {
            showToast("Invalid End Date");
            setIsLoading(false);
            return;
        }

        if (taskType.id == 1 && !(selectedStartDateTime.getHour() < 23 && selectedStartDateTime.getHour() > 4)) {
            showToast("Invalid Start Date time for DayCheck");
            setIsLoading(false);
            return;
        }

        if (taskType.id == 1 && !(selectedEndDateTime.isAfter(selectedStartDateTime) && selectedEndDateTime.getHour() < 23 && selectedEndDateTime.getHour() > 4)) {
            showToast("Invalid End Date time for DayCheck");
            setIsLoading(false);
            return;
        }

        if (taskType.id == 2 && !(selectedStartDateTime.getHour() > 22 || selectedStartDateTime.getHour() < 5)) {
            showToast("Please select time between 11PM to 5 AM");
            setIsLoading(false);
            return;
        }

        if (taskType.id == 2 && !(selectedEndDateTime.isAfter(selectedStartDateTime) && ChronoUnit.HOURS.between(selectedStartDateTime, selectedEndDateTime) < 6 && (selectedEndDateTime.getHour() > 22 || selectedEndDateTime.getHour() < 5))) {
            showToast("Please select time between 11PM to 5 AM");
            setIsLoading(false);
            return;
        }

        if (!(selectedEndDateTime.isAfter(selectedStartDateTime))) {
            showToast("End Date time should be greater than Start Date time");
            setIsLoading(false);
            return;
        }

//        int lookUpIdentifier = reason == null ? 0 : reason.lookupIdentifier;
//        TaskEntity newTask = new TaskEntity(lookUpIdentifier, taskType.id, selectedStartDateTime,
        TaskEntity newTask = new TaskEntity(reason.lookupIdentifier, taskType.id, selectedStartDateTime,
                selectedEndDateTime, Objects.requireNonNull(site).siteName);
        newTask.siteId = site.id;

        if (taskType.id == 3 && barrack != null)
            newTask.barrackId = barrack.id;

        if (taskType.id == 7 && subTask != null)
            newTask.otherTaskTypeLookUpIdentifier = subTask.lookupIdentifier;

        isDuplicateTaskExist(newTask);
    }

    private void isDuplicateTaskExist(TaskEntity newTask) {
        if (newTask.siteId > 0)
            addDisposable(taskDao.checkSiteDuplicateTask(newTask.siteId, newTask.estimatedTaskExecutionStartDateTime)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(id -> checkCount(id, newTask), throwable1 -> {
                        setIsLoading(false);
                        showToast("Unable to create task at the moment");
                    }));
        else if (newTask.barrackId != null && newTask.barrackId > 0) {
            addDisposable(taskDao.checkBarrackDuplicateTask(newTask.barrackId, newTask.estimatedTaskExecutionStartDateTime)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(id -> checkCount(id, newTask), throwable1 -> {
                        setIsLoading(false);
                        showToast("Unable to create task at the moment");
                    }));
        } else if (newTask.siteId == -1 || newTask.siteId == -2) {
            addDisposable(taskDao.checkOtherTypeDuplicateTask(newTask.siteId, newTask.estimatedTaskExecutionStartDateTime)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(id -> checkCount(id, newTask), throwable1 -> {
                        setIsLoading(false);
                        showToast("Unable to create task at the moment");
                    }));
        } else
            showToast("Site id or Barrack id or Other task type id not found...");
    }

    private void checkCount(int count, TaskEntity newTask) {
        setIsLoading(false);
        if (count > 0)
            showToast("Task already exist, please select different start/end time");
        else
            insertAdHocTaskToDB(newTask);
    }

    private void insertAdHocTaskToDB(TaskEntity newTask) {
        addDisposable(taskDao.insert(newTask)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(id -> onTaskCreated(newTask), throwable1 -> {
                    setIsLoading(false);
                    showToast("Unable to create task at the moment");
                }));
    }

    //    private void onTaskCreated(Long taskLocalId) {
    private void onTaskCreated(TaskEntity newTask) {
        setIsLoading(false);
        if (IopsUtil.isInternetAvailable(getApplication())) {
            showLoader();
            addDisposable(coreApi.addOrUpdateCreatedTask(newTask)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(task -> updateTaskTable(task.data, newTask),
                            error -> {
                                hideLoader();
                                syncNewTaskToServerFromWorker();
                            }));
        } else
            syncNewTaskToServerFromWorker();
    }

    private void updateTaskTable(TableSyncResponse.TableSyncData data, TaskEntity item) {
        hideLoader();
        if (data != null && data.serverId != 0)
            addDisposable(taskDao.updateTaskOnServerSync(data.serverId, item.localId, true)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(rowId -> {
                        Timber.e("New ROTA updated successfully in Rota task");
                        message.what = NavigationConstants.ON_TASK_CREATED_SUCCESS;
                        liveData.postValue(message);
                    }, e -> syncNewTaskToServerFromWorker()));
        else
            syncNewTaskToServerFromWorker();
    }

    private void syncNewTaskToServerFromWorker() {
        Data inputData = new Data.Builder().putInt(RotaTaskWorker.class.getSimpleName(),
                RotaTaskWorker.RotaTaskWorkerType.SYNC_TO_SERVER.getWorkerType()).build();
//        oneTimeWorkerWithInputData(RotaTaskWorker.class, inputData);
        oneTimeKeepWorkerWithInputData(RotaTaskWorker.class, inputData);
        message.what = NavigationConstants.ON_TASK_CREATED_SUCCESS;
        liveData.postValue(message);
    }

    public void onSelectSiteForCreateTaskClick(View view) {
        TaskTypeModel taskType = selectedTaskType.get();
        if (taskType != null && (taskType.id == 9 || taskType.id == 8)) {
            return;
        }
        message.what = NavigationConstants.ON_SELECT_SITE_FOR_CREATE_TASK_CLICK;
        message.obj = selectedTaskType.get();
        liveData.postValue(message);
    }

    private void showLoader() {
        message.what = NavigationConstants.SHOW_LOADER_ON_ADD_TASK;
        liveData.postValue(message);
    }

    private void hideLoader() {
        message.what = NavigationConstants.HIDE_LOADER_ON_ADD_TASK;
        liveData.postValue(message);
    }

    public void onSelectBarrackForCreateTaskClick(View view) {
        message.what = NavigationConstants.ON_SELECT_BARRACK_FOR_CREATE_TASK_CLICK;
        liveData.postValue(message);
    }

    public void onSelectReasonForCreateTaskClick(View view) {
        message.what = NavigationConstants.ON_SELECT_REASON_FOR_CREATE_TASK_CLICK;
        liveData.postValue(message);
    }

    public void onSelectSubTaskTypeForCreateTaskClick(View view) {
        message.what = NavigationConstants.ON_SELECT_SUB_TASK_TYPE_FOR_CREATE_TASK_CLICK;
        liveData.postValue(message);
    }

    public void onTaskEndTimeClick(View v) {

        LocalDateTime startDateTime = taskStartDateTime.get();
        LocalDateTime endDateTime = taskEndDateTime.get();
        TaskTypeModel taskType = selectedTaskType.get();

        if (startDateTime != null && endDateTime != null && taskType != null) {
            TimePickerDialog timePickerDialog = new TimePickerDialog(v.getContext(), (view, hour, minute) -> {
                LocalDateTime selectedEndDateTime = LocalDateTime.of(endDateTime.getYear(), endDateTime.getMonth(), endDateTime.getDayOfMonth(), hour, minute, 0);
                if (taskType.id == 1) {
                    if (selectedEndDateTime.isAfter(startDateTime) && hour < 23 && hour > 4) {
                        taskEndDateTime.set(selectedEndDateTime);
                    } else {
                        showToast("Invalid time for DayCheck");
                    }

                } else if (taskType.id == 2) {
                    if (selectedEndDateTime.isAfter(startDateTime) && ChronoUnit.HOURS.between(startDateTime, selectedEndDateTime) < 6 && (hour > 22 || hour < 5)) {
                        taskEndDateTime.set(selectedEndDateTime);
                    } else {
                        showToast("Invalid time for Night Check");
                    }
                } else {
                    taskEndDateTime.set(selectedEndDateTime);
                }

            }, startDateTime.getHour(), startDateTime.getMinute(), false);

            timePickerDialog.show();
            timePickerDialog.getButton(TimePickerDialog.BUTTON_POSITIVE).setTextColor(Color.RED);
            timePickerDialog.getButton(TimePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
        }
    }

    public void onTaskStartTimeClick(View v) {
        LocalDateTime startDateTime = taskStartDateTime.get();
        TaskTypeModel taskType = selectedTaskType.get();
        if (startDateTime != null && taskType != null) {
            TimePickerDialog timePickerDialog = new TimePickerDialog(v.getContext(), (view, hour, minute) -> {
                if (taskType.id == 1) {
                    if (hour < 23 && hour > 4) {
                        taskStartDateTime.set(LocalDateTime.of(startDateTime.getYear(), startDateTime.getMonth(), startDateTime.getDayOfMonth(), hour, minute, startDateTime.getSecond()));
                        taskEndDateTime.set(LocalDateTime.of(startDateTime.getYear(), startDateTime.getMonth(), startDateTime.getDayOfMonth(), hour, minute, startDateTime.getSecond()).plusMinutes(15));
                    } else {
                        showToast("Invalid time for DayCheck");
                    }

                } else if (taskType.id == 2) {
                    if (hour > 22 || hour < 5) {
                        taskStartDateTime.set(LocalDateTime.of(startDateTime.getYear(), startDateTime.getMonth(), startDateTime.getDayOfMonth(), hour, minute, startDateTime.getSecond()));
                        taskEndDateTime.set(LocalDateTime.of(startDateTime.getYear(), startDateTime.getMonth(), startDateTime.getDayOfMonth(), hour, minute, startDateTime.getSecond()).plusMinutes(15));
                    } else {
                        showToast("Invalid time for Night Check");
                    }
                } else {
                    taskStartDateTime.set(LocalDateTime.of(startDateTime.getYear(), startDateTime.getMonth(), startDateTime.getDayOfMonth(), hour, minute, startDateTime.getSecond()));
                }

            }, startDateTime.getHour(), startDateTime.getMinute(), false);

            timePickerDialog.show();
            timePickerDialog.getButton(TimePickerDialog.BUTTON_POSITIVE).setTextColor(Color.RED);
            timePickerDialog.getButton(TimePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
        }
    }

    public void onTaskStartDateClick(View v) {
        LocalDateTime startDateTime = taskStartDateTime.get();
        if (startDateTime != null) {
            int year = startDateTime.getYear();
            int month = startDateTime.getMonthValue() - 1;
            int day = startDateTime.getDayOfMonth();

            final LocalDate currentSunday = LocalDate.now().with(next(SUNDAY));
            final LocalDate nextSunday = currentSunday.with(next(SATURDAY));
            Calendar maxDate = Calendar.getInstance();
            maxDate.set(Calendar.YEAR, nextSunday.getYear());
            maxDate.set(Calendar.MONTH, nextSunday.getMonthValue() - 1);
            maxDate.set(Calendar.DAY_OF_MONTH, nextSunday.getDayOfMonth());

            DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(), (view, pYear, pMonth, pDayOfMonth) -> {
                taskStartDateTime.set(LocalDateTime.of(pYear, pMonth + 1, pDayOfMonth, startDateTime.getHour(), startDateTime.getMinute(), startDateTime.getSecond()));
                taskEndDateTime.set(LocalDateTime.of(pYear, pMonth + 1, pDayOfMonth, startDateTime.getHour(), startDateTime.getMinute(), startDateTime.getSecond()));

            }, year, month, day);
            datePickerDialog.getDatePicker().setMinDate(Instant.now().toEpochMilli());
            datePickerDialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());
            datePickerDialog.show();

            datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
            datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.RED);

        }
    }

    public void onTaskEndDateClick(View v) {
        LocalDateTime startDateTime = taskStartDateTime.get();
        TaskTypeModel taskType = selectedTaskType.get();
        if (startDateTime != null && taskType != null) {
            int year = startDateTime.getYear();
            int month = startDateTime.getMonthValue() - 1;
            int day = startDateTime.getDayOfMonth();

            Calendar minDate = Calendar.getInstance();
            minDate.set(Calendar.YEAR, startDateTime.getYear());
            minDate.set(Calendar.MONTH, startDateTime.getMonthValue() - 1);
            minDate.set(Calendar.DAY_OF_MONTH, startDateTime.getDayOfMonth());
            Calendar maxDate = Calendar.getInstance();
            maxDate.set(Calendar.YEAR, startDateTime.getYear());
            maxDate.set(Calendar.MONTH, startDateTime.getMonthValue() - 1);
            maxDate.set(Calendar.DAY_OF_MONTH, taskType.id == 2 ? startDateTime.getDayOfMonth() + 1 : startDateTime.getDayOfMonth());

            DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(), (view, pYear, pMonth, pDayOfMonth) ->
                    taskEndDateTime.set(LocalDateTime.of(pYear, pMonth + 1, pDayOfMonth, startDateTime.getHour(), startDateTime.getMinute(), startDateTime.getSecond())), year, month, day);
            datePickerDialog.getDatePicker().setMinDate(minDate.getTimeInMillis());
            datePickerDialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());
            datePickerDialog.show();

            datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
            datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.RED);
        }
    }

    public void initViewModel() {
        TaskTypeModel taskType = selectedTaskType.get();

        if (taskType != null && taskType.id == 9) {
            SiteEntity nonUnit = new SiteEntity();
            nonUnit.id = -2;
            nonUnit.siteName = "Branch Task";
            selectedSite.set(nonUnit);
        }

        if (taskType != null && taskType.id == 8) {
            SiteEntity nonUnit = new SiteEntity();
            nonUnit.id = -1;
            nonUnit.siteName = "Non Unit";
            selectedSite.set(nonUnit);
        }

        if (taskType != null && taskType.id == 3) {
            SiteEntity barrackTask = new SiteEntity();
            barrackTask.id = -1;
            barrackTask.siteName = "Barrack";
            selectedSite.set(barrackTask);
        }
    }
}
