package com.sisindia.ai.android.models.site

import com.google.gson.annotations.SerializedName
import com.sisindia.ai.android.base.BaseNetworkResponse
import com.sisindia.ai.android.room.entities.EmployeeSiteEntity
import com.sisindia.ai.android.room.entities.SiteEntity
import com.sisindia.ai.android.room.entities.SitePostEntity
import com.sisindia.ai.android.room.entities.SiteStrengthEntity

/**
 * Created by Ashu Rajput on 8/18/2020.
 */
data class MySitesResponseMO(
    @SerializedName("data")
    //    var mySitesData: List<SiteEntity>? = null
    var mySitesData: MySitesDataResponse? = null) : BaseNetworkResponse()

data class MySitesDataResponse(
    @SerializedName("siteCheckLists")
    var mySitesList: List<SiteEntity>? = null,
    @SerializedName("siteStrengths")
    var siteStrengthList: List<SiteStrengthEntity>? = null,
    @SerializedName("sitePosts")
    var sitePostList: List<SitePostEntity>? = null)

data class EmployeeSitesResponseMO(
    @SerializedName("data")
    var employees: List<EmployeeSiteEntity>) : BaseNetworkResponse()