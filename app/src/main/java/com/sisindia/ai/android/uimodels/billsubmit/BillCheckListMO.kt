package com.sisindia.ai.android.uimodels.billsubmit

import android.os.Parcelable
import androidx.room.Ignore
import kotlinx.android.parcel.Parcelize

/**
 * Created by Ashu Rajput on 4/11/2020.
 */
@Parcelize
data class BillCheckListMO(
    var lookUpName: String? = null,
    var lookUpIdentifier: String? = null,
    @Ignore
    var isOptionOpted: Boolean = false) : Parcelable