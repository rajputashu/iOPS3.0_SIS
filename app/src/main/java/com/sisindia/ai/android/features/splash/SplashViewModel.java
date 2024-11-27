package com.sisindia.ai.android.features.splash;

import static com.sisindia.ai.android.constants.NavigationConstants.OPEN_DASH_BOARD;
import static com.sisindia.ai.android.constants.NavigationConstants.OPEN_LOGIN;
import static com.sisindia.ai.android.constants.PrefConstants.AUTH_TOKEN_KEY;
import static com.sisindia.ai.android.models.AuthorizationValues.GRANT_TYPE_CREDENTIAL_VALUE;
import static com.sisindia.ai.android.models.AuthorizationValues.GRANT_TYPE_REFRESH_TOKEN_VALUE;
import static com.sisindia.ai.android.models.AuthorizationValues.SECRETE_VALUE;

import android.app.Application;
import android.os.Handler;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.droidcommons.preference.Prefs;
import com.sisindia.ai.android.BuildConfig;
import com.sisindia.ai.android.base.IopsBaseViewModel;
import com.sisindia.ai.android.constants.NavigationConstants;
import com.sisindia.ai.android.constants.PrefConstants;
import com.sisindia.ai.android.models.AppVersionResponseMO;
import com.sisindia.ai.android.models.AuthResponse;
import com.sisindia.ai.android.models.AuthorizationValues;
import com.sisindia.ai.android.models.DeviceInfo;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SplashViewModel extends IopsBaseViewModel {

    private static final int DELAY_TIME = 100;
    public ObservableField<String> loadingMsg = new ObservableField<>("Loading please wait...");

    @Inject
    DeviceInfo deviceInfo;
    private final Handler splashHandler = new Handler();
    private final Runnable loginRunnable = this::initViewModel;
//    private Runnable dashBoardRunnable = this::refreshToken;

    @Inject
    public SplashViewModel(@NonNull Application application) {
        super(application);
    }

    public void checkAppVersion() {
        setIsLoading(true);
        addDisposable(coreApi.getCurrentVersion(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onCurrentVersionResponse, e -> {
                    e.printStackTrace();
                    checkUserState();
                }));
    }

    private void onCurrentVersionResponse(AppVersionResponseMO versionResponse) {
        if (versionResponse != null && versionResponse.getAppVersionData() != null) {
            if (versionResponse.getAppVersionData().isForcedUpdate()) {
                if (BuildConfig.VERSION_CODE >= versionResponse.getAppVersionData().getVersionCode()) {
                    checkUserState();
                } else {
                    message.what = NavigationConstants.OPEN_VERSION_INFO_DIALOG;
                    message.obj = versionResponse.getAppVersionData().getApkFileHostedUrl();
                    liveData.postValue(message);
                }
            } else
                checkUserState();
        } else
            checkUserState();
    }

    void checkUserState() {
        String authToken = Prefs.getString(AUTH_TOKEN_KEY);
        if (TextUtils.isEmpty(authToken))
            splashHandler.postDelayed(loginRunnable, DELAY_TIME);
        else {
            // COMMENTING REFRESH TOKEN : AS ITS CREATING SLOW PROBLEM
//            splashHandler.postDelayed(dashBoardRunnable, DELAY_TIME);
            refreshToken();
            message.what = OPEN_DASH_BOARD;
            liveData.postValue(message);
        }

        //setting default value of "Conveyance API" in shared pref
//        Prefs.putBoolean(PrefConstants.IS_FIRST_TIME_CALLING_CONVEYANCE, true);
    }

    public void initViewModel() {
        addDisposable(authServerApi
                .getPreAuth(BuildConfig.CLIENT_ID_CC_VALUE, SECRETE_VALUE, GRANT_TYPE_CREDENTIAL_VALUE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onPreAuthResponse, this::hideProgressBarOnError));
//                .subscribe(this::onPreAuthResponse, this::onApiError));
    }

    private void hideProgressBarOnError(Throwable throwable) {
        setIsLoading(false);
        loadingMsg.set("Error! Please check your internet...");
    }

    private void onPreAuthResponse(AuthResponse response) {
//        setIsLoading(false);
        if (response != null) {
            Prefs.putString(PrefConstants.PRE_AUTH_TOKEN_KEY, PrefConstants.Bearer.concat(response.accessToken));
            message.what = OPEN_LOGIN;
            liveData.postValue(message);
        }
    }

    public void refreshToken() {
//        Timber.e("Coming to refreshToken() Call API");
        addDisposable(authServerApi.refreshToken(GRANT_TYPE_REFRESH_TOKEN_VALUE, BuildConfig.CLIENT_ID_CC_VALUE,
                AuthorizationValues.SECRETE_VALUE, Prefs.getString(PrefConstants.REFRESH_TOKEN_KEY))
                .compose(transformSingle()).subscribe((authResponse, throwable) -> {
                    if (authResponse != null) {
//                        Timber.e("Coming to update Token in BG");
                        Prefs.putString(PrefConstants.AUTH_TOKEN_KEY, PrefConstants.Bearer.concat(authResponse.accessToken));
                        Prefs.putString(PrefConstants.REFRESH_TOKEN_KEY, authResponse.refreshToken);
                        onUserSession(deviceInfo);
                    }
//                    message.what = OPEN_DASH_BOARD;
//                    liveData.postValue(message);
                }));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        splashHandler.removeCallbacks(loginRunnable);
//        splashHandler.removeCallbacks(dashBoardRunnable);
    }
}