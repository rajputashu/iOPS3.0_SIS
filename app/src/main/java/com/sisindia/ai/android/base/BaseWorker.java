package com.sisindia.ai.android.base;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.droidcommons.dagger.worker.AndroidWorkerInjection;
import com.droidcommons.preference.Prefs;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.sisindia.ai.android.BuildConfig;
import com.sisindia.ai.android.constants.PrefConstants;
import com.sisindia.ai.android.features.onboard.OnBoardActivity;
import com.sisindia.ai.android.models.AuthorizationValues;
import com.sisindia.ai.android.rest.AuthServerApi;
import com.sisindia.ai.android.rest.CoreApi;
import com.sisindia.ai.android.rest.GpsApi;
import com.sisindia.ai.android.rest.MastersApi;

import javax.inject.Inject;

import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;
import retrofit2.Response;
import timber.log.Timber;

import static com.sisindia.ai.android.constants.PrefConstants.CLEAR_PREFS;
import static com.sisindia.ai.android.models.AuthorizationValues.GRANT_TYPE_REFRESH_TOKEN_VALUE;

public class BaseWorker extends Worker {

    @Inject
    protected CoreApi coreApi;

    @Inject
    protected AuthServerApi authServerApi;

    @Inject
    protected MastersApi mastersApi;

    @Inject
    protected GpsApi gpsApi;

    @Inject
    protected FirebaseAnalytics analytics;


    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    public BaseWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        AndroidWorkerInjection.inject(this);
    }

    @NonNull
    @Override
    public Result doWork() {

        return Result.success();
    }

    protected void addDisposable(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    protected <T> SingleTransformer<T, T> transformSingle() {
        return single -> single.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    @Override
    public void onStopped() {
        if (compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
            compositeDisposable.clear();
        }
        super.onStopped();
    }

    protected void onApiError(Throwable throwable) {
        Timber.e(throwable);
        if (throwable instanceof HttpException) {
            Response errorResponse = ((HttpException) throwable).response();
            if (errorResponse != null && errorResponse.code() == 401) {
                Toast.makeText(getApplicationContext(), "Session Expired.. Please Wait", Toast.LENGTH_SHORT).show();
                refreshToken();
            }
            Bundle bundle = new Bundle();
            bundle.putString(PrefConstants.AREA_INSPECTOR_NAME, Prefs.getString(PrefConstants.AREA_INSPECTOR_NAME));
            bundle.putString(PrefConstants.AREA_INSPECTOR_CODE, Prefs.getString(PrefConstants.AREA_INSPECTOR_CODE));
            bundle.putString("VERSION_NAME", BuildConfig.VERSION_NAME);
            analytics.logEvent("HTTP_ERROR_LOG", bundle);
        }
    }


    protected void refreshToken() {
        addDisposable(authServerApi
                .refreshToken(GRANT_TYPE_REFRESH_TOKEN_VALUE, BuildConfig.CLIENT_ID_CC_VALUE,
                        AuthorizationValues.SECRETE_VALUE,
                        Prefs.getString(PrefConstants.REFRESH_TOKEN_KEY))
                .compose(transformSingle()).subscribe(authResponse -> {
                    Prefs.putString(PrefConstants.AUTH_TOKEN_KEY, PrefConstants.Bearer.concat(authResponse.accessToken));
                    Prefs.putString(PrefConstants.REFRESH_TOKEN_KEY, authResponse.refreshToken);
                    Toast.makeText(getApplicationContext(), "Session Renewed, Please try again...", Toast.LENGTH_SHORT).show();
                }, throwable -> {
                    Toast.makeText(getApplicationContext(), "Session Expired, Please Login", Toast.LENGTH_SHORT).show();
                    Prefs.clear();
                    Prefs.putBoolean(CLEAR_PREFS, false);
                    getApplicationContext().startActivity(OnBoardActivity.newIntent(getApplicationContext().getApplicationContext()));
                }));
    }
}
