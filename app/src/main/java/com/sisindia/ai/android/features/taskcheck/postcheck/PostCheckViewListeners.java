package com.sisindia.ai.android.features.taskcheck.postcheck;

import com.sisindia.ai.android.room.entities.CheckedPostCheckListEntity;
import com.sisindia.ai.android.uimodels.CheckedGuardItemModel;
import com.sisindia.ai.android.uimodels.SitePostModel;

public interface PostCheckViewListeners {

    void onSitePostItemClick(SitePostModel item);

    void onCheckedGuardClick(CheckedGuardItemModel item);

    void onPostCheckListItemClick(CheckedPostCheckListEntity item);

    void onCheckedRegisterClick();
}
