package com.sisindia.ai.android.features.issues

import com.sisindia.ai.android.room.entities.SiteEntity

interface SiteListListeners {
    fun onSiteSelected(item: SiteEntity)
}