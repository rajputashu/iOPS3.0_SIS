package com.sisindia.ai.android.features.taskcheck.postcheck.postguardscan;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

import com.droidcommons.preference.Prefs;
import com.sisindia.ai.android.base.IopsBaseViewModel;
import com.sisindia.ai.android.constants.NavigationConstants;
import com.sisindia.ai.android.constants.PrefConstants;
import com.sisindia.ai.android.features.addgrievances.GrievanceRecyclerAdapter;
import com.sisindia.ai.android.features.addkitrequest.KitRequestRecyclerAdapter;
import com.sisindia.ai.android.room.dao.DayCheckDao;
import com.sisindia.ai.android.room.dao.EmployeeFineRewardDao;
import com.sisindia.ai.android.room.dao.GrievanceDao;
import com.sisindia.ai.android.room.dao.GuardKitRequestDao;
import com.sisindia.ai.android.uimodels.EmployeePostScanModel;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class GuardCheckScanSuccessViewModel extends IopsBaseViewModel {

    public GrievanceRecyclerAdapter grievanceRecyclerAdapter = new GrievanceRecyclerAdapter();

    public ObservableField<EmployeePostScanModel> checkedGuardObs = new ObservableField<>(new EmployeePostScanModel());
    public ObservableInt grievancePendingCount = new ObservableInt(0);
    public KitRequestRecyclerAdapter kitRequestRecyclerAdapter = new KitRequestRecyclerAdapter();
    public ObservableInt kitRequestAllCount = new ObservableInt(0);

    public ObservableField<String> rewardCount = new ObservableField<>();
    public ObservableField<String> fineCount = new ObservableField<>();

    public int companyId = Prefs.getInt(PrefConstants.COMPANY_ID);

    @Inject
    public DayCheckDao dayCheckDao;

    @Inject
    public GrievanceDao grievanceDao;

    @Inject
    public GuardKitRequestDao kitRequestDao;

    @Inject
    public EmployeeFineRewardDao fineRewardDao;

    @Inject
    public GuardCheckScanSuccessViewModel(@NonNull Application application) {
        super(application);
    }

    public void onTakePhotoClick(View view) {
        message.what = NavigationConstants.ON_TAKE_GUARD_PHOTO_FOR_EVALUATION;
        liveData.postValue(message);
    }


    public void initViewModel() {
        int taskId = Prefs.getInt(PrefConstants.CURRENT_TASK);
        int postId = Prefs.getInt(PrefConstants.CURRENT_POST);
        int siteId = Prefs.getInt(PrefConstants.CURRENT_SITE);
        int employeeId = Prefs.getInt(PrefConstants.SELECTED_EMPLOYEE_ID);

        if (employeeId != 0 && postId != 0 && siteId != 0 && taskId != 0) {
            addDisposable(dayCheckDao.fetchEmployeePostScan(employeeId, taskId, siteId, postId)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onEmployeeFetch, Timber::e));
        }

        addDisposable(grievanceDao.fetchAllOpenGrievanceByEmployeeId(employeeId, siteId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                    grievancePendingCount.set(list.size());
                    grievanceRecyclerAdapter.clearAndSetItems(list);
                }, Timber::e));

        addDisposable(kitRequestDao.fetchAllEmployeeKitRequestsBySite(employeeId, siteId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                    kitRequestAllCount.set(list.size());
                    kitRequestRecyclerAdapter.clearAndSetItems(list);
                }, Timber::e));
    }

    private void onEmployeeFetch(EmployeePostScanModel model) {
        setIsLoading(true);
//        addDisposable(coreApi.getEmployeeRewardSummary(model.employeeNo)
        addDisposable(fineRewardDao.getRewardOrFineViaEmpId(model.employeeNo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(item -> {
                    setIsLoading(false);
                    if (item != null) {
                        model.totalFine = (int) Math.round(item.totalFine);
                        model.totalReward = (int) Math.round(item.totalReward);
                        rewardCount.set("No of times reward added :" + item.rewardCount);
                        fineCount.set("No of times fine added :" + item.fineCount);
                        checkedGuardObs.set(model);
                    }

                    /*if (item != null && item.summary != null) {
                        model.totalFine = (int) Math.round(item.summary.totalFine);
                        model.totalReward = (int) Math.round(item.summary.totalReward);
                        rewardCount.set("No of times reward added :" + item.summary.rewardCount);
                        fineCount.set("No of times fine added :" + item.summary.fineCount);
                        checkedGuardObs.set(model);
                    }*/
                }, e -> setIsLoading(false)));
    }
}
