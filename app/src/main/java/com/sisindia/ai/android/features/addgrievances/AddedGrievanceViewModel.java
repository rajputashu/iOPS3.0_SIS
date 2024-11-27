package com.sisindia.ai.android.features.addgrievances;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.sisindia.ai.android.base.IopsBaseViewModel;
import com.sisindia.ai.android.constants.NavigationConstants;
import com.sisindia.ai.android.models.GrievanceModel;
import com.sisindia.ai.android.room.dao.GrievanceDao;
import com.sisindia.ai.android.uimodels.ClosedGrievanceModel;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class AddedGrievanceViewModel extends IopsBaseViewModel {

    @Inject
    public GrievanceDao grievanceDao;

    public ObservableField<GrievanceModel> grievance = new ObservableField<>(new GrievanceModel());

    public ObservableField<ClosedGrievanceModel> closedGrievance = new ObservableField<>(new ClosedGrievanceModel());

    @Inject
    public AddedGrievanceViewModel(@NonNull Application application) {
        super(application);
    }

    public void onOpenGrievance(int grievanceId) {
        addDisposable(grievanceDao.fetchGrievanceById(grievanceId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(item -> grievance.set(item), Timber::e));
    }

    public void onClosedGrievance(int grievanceId) {
        addDisposable(grievanceDao.fetchDetailOnGrievanceClose(grievanceId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(item -> closedGrievance.set(item), Timber::e));
    }


    public void onAddedGrievanceContinue(View view) {
        message.what = NavigationConstants.ON_GRIEVANCE_ADDED_CONTINUE;
        liveData.postValue(message);
    }
}
