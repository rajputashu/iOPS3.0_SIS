package com.sisindia.ai.android.features.addgrievances;

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
import com.sisindia.ai.android.room.dao.DayCheckDao;
import com.sisindia.ai.android.room.dao.EmployeeSiteDao;
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

public class AddGrievanceSelectGuardViewModel extends IopsBaseViewModel {

    public ObservableField<List<GuardSuggestionItem>> allGuards = new ObservableField<>(new ArrayList<>());

    @Inject
    public DayCheckDao dayCheckDao;

    @Inject
    public EmployeeSiteDao employeeSiteDao;

    public GuardSuggestionViewListeners guardSuggestionViewListeners = new GuardSuggestionViewListeners() {

        @Override
        public void fetchGuardFromServer(@NotNull String employeeNo) {
            EmployeeSiteEntity emp = new EmployeeSiteEntity();
            emp.employeeNo = employeeNo;
            fetchEmployeeFromServer(emp);
        }

        @Override
        public void onGuardSelected(@NotNull GuardSuggestionItem item) {
            onGuardSuggestionSelected(item);
        }
    };

    @Inject
    public AddGrievanceSelectGuardViewModel(@NonNull Application application) {
        super(application);
    }

    private void onGuardSuggestionSelected(GuardSuggestionItem item) {
        if (item != null && item.employeeId != 0) {
            message.arg1 = item.employeeId;
            message.what = NavigationConstants.ON_OPEN_ADD_GRIEVANCE_DETAIL;
            liveData.postValue(message);
        }
    }

    public void onScanGuardQrClick(View view) {
        message.what = NavigationConstants.ON_SCAN_GUARD_QR_CLICK;
        liveData.postValue(message);
    }

    void initViewModel() {
        int siteId = Prefs.getInt(PrefConstants.CURRENT_SITE);
        if (siteId != 0) {
//            addDisposable(employeeSiteDao.fetchAllGuardsBySiteId(siteId)
            addDisposable(employeeSiteDao.fetchAllGuards()
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(guards -> allGuards.set(guards), Timber::e));
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
        } catch (Exception ignored) {
        }

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

    private void fetchEmployeeFromOffline(EmployeeSiteEntity employeeEntity) {
        addDisposable(employeeSiteDao.fetchMinGuardId()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((employeeId, throwable) -> {
                    if (employeeId > -1)
                        employeeEntity.employeeId = -1;
                    else
                        employeeEntity.employeeId = employeeId + (-1);
                    onGuardFound(employeeEntity);
                }));
    }

    public void onGuardFound(EmployeeSiteEntity emp) {
        if (emp != null && emp.employeeId != 0) {
            addDisposable(employeeSiteDao.insert(emp)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(rowId -> Timber.d("employee added"), Timber::e));
            message.arg1 = emp.employeeId;
            message.what = NavigationConstants.ON_OPEN_ADD_GRIEVANCE_DETAIL;
            liveData.postValue(message);
        }
    }
}
