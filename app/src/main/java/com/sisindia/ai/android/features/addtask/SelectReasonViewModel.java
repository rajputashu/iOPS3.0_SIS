package com.sisindia.ai.android.features.addtask;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;

import com.sisindia.ai.android.base.IopsBaseViewModel;
import com.sisindia.ai.android.constants.NavigationConstants;
import com.sisindia.ai.android.models.LookUpType;
import com.sisindia.ai.android.room.dao.LookUpDao;
import com.sisindia.ai.android.room.entities.LookUpEntity;
import com.sisindia.ai.android.room.entities.SiteEntity;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class SelectReasonViewModel extends IopsBaseViewModel {

    public ReasonListRecyclerAdapter recyclerAdapter = new ReasonListRecyclerAdapter();


    public ObservableBoolean isReasonAvailable = new ObservableBoolean(false);

    public AddTaskViewListeners viewListeners = new AddTaskViewListeners() {

        @Override
        public void onSiteSelected(SiteEntity item) {
        }

        @Override
        public void onReasonSelected(LookUpEntity item) {
            message.what = NavigationConstants.ON_CREATE_TASK_REASON_SELECTED;
            message.obj = item;
            liveData.postValue(message);
        }

        @Override
        public void onSubTaskTypeSelected(LookUpEntity item) {

        }

    };

    @Inject
    LookUpDao lookUpDao;


    @Inject
    public SelectReasonViewModel(@NonNull Application application) {
        super(application);
    }

    public void initViewModel() {
        setIsLoading(true);
        addDisposable(lookUpDao.fetchAllReasons(LookUpType.REASON.getTypeId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onReasonFetch, this::onReasonFetchError));

    }

    private void onReasonFetch(List<LookUpEntity> list) {
        setIsLoading(false);
        isReasonAvailable.set(list.size() != 0);
        recyclerAdapter.updateAdapter(list);
    }

    public void onSearchReasonTextChanged(CharSequence sequence) {
        recyclerAdapter.getFilter().filter(sequence);
    }

    private void onReasonFetchError(Throwable throwable) {
        setIsLoading(false);
        Timber.e(throwable);
        Toast.makeText(getApplication(), "No Reasons Present", Toast.LENGTH_SHORT).show();
    }
}
