package com.sisindia.ai.android.base;

import dagger.android.DaggerIntentService;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public abstract class IopsBaseIntentService extends DaggerIntentService {

    protected CompositeDisposable compositeDisposable = new CompositeDisposable();

    public IopsBaseIntentService(String name) {
        super(name);
    }

    protected void addDisposable(Disposable disposable) {
        compositeDisposable.add(disposable);
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
