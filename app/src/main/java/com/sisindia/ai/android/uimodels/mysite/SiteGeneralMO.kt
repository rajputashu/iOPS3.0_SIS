package com.sisindia.ai.android.uimodels.mysite

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Ashu Rajput on 4/27/2020.
 */
@Parcelize
data class SiteGeneralMO(
    var id: Int = 0,
    var siteName: String? = null,
    var siteGeoCode: String? = null,
    var siteAddress: String? = null,
    var siteCode: String? = null,
    var billSubmission: String? = null,
    var billCollection: String? = null,
    var wage: String? = null) : Parcelable