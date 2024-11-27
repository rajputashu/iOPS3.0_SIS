package com.sisindia.ai.android.features.disband

import androidx.room.Ignore

/**
 * Created by Ashu Rajput on 30-03-2023
 */
data class DisbandmentSitesMO(
    var siteId: Int = 0,
    var siteName: String? = null,
    var siteCode: String? = null,
//    val siteAddress :String?=null,
    var operationalSite: Int = 0,

    @Ignore
    var isBoxChecked: Boolean = false)
