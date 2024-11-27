package com.sisindia.ai.android.workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkerParameters;

import com.droidcommons.dagger.worker.AndroidWorkerInjection;
import com.droidcommons.preference.Prefs;
import com.sisindia.ai.android.base.BaseWorker;
import com.sisindia.ai.android.constants.PrefConstants;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class AzureFileStorageWorker extends BaseWorker {

//    private boolean isSelfieRequest = false;

    @Inject
    public AzureFileStorageWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        AndroidWorkerInjection.inject(this);
    }

    @NonNull
    @Override
    public Result doWork() {

//        isSelfieRequest = getInputData().getBoolean(AzureFileStorageWorker.class.getSimpleName(), false);

        addDisposable(coreApi.getAzureSasUserContainerToken()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(response -> {
                    if (response.getStatusCode() == 200 && response.getData() != null) {
                        Prefs.putString(PrefConstants.SAS_USER_CONTAINER_KEY, response.getData().getSasToken());
//                        if (isSelfieRequest)
                        triggerFirstTimeSelfieWorker();
                    }
                }, this::onApiError));

        return Result.success();
    }

    private void triggerFirstTimeSelfieWorker() {
        Timber.e("Coming to sync selfie after getting SAS container key...");
        Data data = new Data.Builder().putBoolean(AttachmentsUploadWorker.class.getSimpleName(), true).build();
        final OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(AttachmentsUploadWorker.class).
                setInputData(data).setConstraints(WorkerUtils.networkConstraints()).build();
        WorkManager.getInstance(getApplicationContext())
                .enqueueUniqueWork(AttachmentsUploadWorker.class.getSimpleName(), ExistingWorkPolicy.REPLACE, request);

        //USING BELOW PREF TO CHECK WHETHER FIRST TIME TAKING SELFIE
//        Prefs.putBoolean(PrefConstants.IS_FIRST_TIME_LOGGED_IN, false);
    }
}
