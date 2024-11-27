package com.sisindia.ai.android.features.addtask;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableInt;

import com.sisindia.ai.android.base.IopsBaseViewModel;
import com.sisindia.ai.android.constants.NavigationConstants;
import com.sisindia.ai.android.room.dao.SiteDao;
import com.sisindia.ai.android.room.entities.BarrackEntity;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class SelectBarrackViewModel extends IopsBaseViewModel {

//    public ObservableField<SiteEntity> selectedSite = new ObservableField<>();

    public BarrackListRecyclerAdapter adapter = new BarrackListRecyclerAdapter();

    public ObservableInt noBarracks = new ObservableInt(View.GONE);

    public SelectBarrackViewListeners listeners = item -> {
        message.what = NavigationConstants.ON_CREATE_TASK_BARRACK_SELECTED;
        message.obj = item;
        liveData.postValue(message);
    };

    @Inject
    public SiteDao siteDao;

    @Inject
    public SelectBarrackViewModel(@NonNull Application application) {
        super(application);
    }


    public void onSearchSiteTextChanged(CharSequence sequence) {
        adapter.getFilter().filter(sequence);
    }

    public void initViewModel() {
        /*SiteEntity site = selectedSite.get();
        if (site != null && site.id != 0) {
            addDisposable(siteDao.fetchAllBarrackMappedWithSite(site.id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onBarrackListFetch, Timber::e));
        }*/
        //fetchAllBarracks
        addDisposable(siteDao.fetchAllBarracks()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onBarrackListFetch, Timber::e));
    }

    private void onBarrackListFetch(List<BarrackEntity> list) {
        adapter.updateAdapter(list);
        noBarracks.set(list.size() == 0 ? View.VISIBLE : View.GONE);
    }
}
