package com.sisindia.ai.android.features.login.otp;

import static com.sisindia.ai.android.constants.NavigationConstants.ON_UPDATING_LOADING_TIME;
import static com.sisindia.ai.android.constants.NavigationConstants.OPEN_DASH_BOARD;

import android.app.Application;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.work.Data;

import com.droidcommons.preference.Prefs;
import com.droidcommons.views.otpview.OnOtpCompletionListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.sisindia.ai.android.BuildConfig;
import com.sisindia.ai.android.base.BaseNetworkResponse;
import com.sisindia.ai.android.base.IopsBaseViewModel;
import com.sisindia.ai.android.commons.SyncPOA;
import com.sisindia.ai.android.constants.PrefConstants;
import com.sisindia.ai.android.models.AuthResponse;
import com.sisindia.ai.android.models.AuthorizationValues;
import com.sisindia.ai.android.models.DeviceInfo;
import com.sisindia.ai.android.models.UserOnBoardModel;
import com.sisindia.ai.android.room.dao.AIProfileDao;
import com.sisindia.ai.android.workers.CommonMasterDataWorker;
import com.sisindia.ai.android.workers.ComplaintWorker;
import com.sisindia.ai.android.workers.EmployeesRewardFineWorker;
import com.sisindia.ai.android.workers.GrievanceWorker;
import com.sisindia.ai.android.workers.RotaTaskWorker;
import com.sisindia.ai.android.workers.StateDistrictWorker;
import com.sisindia.ai.android.workers.SyncPoaWorker;
import com.sisindia.ai.android.workers.UserMasterDataWorkerV2;
import com.sisindia.ai.android.workers.WeekRotaTaskWorker;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class EnterOtpViewModel extends IopsBaseViewModel {

    public ObservableField<String> otpText = new ObservableField<>("");
    public ObservableField<String> phoneNumber = new ObservableField<>(Prefs.getString(PrefConstants.COUNTRY_CODE).concat("-").concat(Prefs.getString(PrefConstants.USER_MOBILE_NUMBER)));
    public ObservableBoolean isDataSyncing = new ObservableBoolean(false);

    public OnOtpCompletionListener otpCompletionListener = otp -> {
//        Toast.makeText(getApplication(), "Submitting OTP.....", Toast.LENGTH_SHORT).show();
//        onValidateBtnClick(null);
    };

    @Inject
    public AIProfileDao aiProfileDao;

    @Inject
    public DeviceInfo deviceInfo;

    private final Handler handler = new Handler();

    @Inject
    public EnterOtpViewModel(@NonNull Application application) {
        super(application);
    }

    public void onValidateBtnClick(View view) {

        String otp = otpText.get();
        String phoneNumber = Prefs.getString(PrefConstants.COUNTRY_CODE).concat("-").concat(Prefs.getString(PrefConstants.USER_MOBILE_NUMBER));
        if (!TextUtils.isEmpty(otp) && otp.length() == 6 && !TextUtils.isEmpty(phoneNumber)) {
            setIsLoading(true);
            addDisposable(authServerApi
                    .submitOtp(AuthorizationValues.GRANT_TYPE_PASSWORD_VALUE,
                            phoneNumber, otp, BuildConfig.CLIENT_ID_RO_VALUE,
                            AuthorizationValues.SECRETE_VALUE, BuildConfig.SCOPE_VALUE)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onOtpSubmitSuccess, this::onApiError));

        } else {
            Toast.makeText(getApplication(), "Invalid Otp", Toast.LENGTH_SHORT).show();
        }
    }

    private void onOtpSubmitSuccess(AuthResponse authResponse) {
        if (authResponse != null) {

            long delayTime = 50000;
            setIsLoading(false);
            isDataSyncing.set(true);
            calculateLoadingPercentage(delayTime);
//            calculateLoadingPercentage();

            //Delete AI profile from database
            addDisposable(aiProfileDao.deleteAiProfile()
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(rows -> {
                    }, Timber::e));

            Prefs.putBoolean(PrefConstants.IS_LOGGED_IN, true);
            Prefs.putString(PrefConstants.AUTH_TOKEN_KEY, PrefConstants.Bearer.concat(authResponse.accessToken));
            Prefs.putString(PrefConstants.REFRESH_TOKEN_KEY, authResponse.refreshToken);

            FirebaseMessaging.getInstance().getToken().addOnSuccessListener(fcmToken -> {
                Prefs.putString(PrefConstants.FCM_TOKEN, fcmToken);
                deviceInfo.appToken = fcmToken;
                deviceInfo.phoneNumber = Prefs.getString(PrefConstants.COUNTRY_CODE).concat("-").concat(Prefs.getString(PrefConstants.USER_MOBILE_NUMBER));
                deviceInfo.androidOSVersion = String.valueOf(Build.VERSION.SDK_INT);
                deviceInfo.deviceManufacturer = Build.MANUFACTURER;
                deviceInfo.deviceModel = Build.MODEL;

                addDisposable(coreApi.updateDeviceInfo((deviceInfo))
                        .compose(transformSingle())
                        .subscribe(jsonObject -> Timber.i("Device Info update success"), this::onApiError));

                addDisposable(coreApi.fetchAIProfile()
                        .compose(transformSingle())
                        .subscribe(response -> {
                            if (response != null && response.getAiProfileData() != null) {
                                Prefs.putInt(PrefConstants.AREA_INSPECTOR_ID, response.getAiProfileData().id);
                                Prefs.putString(PrefConstants.AREA_INSPECTOR_NAME, response.getAiProfileData().employeeName);
                                Prefs.putString(PrefConstants.AREA_INSPECTOR_CODE, response.getAiProfileData().employeeNo);
                                Prefs.putInt(PrefConstants.COMPANY_ID, response.getAiProfileData().companyId);
//                                Prefs.putInt(PrefConstants.COMPANY_ID, 3);
                                addDisposable(aiProfileDao.insertMasterAIDetails(response.getAiProfileData())
                                        .subscribeOn(Schedulers.newThread())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(row -> Timber.i("Ai Profile inserted"), Timber::e));
                            } else {
                                showToast("Unable to get Profile");
                            }
                        }, this::onApiError));
            });

            oneTimeWorkerWithNetwork(CommonMasterDataWorker.class);
            oneTimeWorkerWithNetwork(UserMasterDataWorkerV2.class);

            Data rotaData = new Data.Builder().putInt(RotaTaskWorker.class.getSimpleName(),
                    RotaTaskWorker.RotaTaskWorkerType.SYNC_FROM_SERVER.getWorkerType()).build();
            oneTimeWorkerWithInputData(RotaTaskWorker.class, rotaData);

            Data grievanceData = new Data.Builder().putInt(GrievanceWorker.class.getSimpleName(),
                    GrievanceWorker.GrievanceWorkerType.SYNC_FROM_SERVER.getWorkerType()).build();
            oneTimeWorkerWithInputData(GrievanceWorker.class, grievanceData);

            Data complaintData = new Data.Builder().putInt(ComplaintWorker.class.getSimpleName(),
                    ComplaintWorker.ComplaintWorkerType.SYNC_FROM_SERVER.getWorkerType()).build();
            oneTimeWorkerWithInputData(ComplaintWorker.class, complaintData);

            oneTimeWorkerWithNetwork(WeekRotaTaskWorker.class);

            /*SYNCING EMPLOYEES AND FINE-REWARD DATA*/
            oneTimeWorkerWithNetwork(EmployeesRewardFineWorker.class);
            //Syncing State and District Data
            oneTimeWorkerWithNetwork(StateDistrictWorker.class);

            handler.postDelayed(this::syncAtRiskAndIpPoaFromServer, delayTime);

        } else {
            Toast.makeText(getApplication(), "Invalid Authentication", Toast.LENGTH_SHORT).show();
        }
    }

    public void onResendOtpClick(View view) {
        setIsLoading(true);

        int countryId = Prefs.getInt(PrefConstants.COUNTRY_ID);
        String countryCode = Prefs.getString(PrefConstants.COUNTRY_CODE);
        String phoneNumber = Prefs.getString(PrefConstants.USER_MOBILE_NUMBER);
        UserOnBoardModel request = new UserOnBoardModel(countryCode, countryId, phoneNumber, true);
        String preAuthToken = Prefs.getString(PrefConstants.PRE_AUTH_TOKEN_KEY);

        addDisposable(coreApi.sendOtp(preAuthToken, request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onMobileNumberSubmitSuccess, this::onApiError));
    }

    private void onMobileNumberSubmitSuccess(BaseNetworkResponse response) {
        setIsLoading(false);
        if (response.statusCode == 200)
            Toast.makeText(getApplication(), "Otp Sent Success", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getApplication(), response.statusMessage, Toast.LENGTH_LONG).show();
    }

    private void syncAtRiskAndIpPoaFromServer() {

        Data atRiskData = new Data.Builder().putInt(SyncPoaWorker.class.getSimpleName(),
                SyncPOA.AT_RISK.getTypeId()).build();
        oneTimeWorkerWithInputData(SyncPoaWorker.class, atRiskData);

        handler.postDelayed(() -> {
            Data improvementData = new Data.Builder().putInt(SyncPoaWorker.class.getSimpleName(),
                    SyncPOA.IMPROVEMENT_PLAN.getTypeId()).build();
            oneTimeWorkerWithInputData(SyncPoaWorker.class, improvementData);
            message.what = OPEN_DASH_BOARD;
            liveData.postValue(message);
        }, 3000);
    }

    private CountDownTimer cTimer = null;
    public ObservableInt percentageTime = new ObservableInt(0);

    /*private void calculateLoadingPercentage(long loaderTime) {
        long finalTime = loaderTime + 4000;
        cTimer = new CountDownTimer(finalTime, 2000) {
            public void onTick(long tickTime) {
                int percentage = (int) ((tickTime * 100) / finalTime);
                int updatedPercentage = 100 - percentage;
                percentageTime.set(updatedPercentage);
                message.what = ON_UPDATING_LOADING_TIME;
                message.arg1 = updatedPercentage;
                liveData.postValue(message);
            }

            public void onFinish() {
            }
        };
        cTimer.start();
    }*/

    private void calculateLoadingPercentage(long finalTime) {
//        long finalTime = 60000;

        cTimer = new CountDownTimer(finalTime, 2000) {
            public void onTick(long tickTime) {
                int percentage = (int) ((tickTime * 100) / finalTime);
                int updatedPercentage = 100 - percentage;
                percentageTime.set(updatedPercentage);

                message.what = ON_UPDATING_LOADING_TIME;
                message.arg1 = updatedPercentage;
                liveData.postValue(message);
            }

            public void onFinish() {
                percentageTime.set(100);
                message.what = ON_UPDATING_LOADING_TIME;
                message.arg1 = 100;
                liveData.postValue(message);
            }
        };
        cTimer.start();
    }


    public void cancelTimer() {
        if (cTimer != null)
            cTimer.cancel();
    }
}
