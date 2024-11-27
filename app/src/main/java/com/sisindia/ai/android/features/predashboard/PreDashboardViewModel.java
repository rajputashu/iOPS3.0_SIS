package com.sisindia.ai.android.features.predashboard;

import static com.sisindia.ai.android.constants.NavigationConstants.OPEN_DASH_BOARD_ROTA;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

import com.droidcommons.preference.Prefs;
import com.sisindia.ai.android.R;
import com.sisindia.ai.android.base.IopsBaseViewModel;
import com.sisindia.ai.android.constants.NavigationConstants;
import com.sisindia.ai.android.constants.PrefConstants;
import com.sisindia.ai.android.room.dao.LookUpDao;
import com.sisindia.ai.android.room.dao.PreDashBoardEffortsDao;
import com.sisindia.ai.android.uimodels.EffortsModel;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class PreDashboardViewModel extends IopsBaseViewModel {

    public ObservableField<String> userName = new ObservableField<>(Prefs.getString(PrefConstants.AREA_INSPECTOR_NAME));
    public ObservableInt companyId = new ObservableInt(Prefs.getInt(PrefConstants.COMPANY_ID, 1));

    @Inject
    public PreDashBoardEffortsDao effortsDao;

    @Inject
    public LookUpDao lookUpDao;

    public ObservableField<EffortsModel.EffortBillSubmission> effortBillSubmission = new ObservableField<>(new EffortsModel.EffortBillSubmission());
    public ObservableField<EffortsModel.EffortMonInput> effortMonInput = new ObservableField<>(new EffortsModel.EffortMonInput());
    public ObservableField<EffortsModel.EffortBillCollection> effortBillCollection = new ObservableField<>(new EffortsModel.EffortBillCollection());
    public ObservableField<EffortsModel.EffortsSiteAtRisk> effortUnitAtRisk = new ObservableField<>(new EffortsModel.EffortsSiteAtRisk());
    public ObservableField<EffortsModel.EffortsDayCheck> effortDayCheck = new ObservableField<>(new EffortsModel.EffortsDayCheck());
    public ObservableField<EffortsModel.EffortsNightCheck> effortNightCheck = new ObservableField<>(new EffortsModel.EffortsNightCheck());
    public ObservableField<EffortsModel.EffortsOthers> effortOthers = new ObservableField<>(new EffortsModel.EffortsOthers());
    public ObservableField<EffortsModel.EffortUnits> effortUnits = new ObservableField<>(new EffortsModel.EffortUnits());

    @Inject
    public PreDashboardViewModel(@NonNull Application application) {
        super(application);
//        Prefs.putString(PrefConstants.PRE_DASH_BOARD_LAST_LAUNCH_DATE, LocalDateTime.now().toString());
    }


    public void onContinueBtnClick(View view) {
        message.what = OPEN_DASH_BOARD_ROTA;
        liveData.postValue(message);
    }

    public void initEfforts() {
        addDisposable(Single.zip(
                effortsDao.fetchBillSubmission(),
                effortsDao.fetchMonInput(),
                effortsDao.fetchBillCollection(),
                effortsDao.fetchUnitAtRisk(),
                effortsDao.fetchDayCheck(),
                effortsDao.fetchNightCheck(),
                effortsDao.fetchUnits(),
                effortsDao.fetchOthers(), (billSubmission, monInput, billCollection, unitAtRisk, dayCheck, nightCheck, units, others) -> {
                    effortBillSubmission.set(billSubmission);
                    effortMonInput.set(monInput);
                    effortBillCollection.set(billCollection);
                    effortUnitAtRisk.set(unitAtRisk);
                    effortDayCheck.set(dayCheck);
                    effortNightCheck.set(nightCheck);
                    effortUnits.set(units);
                    effortOthers.set(others);
                    return new EffortsModel();
                }).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(effortsModel -> Timber.d("Efforts Done"), Timber::e));
    }

    public void onCardItemsClicked(View view) {
        if (view.getId() == R.id.cvBillSubmissionLayout)
            message.what = NavigationConstants.OPEN_PRE_DASHBOARD_BILL_SUBMISSION_TASK;
        else if (view.getId() == R.id.cvMonInputLayout)
            message.what = NavigationConstants.OPEN_MONINPUT_TASK;
        else if (view.getId() == R.id.cvBillCollection)
            message.what = NavigationConstants.OPEN_PRE_DASH_BOARD_BILL_COLLECTION;
        else if (view.getId() == R.id.cvUnitsAtRisk)
            message.what = NavigationConstants.ON_PLAN_OF_ACTIONS_CLICK;
        else if (view.getId() == R.id.cvUnits)
            message.what = NavigationConstants.ON_UNITS_CLICK;
        liveData.setValue(message);

        /*switch (view.getId()) {
            case R.id.cvBillSubmissionLayout:
                message.what = NavigationConstants.OPEN_PRE_DASHBOARD_BILL_SUBMISSION_TASK;
                break;
            case R.id.cvMonInputLayout:
                message.what = NavigationConstants.OPEN_MONINPUT_TASK;
                break;
            case R.id.cvBillCollection:
                message.what = NavigationConstants.OPEN_PRE_DASH_BOARD_BILL_COLLECTION;
                break;
            case R.id.cvUnitsAtRisk:
                message.what = NavigationConstants.ON_UNITS_RISK_POA_CLICK;
                break;
            case R.id.cvUnits:
                message.what = NavigationConstants.ON_UNITS_CLICK;
                break;
        }
        liveData.setValue(message);*/
    }
}
