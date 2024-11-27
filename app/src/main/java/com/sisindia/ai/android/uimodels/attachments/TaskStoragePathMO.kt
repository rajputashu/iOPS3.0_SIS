package com.sisindia.ai.android.uimodels.attachments

/**
 * Created by Ashu Rajput on 5/15/2020.
 */
data class TaskStoragePathMO(
    var zoneCode: String? = null,
    var regionCode: String? = null,
    var branchCode: String? = null,
    var barrackCode: String? = null,
    var barrackId: String = "",
    var taskId: String? = null,
    var taskName: String? = null,
    var siteId: String? = null)