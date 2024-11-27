package com.droidcommons.base;

import android.app.Application;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.AndroidViewModel;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public abstract class BaseViewModel extends AndroidViewModel {

    public ObservableInt isLoading = new ObservableInt(GONE);

    protected Message message = new Message();

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public BaseViewModel(@NonNull Application application) {
        super(application);
    }


    public void setIsLoading(Boolean isLoading) {
        this.isLoading.set(isLoading ? VISIBLE : GONE);
    }

    @Override
    protected void onCleared() {
        if (compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
            compositeDisposable.clear();
        }
        super.onCleared();
    }

    protected void toast(String msg) {
        Toast.makeText(getApplication(), msg, Toast.LENGTH_SHORT).show();
    }

    protected void addDisposable(Disposable disposable) {
        compositeDisposable.add(disposable);
    }
}
