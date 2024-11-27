package com.sisindia.ai.android.features.taskcheck.postcheck.guardcheck;

import com.sisindia.ai.android.room.entities.LookUpEntity;
import com.sisindia.ai.android.uimodels.GuardSuggestionItem;

public interface CheckGuardViewListeners {

    void onGuardNotAvailableStatusChanged(LookUpEntity item);
}
