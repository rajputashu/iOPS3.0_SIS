package com.sisindia.ai.android.uimodels.barracks

import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.android.parcel.Parcelize


/**
 * Created by Ashu Rajput on 4/23/2020.
 */
@Parcelize
data class MetLandlordMO(
    @Expose
    var landlordName: String? = null,
    @Expose
    var landlordMobile: String? = null,
    @Expose
    var isPaymentComing: Boolean? = true,
    @Expose
    var paymentRemarks: String? = null,
    @Expose
    var landlordUri: String? = null,
    @Expose
    var custodianUri: String? = null) : Parcelable