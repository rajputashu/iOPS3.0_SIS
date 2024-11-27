package com.sisindia.ai.android.features.taskcheck.postcheck.postlist;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;

import com.droidcommons.preference.Prefs;
import com.sisindia.ai.android.base.IopsBaseViewModel;
import com.sisindia.ai.android.constants.NavigationConstants;
import com.sisindia.ai.android.constants.PrefConstants;
import com.sisindia.ai.android.features.taskcheck.postcheck.PostCheckViewListeners;
import com.sisindia.ai.android.room.dao.SitePostDao;
import com.sisindia.ai.android.room.dao.TaskDao;
import com.sisindia.ai.android.room.entities.CheckedPostCheckListEntity;
import com.sisindia.ai.android.room.entities.CheckedPostEntity;
import com.sisindia.ai.android.uimodels.CheckedGuardItemModel;
import com.sisindia.ai.android.uimodels.SitePostModel;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SitePostListViewModel extends IopsBaseViewModel {

    public SitePostListAdapter postCheckAdapter = new SitePostListAdapter();


    public PostCheckViewListeners postCheckViewListeners = new PostCheckViewListeners() {

        @Override
        public void onSitePostItemClick(SitePostModel item) {
            Prefs.putInt(PrefConstants.CURRENT_POST, item.id);
            message.what = NavigationConstants.ON_SITE_POST_CLICK;
            liveData.postValue(message);
        }

        @Override
        public void onCheckedGuardClick(CheckedGuardItemModel item) {

        }

        @Override
        public void onPostCheckListItemClick(CheckedPostCheckListEntity item) {

        }

        @Override
        public void onCheckedRegisterClick() {

        }
    };


    @Inject
    TaskDao taskDao;

    @Inject
    SitePostDao sitePostDao;

    @Inject
    public SitePostListViewModel(@NonNull Application application) {
        super(application);
    }


    public void initViewModel() {
        int taskId = Prefs.getInt(PrefConstants.CURRENT_TASK);
        int siteId = Prefs.getInt(PrefConstants.CURRENT_SITE);
        int status = CheckedPostEntity.CheckedPostStatus.COMPLETED.getPostStatus();
        if (siteId != 0) {
            addDisposable(sitePostDao.fetchPendingPostsForCheckIn(siteId, taskId, status)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onAllPostFetch, this::onAllPostFetchError));
        }
    }


    private void onAllPostFetch(List<SitePostModel> posts) {
        postCheckAdapter.clearAndSetItems(posts);
    }

    private void onAllPostFetchError(Throwable throwable) {
        showToast("no post present with the site");
    }


    public void btnScanPostClick(View view) {
        message.what = NavigationConstants.OPEN_LIVE_QR_SCANNER_SCREEN;
        liveData.postValue(message);
    }
}
