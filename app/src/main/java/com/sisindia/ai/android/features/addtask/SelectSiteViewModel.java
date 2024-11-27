package com.sisindia.ai.android.features.addtask;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import com.sisindia.ai.android.base.IopsBaseViewModel;
import com.sisindia.ai.android.constants.NavigationConstants;
import com.sisindia.ai.android.room.dao.SiteDao;
import com.sisindia.ai.android.room.entities.LookUpEntity;
import com.sisindia.ai.android.room.entities.SiteEntity;
import com.sisindia.ai.android.uimodels.TaskTypeModel;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class SelectSiteViewModel extends IopsBaseViewModel {

    public SiteListRecyclerAdapter siteListAdapter = new SiteListRecyclerAdapter();
    public ObservableBoolean isSitesAvailable = new ObservableBoolean(false);
    public ObservableField<TaskTypeModel> selectedTaskType = new ObservableField<>();

    public AddTaskViewListeners viewListeners = new AddTaskViewListeners() {

        @Override
        public void onSiteSelected(SiteEntity item) {
            message.what = NavigationConstants.ON_CREATE_TASK_SITE_SELECTED;
            message.obj = item;
            liveData.postValue(message);
        }

        @Override
        public void onReasonSelected(LookUpEntity item) {

        }

        @Override
        public void onSubTaskTypeSelected(LookUpEntity item) {

        }
    };

    @Inject
    SiteDao siteDao;

    @Inject
    public SelectSiteViewModel(@NonNull Application application) {
        super(application);
    }

    public void initViewModel() {
        setIsLoading(true);
        TaskTypeModel taskType = selectedTaskType.get();
        Single<List<SiteEntity>> list = taskType != null && taskType.id == 3 ?
                siteDao.fetchAllActiveSitesMappedWithBarrack(true) : siteDao.fetchAllActiveSitesForAdHoc(true);

        addDisposable(list
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSiteFetch, this::onSiteFetchError));
    }

    public void onSearchSiteTextChanged(CharSequence sequence) {
        siteListAdapter.getFilter().filter(sequence);
    }

    private void onSiteFetch(List<SiteEntity> list) {
        setIsLoading(false);
        isSitesAvailable.set(list.size() != 0);
        siteListAdapter.updateAdapter(list);
    }

    private void onSiteFetchError(Throwable throwable) {
        setIsLoading(false);
        Timber.e(throwable);
        Toast.makeText(getApplication(), "No Sites Present", Toast.LENGTH_SHORT).show();
    }
}