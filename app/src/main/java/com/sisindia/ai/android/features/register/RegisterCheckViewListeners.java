package com.sisindia.ai.android.features.register;

import com.sisindia.ai.android.room.entities.CheckedRegisterEntity;
import com.sisindia.ai.android.room.entities.RegisterAttachmentEntity;

public interface RegisterCheckViewListeners {

    void onRegisterItemClick(CheckedRegisterEntity item);

    void onRemoveRegisterAttachment(RegisterAttachmentEntity item);
}
