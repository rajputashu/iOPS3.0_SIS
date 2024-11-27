package com.sisindia.ai.android.features.addkitrequest;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.sisindia.ai.android.base.IopsBaseViewModel;
import com.sisindia.ai.android.constants.NavigationConstants;
import com.sisindia.ai.android.room.dao.GuardKitRequestDao;
import com.sisindia.ai.android.uimodels.KitRequestModel;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class AddedKitRequestViewModel extends IopsBaseViewModel {

    @Inject
    public GuardKitRequestDao kitRequestDao;

    public ObservableField<KitRequestModel> item = new ObservableField<>(new KitRequestModel());

    @Inject
    public AddedKitRequestViewModel(@NonNull Application application) {
        super(application);
    }

    public void initViewModel(int rowId) {
        addDisposable(kitRequestDao.fetchAddedKitRequestItem(rowId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> item.set(model), Timber::e));
    }


    public void onAddedKitRequestContinue(View view) {
        message.what = NavigationConstants.ON_KIT_REQUEST_COMPLETED;
        liveData.postValue(message);
    }
}
