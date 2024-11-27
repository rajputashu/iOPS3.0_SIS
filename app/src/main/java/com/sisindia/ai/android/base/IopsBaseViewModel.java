package com.sisindia.ai.android.base;

import static com.sisindia.ai.android.constants.PrefConstants.CLEAR_PREFS;
import static com.sisindia.ai.android.models.AuthorizationValues.GRANT_TYPE_REFRESH_TOKEN_VALUE;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.location.LocationManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.annotation.StringRes;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.droidcommons.base.BaseViewModel;
import com.droidcommons.base.SingleLiveEvent;
import com.droidcommons.base.timer.SingleLiveTimerEvent;
import com.droidcommons.preference.Prefs;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sisindia.ai.android.BuildConfig;
import com.sisindia.ai.android.R;
import com.sisindia.ai.android.constants.NavigationConstants;
import com.sisindia.ai.android.constants.PrefConstants;
import com.sisindia.ai.android.features.onboard.OnBoardActivity;
import com.sisindia.ai.android.models.AuthorizationValues;
import com.sisindia.ai.android.models.DeviceInfo;
import com.sisindia.ai.android.rest.AuthServerApi;
import com.sisindia.ai.android.rest.CoreApi;
import com.sisindia.ai.android.rest.MastersApi;
import com.sisindia.ai.android.room.dao.DutyStatusDao;
import com.sisindia.ai.android.workers.WorkerUtils;

import org.threeten.bp.LocalTime;

import java.net.ConnectException;

import javax.inject.Inject;

import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.HttpException;
import retrofit2.Response;
import timber.log.Timber;

public class IopsBaseViewModel extends BaseViewModel {

//    private final Handler handler = new Handler();

    @Inject
    protected CoreApi coreApi;

    @Inject
    protected DutyStatusDao dutyStatusDao;

    @Inject
    protected AuthServerApi authServerApi;

    @Inject
    protected MastersApi masterApi;

    @Inject
    protected FirebaseAnalytics analytics;

    @Inject
    protected SingleLiveTimerEvent<Message> liveTimerEvent;

    @Inject
    protected SingleLiveEvent<Message> liveData;

    @Inject
    public IopsBaseViewModel(@NonNull Application application) {
        super(application);
    }

