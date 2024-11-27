package com.sisindia.ai.android.features.taskcheck;

import com.sisindia.ai.android.room.entities.CheckedSiteCheckListEntity;
import com.sisindia.ai.android.uimodels.CheckedPostModel;

public interface DayCheckViewListeners {

    void onCheckedPostClick(CheckedPostModel item);

    void onSiteCheckListItemClick(CheckedSiteCheckListEntity item);
}
