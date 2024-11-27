package com.sisindia.ai.android.features.taskcheck.postcheck.guardcheck.notavailable;

import android.app.Application;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.droidcommons.preference.Prefs;
import com.sisindia.ai.android.base.IopsBaseViewModel;
import com.sisindia.ai.android.constants.NavigationConstants;
import com.sisindia.ai.android.constants.PrefConstants;
import com.sisindia.ai.android.features.issues.grievance.GuardSuggestionViewListeners;
import com.sisindia.ai.android.features.taskcheck.postcheck.guardcheck.CheckGuardViewListeners;
import com.sisindia.ai.android.models.LookUpType;
import com.sisindia.ai.android.room.dao.DayCheckDao;
import com.sisindia.ai.android.room.dao.EmployeeSiteDao;
import com.sisindia.ai.android.room.dao.LookUpDao;
import com.sisindia.ai.android.room.entities.CheckedGuardEntity;
import com.sisindia.ai.android.room.entities.EmployeeSiteEntity;
import com.sisindia.ai.android.room.entities.LookUpEntity;
import com.sisindia.ai.android.uimodels.GuardSuggestionItem;
import com.sisindia.ai.android.utils.IopsUtil;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class GuardNotAvailableViewModel extends IopsBaseViewModel {

    public ObservableField<List<GuardSuggestionItem>> allGuards = new androidx.databinding.ObservableField<>(new ArrayList<>());
    public ObservableField<GuardSuggestionItem> selectedGuard = new ObservableField<>();
    public GuardNotAvailableRecyclerAdapter guardNotAvailableRecyclerAdapter = new GuardNotAvailableRecyclerAdapter();
    public ObservableField<String> query = new ObservableField<>("");
    public ObservableField<String> obsGuardName = new ObservableField<>("");
    public ObservableField<LookUpEntity> selectedStatus = new ObservableField<>();

    @Inject
    public DayCheckDao dayCheckDao;

    @Inject
    public LookUpDao lookUpDao;

    @Inject
    public EmployeeSiteDao employeeSiteDao;


    public GuardSuggestionViewListeners guardSuggestionViewListeners = new GuardSuggestionViewListeners() {
        @Override
        public void fetchGuardFromServer(@NotNull String employeeNo) {
//            EmployeeSiteEntity emp = new EmployeeSiteEntity();
//            emp.employeeNo = employeeNo;
//            fetchEmployeeFromServer(emp);

            if (IopsUtil.isInternetAvailable(getApplication())) {
                EmployeeSiteEntity emp = new EmployeeSiteEntity();
                emp.employeeNo = employeeNo;
                fetchEmployeeFromServer(emp);
            } else
                showToast("Please check your internet connection...");

        }

        @Override
        public void onGuardSelected(@NotNull GuardSuggestionItem item) {
            onGuardSuggestionSelected(item);
        }
    };

    public CheckGuardViewListeners viewListeners = item -> selectedStatus.set(item);

    @Inject
    public GuardNotAvailableViewModel(@NonNull Application application) {
        super(application);
    }

    private void onGuardSuggestionSelected(GuardSuggestionItem item) {
        selectedGuard.set(item);
    }

    public void initViewModel() {
        /** fetch all guards for the drop down*/
        int taskId = Prefs.getInt(PrefConstants.CURRENT_TASK);
//        int postId = Prefs.getInt(PrefConstants.CURRENT_POST);
        int siteId = Prefs.getInt(PrefConstants.CURRENT_SITE);

        if (siteId != 0) {
            addDisposable(dayCheckDao.fetchAllGuardsBySiteId(taskId)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(guards -> allGuards.set(guards), Timber::e));
        }

        addDisposable(lookUpDao.fetchAllReasons(LookUpType.GUARD_NOT_AVAILABLE.getTypeId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onGuardStatusFetch, Timber::e));
    }

    private void onGuardStatusFetch(List<LookUpEntity> list) {
        guardNotAvailableRecyclerAdapter.clearAndSetItems(list);
    }

    public void onGuardNotAvailableClick(View view) {
        LookUpEntity selectedLookUp = selectedStatus.get();
        String enteredQuery = query.get();
        GuardSuggestionItem guard = selectedGuard.get();

        if (TextUtils.isEmpty(enteredQuery)) {
            showToast("Please Enter remarks");
            return;
        }

        if (selectedLookUp == null || selectedLookUp.id == 0) {
            showToast("Please Select an Option");
            return;
        }

        if (guard == null || guard.employeeId == 0) {
            showToast("Please Select Guard");
            return;
        }
        Prefs.putInt(PrefConstants.SELECTED_EMPLOYEE_ID, guard.employeeId);
        Prefs.putString(PrefConstants.SELECTED_EMPLOYEE_NO, guard.employeeNo);

        int taskId = Prefs.getInt(PrefConstants.CURRENT_TASK);
        int postId = Prefs.getInt(PrefConstants.CURRENT_POST);
        int siteId = Prefs.getInt(PrefConstants.CURRENT_SITE);
        int employeeId = Prefs.getInt(PrefConstants.SELECTED_EMPLOYEE_ID);
        int taskServerId = Prefs.getInt(PrefConstants.TASK_SERVER_ID);

        CheckedGuardEntity item = new CheckedGuardEntity(taskId, postId, siteId, employeeId, selectedLookUp.id,
                enteredQuery, taskServerId, Prefs.getString(PrefConstants.SELECTED_EMPLOYEE_NO, ""));

        addDisposable(dayCheckDao.insertCheckedGuard(item)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(row -> {
                    message.what = NavigationConstants.ON_GUARD_NOT_AVAILABLE_CHECK;
                    liveData.postValue(message);
                }, Timber::e));
    }

    public void fetchEmployeeFromServer(EmployeeSiteEntity item) {
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
                    createSelectedGuardMO(empResponse.emp);
                    onGuardFound(empResponse.emp);
                }));
    }

    private void createSelectedGuardMO(EmployeeSiteEntity response) {
        GuardSuggestionItem item = new GuardSuggestionItem();
        item.employeeId = response.employeeId;
        item.employeeNo = response.employeeNo;
        item.employeeName = response.employeeName;
        item.siteId = response.siteId;
        item.phone = response.phone;
        selectedGuard.set(item);
        obsGuardName.set(response.employeeName);
    }

    public void onGuardFound(EmployeeSiteEntity emp) {
        if (emp != null && emp.employeeId != 0) {
            addDisposable(employeeSiteDao.insert(emp)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(rowId -> {
                        Timber.d("employee added");
                    }, Timber::e));
        }
    }
}
