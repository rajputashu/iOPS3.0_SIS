package com.sisindia.ai.android.uimodels.barracks

import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.android.parcel.Parcelize

/**
 * Created by Ashu Rajput on 4/23/2020.
 */
@Parcelize
data class BILandlordsMO(
    @Expose
    var isMetLandlord: Boolean? = false,
    @Expose
    var metLandlordMO: MetLandlordMO? = null,
    @Expose
    var isAmenitiesProvided: Boolean? = false,
    @Expose
    var amenitiesRemarks: String? = null,
    @Expose
    var landlordType: String? = null) : Parcelable

