package com.sisindia.ai.android.features.sync


/**
 * Created by Ashu Rajput on 10/6/2020.
 */
data class SyncOptionsMO(val optionName: String, val optionId: Int, val isDownloadingFromServer: Boolean, var unSyncedCount: Int = 0,
    val isNeedToShowCount: Boolean = false, var dutySyncStatus: String?=null)