    protected <T> SingleTransformer<T, T> transformSingle() {
        return single -> single.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    protected void onUserSession(DeviceInfo deviceInfo) {
        Prefs.putBoolean(PrefConstants.IS_LOGGED_IN, true);
//        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(instanceIdResult -> {
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(fcmToken -> {
            Prefs.putString(PrefConstants.FCM_TOKEN, fcmToken);
//            deviceInfo.appToken = instanceIdResult.getToken();
            deviceInfo.appToken = fcmToken;
            deviceInfo.phoneNumber = Prefs.getString(PrefConstants.COUNTRY_CODE).concat("-").concat(Prefs.getString(PrefConstants.USER_MOBILE_NUMBER));
            deviceInfo.androidOSVersion = String.valueOf(Build.VERSION.SDK_INT);
            deviceInfo.deviceManufacturer = Build.MANUFACTURER;
            deviceInfo.deviceModel = Build.MODEL;
            addDisposable(coreApi.updateDeviceInfo((deviceInfo))
                    .compose(transformSingle())
                    .subscribe(this::onDeviceInfoResponse, this::onApiError));
        });
        FirebaseCrashlytics.getInstance().setUserId(Prefs.getString(PrefConstants.USER_MOBILE_NUMBER));
    }

    private void onDeviceInfoResponse(JsonObject response) {
        Timber.e(response.toString());
    }

    protected void showToast(@StringRes int resId) {
        Toast.makeText(getApplication(), resId, Toast.LENGTH_SHORT).show();
    }

    protected void showToast(String msg) {
        Toast.makeText(getApplication(), msg, Toast.LENGTH_SHORT).show();
    }

    protected void oneTimeWorkerWithNetwork(final Class worker) {
        final OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(worker)
                .setConstraints(WorkerUtils.networkConstraints()).build();
        WorkManager.getInstance(getApplication())
                .enqueueUniqueWork(worker.getSimpleName(), ExistingWorkPolicy.REPLACE, request);
    }

    protected void oneTimeWorkerWithInputData(final Class worker, Data input) {
        final OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(worker).setInputData(input)
                .setConstraints(WorkerUtils.networkConstraints()).build();
        WorkManager.getInstance(getApplication())
                .enqueueUniqueWork(worker.getSimpleName(), ExistingWorkPolicy.REPLACE, request);
    }

    protected void oneTimeKeepWorkerWithInputData(final Class worker, Data input) {
        final OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(worker).setInputData(input)
                .setConstraints(WorkerUtils.networkConstraints()).build();
        WorkManager.getInstance(getApplication()).enqueueUniqueWork(worker.getSimpleName(), ExistingWorkPolicy.KEEP, request);
    }

    protected void oneTimeBackgroundWorker(final Class worker, Data input) {
        final OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(worker).setInputData(input).build();
        WorkManager.getInstance(getApplication()).enqueueUniqueWork(worker.getSimpleName(), ExistingWorkPolicy.REPLACE, request);
    }

    /*protected void setDutyStatus(boolean isDutyOn) {
        Data inputData = new Data.Builder().putBoolean(DutyStatusWorker.class.getSimpleName(), isDutyOn).build();
        oneTimeBackgroundWorker(DutyStatusWorker.class, inputData);
    }*/

    protected void onApiError(Throwable throwable) {
        Timber.e(throwable);
        setIsLoading(false);
        if (throwable instanceof HttpException) {
            Response errorResponse = ((HttpException) throwable).response();
            if (errorResponse != null && errorResponse.code() == 401) {
//                showToast("Session Expired.. Please Wait");
                refreshToken();
            } else if (errorResponse != null) {
                ResponseBody errorResponseBody = errorResponse.errorBody();
                if (errorResponseBody != null) {
                    ApiError response = new Gson().fromJson(errorResponseBody.charStream(), ApiError.class);
                    if (response != null) {
                        showToast(TextUtils.isEmpty(response.statusMessage) ? response.description : response.statusMessage);
                    } else {
                        showToast(R.string.something_went_wrong);
                    }
                }
            }
        } else if (throwable instanceof ConnectException)
            showToast("Please Check Internet Connection");
        else
            showToast("Please Check Internet Connection");
    }

    private void refreshToken() {
        setIsLoading(true);
        addDisposable(authServerApi
                .refreshToken(GRANT_TYPE_REFRESH_TOKEN_VALUE, BuildConfig.CLIENT_ID_CC_VALUE,
                        AuthorizationValues.SECRETE_VALUE,
                        Prefs.getString(PrefConstants.REFRESH_TOKEN_KEY))
                .compose(transformSingle()).subscribe(authResponse -> {
                    setIsLoading(false);
                    Prefs.putString(PrefConstants.AUTH_TOKEN_KEY, PrefConstants.Bearer.concat(authResponse.accessToken));
                    Prefs.putString(PrefConstants.REFRESH_TOKEN_KEY, authResponse.refreshToken);
//                    showToast("Session Renewed, Please try again...");
                }, throwable -> {
                    setIsLoading(false);
                    showToast("Session Expired, Please Login");
                    Prefs.clear();
                    Prefs.putBoolean(CLEAR_PREFS, false);
                    getApplication().startActivity(OnBoardActivity.newIntent(getApplication().getApplicationContext()));
                }));
    }

    public boolean isGspEnabled() {
        boolean gps_enabled = false;
        LocationManager lm = (LocationManager) getApplication().getSystemService(Context.LOCATION_SERVICE);
        try {
            if (lm != null) {
                gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            }
        } catch (Exception e) {
            Timber.e(e);
            gps_enabled = false;
        }
        return gps_enabled;
    }

    public class TaskTimer extends CountDownTimer {
        private static final long INTERVAL_MS = 1000;
        private static final long MAX_MILLIS_OF_DAY = 86400000;
        public long lastTik;

        @SuppressLint("NewApi")
        public TaskTimer(long durationMs) {
//            super(durationMs == 0 ? Long.MAX_VALUE : durationMs, INTERVAL_MS);
            super(durationMs == 0 ? MAX_MILLIS_OF_DAY : durationMs, INTERVAL_MS);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onTick(long msUntilFinished) {
            this.lastTik = msUntilFinished;
//            int sec = (int) ((Long.MAX_VALUE - msUntilFinished) / INTERVAL_MS);
            int sec = (int) ((MAX_MILLIS_OF_DAY - msUntilFinished) / INTERVAL_MS);
            message.what = NavigationConstants.TASK_TIMER_TIK;
            message.arg1 = sec;
            liveData.postValue(message);
        }

        @Override
        public void onFinish() {

        }
    }

    public long getCurrentMilliOfTheDay() {
//        return (LocalTime.now().toSecondOfDay() * 1000);
        return (LocalTime.now().toSecondOfDay() * 1000L);
    }

    public long findTimeDifference(long lastElapsedTime) {
        long timeDifference = 0;
//        if (model.getLastElapsedTime() > 0)
        if (lastElapsedTime > 0)
            timeDifference = getCurrentMilliOfTheDay() - lastElapsedTime;
        return timeDifference;
    }
}
