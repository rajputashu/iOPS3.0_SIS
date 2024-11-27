package com.sisindia.ai.android.features.addtask;

import com.sisindia.ai.android.room.entities.LookUpEntity;
import com.sisindia.ai.android.room.entities.SiteEntity;

public interface AddTaskViewListeners {

    void onSiteSelected(SiteEntity item);

    void onReasonSelected(LookUpEntity item);

    void onSubTaskTypeSelected(LookUpEntity item);

}
