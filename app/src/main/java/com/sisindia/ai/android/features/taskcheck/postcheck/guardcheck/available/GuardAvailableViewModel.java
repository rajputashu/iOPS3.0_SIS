package com.sisindia.ai.android.features.taskcheck.postcheck.guardcheck.available;

import android.app.Application;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

import com.droidcommons.preference.Prefs;
import com.sisindia.ai.android.R;
import com.sisindia.ai.android.constants.NavigationConstants;
import com.sisindia.ai.android.constants.PrefConstants;
import com.sisindia.ai.android.features.issues.grievance.GuardSuggestionViewListeners;
import com.sisindia.ai.android.features.taskcheck.postcheck.guardcheck.CheckGuardViewListeners;
import com.sisindia.ai.android.mlcore.QRScannerViewModel;
import com.sisindia.ai.android.room.dao.AttachmentDao;
import com.sisindia.ai.android.room.dao.DayCheckDao;
import com.sisindia.ai.android.room.dao.EmployeeSiteDao;
import com.sisindia.ai.android.room.dao.TaskDao;
import com.sisindia.ai.android.room.dao.UserMasterDataDao;
import com.sisindia.ai.android.room.entities.AttachmentEntity;
import com.sisindia.ai.android.room.entities.CheckedGuardEntity;
import com.sisindia.ai.android.room.entities.EmployeeSiteEntity;
import com.sisindia.ai.android.uimodels.GuardSuggestionItem;
import com.sisindia.ai.android.utils.IopsUtil;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class GuardAvailableViewModel extends QRScannerViewModel {

    public ObservableField<List<GuardSuggestionItem>> allGuards = new ObservableField<>(new ArrayList<>());
    public ObservableField<GuardAvailableStatus> guardStatus = new ObservableField<>(GuardAvailableStatus.GUARD_AVAILABLE);
    public ObservableField<EmployeeSiteEntity> selectedGuard = new ObservableField<>(new EmployeeSiteEntity());
    public ObservableInt companyId = new ObservableInt(Prefs.getInt(PrefConstants.COMPANY_ID));
    public ObservableField<String> enteredGuardNo = new ObservableField<>("");
    public boolean isComingFromPractoTask = false;

    @Inject
    public DayCheckDao dayCheckDao;

    @Inject
    public TaskDao taskDao;

    @Inject
    public EmployeeSiteDao employeeSiteDao;

    @Inject
    public UserMasterDataDao userMasterDataDao;

    @Inject
    public AttachmentDao attachmentDao;

    public AttachmentEntity attachment = new AttachmentEntity(AttachmentEntity.AttachmentSourceType.SLEEPING_GUARD);

    public GuardSuggestionViewListeners guardSuggestionViewListeners = new GuardSuggestionViewListeners() {

        @Override
        public void fetchGuardFromServer(@NotNull String employeeNo) {
            if (IopsUtil.isInternetAvailable(getApplication())) {
                EmployeeSiteEntity emp = new EmployeeSiteEntity();
                emp.employeeNo = employeeNo;
                fetchEmployeeFromServerViaTextInput(emp);
            } else
                showToast("Please check your internet connection...");
        }

        @Override
        public void onGuardSelected(@NotNull GuardSuggestionItem item) {
            setIsLoading(true);
            addDisposable(dayCheckDao.isGuardAlreadyCheckInTask(Prefs.getInt(PrefConstants.CURRENT_TASK), item.employeeId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(count -> {
                        setIsLoading(false);
                        if (count > 0)
                            showCustomToast("  Guard is already checked for this task ");
                        else
                            onGuardAvailable(item);
                    }, throwable -> {
                        setIsLoading(false);
                        showCustomToast("  Invalid Guard  ");
                    }));
        }
    };

    public CheckGuardViewListeners viewListeners = item -> {

    };

    @Inject
    public GuardAvailableViewModel(@NonNull Application application) {
        super(application);
    }

    private void onGuardAvailable(GuardSuggestionItem item) {
        int taskId = Prefs.getInt(PrefConstants.CURRENT_TASK);
        int postId = Prefs.getInt(PrefConstants.CURRENT_POST);
        int siteId = Prefs.getInt(PrefConstants.CURRENT_SITE);
        int taskServerId = Prefs.getInt(PrefConstants.TASK_SERVER_ID);

        Prefs.putInt(PrefConstants.SELECTED_EMPLOYEE_ID, item.employeeId);
        Prefs.putString(PrefConstants.SELECTED_EMPLOYEE_NO, item.employeeNo);

        GuardAvailableStatus currentState = guardStatus.get();
        CheckedGuardEntity checkedItem = new CheckedGuardEntity(item.employeeId, postId, siteId, taskId, taskServerId);
        checkedItem.guardStatus = (currentState == GuardAvailableStatus.GUARD_SLEEPING) ? CheckedGuardEntity.GuardStatus.SLEEPING.getGuardStatus() : CheckedGuardEntity.GuardStatus.AVAILABLE.getGuardStatus();
        checkedItem.employeeNo = item.employeeNo;

        if (!isComingFromPractoTask && currentState == GuardAvailableStatus.GUARD_SLEEPING) {
            Timber.e("Coming From Practo task with guard sleeping zone attachment");
            checkedItem.sleepingGuardGuid = attachment.attachmentGuid;
            addDisposable(attachmentDao.insert(attachment).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(row -> Timber.d("Sleeping guard attachment inserted"), Timber::e));
        }

        addDisposable(dayCheckDao.insertCheckedGuard(checkedItem)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(row -> {
                    message.what = (currentState == GuardAvailableStatus.GUARD_SLEEPING) ? NavigationConstants.ON_GUARD_SLEEPING : NavigationConstants.ON_GUARD_AVAILABLE;
                    liveData.postValue(message);
                }, Timber::e));
    }

    public void initViewModel() {
        /* fetch all guards for the drop down*/
        int taskId = Prefs.getInt(PrefConstants.CURRENT_TASK);
        addDisposable(dayCheckDao.fetchAllGuardsBySiteId(taskId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(guards -> {
                    allGuards.set(guards);
                }, Timber::e));
    }

    /*public void onOpenGuardSleepingClick(View view) {
    }*/

    /*public void onOpenGuardNotAvailableClick(View view) {
    }*/

    public void onViewClicks(View view) {
        if (view.getId() == R.id.guardNotAvailableId) {
            message.what = NavigationConstants.ON_OPEN_GUARD_NOT_AVAILABLE;
            liveData.postValue(message);
        } else if (view.getId() == R.id.guardSleepingId) {
            message.what = NavigationConstants.ON_OPEN_GUARD_SLEEPING_IMAGE_CAPTURE;
            message.obj = attachment;
            liveData.postValue(message);
        } else if (view.getId() == R.id.guardDetailsFromButton) {
            if (IopsUtil.isInternetAvailable(getApplication())) {
                EmployeeSiteEntity emp = new EmployeeSiteEntity();
                emp.employeeNo = enteredGuardNo.get();
//                Timber.e("Employee NO-- %s", enteredGuardNo.get());
                fetchEmployeeFromServerViaTextInput(emp);
            } else
                showToast("Please check your internet connection...");
        }
    }

    public void onGuardQrScanned(String qrRawData) {

        if (TextUtils.isEmpty(qrRawData)) {
            showToast("Qr code is empty..");
            return;
        }

        if (qrRawData.contains("Company :")) {
            showToast("Post QR is not allowed, please scan valid guard QR");
            return;
        }

        EmployeeSiteEntity guardUsingQr = new EmployeeSiteEntity();
        try {
            guardUsingQr.employeeName = qrRawData.split("\n")[0].trim();
            guardUsingQr.employeeNo = qrRawData.split("\n")[1].trim();

            addDisposable(employeeSiteDao.fetchGuardViaEmpNo(guardUsingQr.employeeNo)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe((employeeEntity, throwable) -> {
                        if (employeeEntity != null)
                            onGuardFound(employeeEntity);
                        else {
                            if (IopsUtil.isInternetAvailable(getApplication()))
                                fetchEmployeeFromServer(guardUsingQr);
                            else
                                fetchEmployeeFromOffline(guardUsingQr);
                        }
                    }));
        } catch (Exception ignored) {
            showToast("QR Code Split ERROR!");
        }
    }

    public void fetchEmployeeFromServer(EmployeeSiteEntity item) {
        setIsLoading(true);
        addDisposable(coreApi.getEmployeeByEmployeeNo(item.employeeNo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((empResponse, throwable) -> {
                    setIsLoading(false);
                    if (throwable != null) {
                        fetchEmployeeFromOffline(item);
                        return;
                    }
                    onGuardFound(empResponse.emp);
                }));
    }

    public void fetchEmployeeFromServerViaTextInput(EmployeeSiteEntity item) {
        setIsLoading(true);
        addDisposable(coreApi.getEmployeeByEmployeeNo(item.employeeNo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((empResponse, throwable) -> {
                    setIsLoading(false);
                    if (throwable != null) {
                        if (IopsUtil.isInternetAvailable(getApplication()))
                            showToast("Unable to fetch guard details from server");
                        else
                            showToast("Internet connection not available");
                        return;
                    }
                    onGuardFound(empResponse.emp);
                }));
    }

    private void fetchEmployeeFromOffline(EmployeeSiteEntity employeeEntity) {
        addDisposable(employeeSiteDao.fetchMinGuardId()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((employeeId, throwable) -> {
                    employeeEntity.employeeId = -1;
                    if (employeeId < 0)
                        employeeEntity.employeeId = employeeId + (-1);
                    onGuardFound(employeeEntity);
                }));
    }

    public void onGuardFound(EmployeeSiteEntity emp) {
        if (emp != null && emp.employeeId != 0) {
            selectedGuard.set(emp);
            message.what = NavigationConstants.ON_GUARD_DETAIL_FOUND;
            liveData.postValue(message);
        } else
            showToast("Invalid Id Or Guard Not Found");
    }

    public void onGuardCheckInClick(View view) {

        //Added this to close dialog when submit button clicked
        message.what = NavigationConstants.ON_CLOSE_DIALOG_CLICK;
        liveData.postValue(message);

        setIsLoading(true);
        EmployeeSiteEntity guard = selectedGuard.get();
        int taskId = Prefs.getInt(PrefConstants.CURRENT_TASK);
        if (guard != null && guard.employeeId != 0) {
            addDisposable(dayCheckDao.isGuardAlreadyCheckInTask(taskId, guard.employeeId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(count -> {
                        setIsLoading(false);
                        if (count > 0)
                            showCustomToast("  Guard is Already Checked In  ");
                        else
                            insertGuardIntoEmployeeSite(guard);
                    }, throwable -> {
                        setIsLoading(false);
                        showCustomToast("  Invalid Guard  ");
                    }));
        } else {
            setIsLoading(false);
            showCustomToast("  Invalid Guard Found...  ");
        }
    }

    private void insertGuardIntoEmployeeSite(EmployeeSiteEntity guard) {
        guard.siteId = Prefs.getInt(PrefConstants.CURRENT_SITE);
        addDisposable(userMasterDataDao.insertEmployeeSite(guard)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rows -> onGuardAvailable(new GuardSuggestionItem(guard.employeeId, guard.employeeNo, guard.employeeName)), Timber::e));
    }

    public void onCloseDialogClick(View view) {
        message.what = NavigationConstants.ON_CLOSE_DIALOG_CLICK;
        liveData.postValue(message);
    }

    public enum GuardAvailableStatus {
        GUARD_AVAILABLE, GUARD_SLEEPING
    }

    private void showCustomToast(String message) {
        try {
            Toast toast = Toast.makeText(getApplication(), message, Toast.LENGTH_LONG);
            toast.getView().setBackgroundResource(R.color.colorWhite);
            TextView text = toast.getView().findViewById(android.R.id.message);
            text.setTextColor(getApplication().getResources().getColor(R.color.colorLightRed));
            toast.show();
        } catch (Exception e) {
            showToast(message);
        }
    }
}