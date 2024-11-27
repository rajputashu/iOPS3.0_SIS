package com.sisindia.ai.android.uimodels.barracks

import com.google.gson.annotations.SerializedName
import com.sisindia.ai.android.models.GeoPointItem

data class BarrackUpdateBodyMO(
    @SerializedName("id")
    var id: Int? = null,

    @SerializedName("name")
    var name: String? = null,

    @SerializedName("barrackCode")
    var barrackCode: String? = null,

    @SerializedName("barrackGeoPoint")
    var geoPoint: GeoPointItem? = null,

    @SerializedName("srNumber")
    var srNumber: String? = null

    /*@SerializedName("SiteId")
    var siteId: Int? = null,*/

    /*@SerializedName("barrackId")
    var barrackId: Int = 0,*/

    /*@SerializedName("barrackName")
    var barrackName: String? = null,*/

    /*@SerializedName("barrackUnitCode")
    var barrackUnitCode: String? = null,*/

    /*@SerializedName("createdDateTime")
    var createdDateTime: String? = null,

    @SerializedName("updatedDateTime")
    var updatedDateTime: String? = null*/)