package com.sisindia.ai.android.base;

import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.sisindia.ai.android.workers.LocationWorker;
import com.sisindia.ai.android.workers.WorkerUtils;

import dagger.android.DaggerService;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public abstract class IopsBaseService extends DaggerService {

    protected CompositeDisposable compositeDisposable = new CompositeDisposable();

    protected void addDisposable(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    protected void oneTimeWorkerOfGeoPings() {
        final OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(LocationWorker.class)
                .setConstraints(WorkerUtils.networkConstraints()).build();
        WorkManager.getInstance(getApplication()).enqueueUniqueWork("PING", ExistingWorkPolicy.REPLACE,request);
    }

    @Override
    public void onDestroy() {
        if (compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
            compositeDisposable.clear();
        }
        Timber.e("Service Destroyed");
        super.onDestroy();
    }

}
