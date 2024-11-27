package com.sisindia.ai.android.uimodels.tasks

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by Ashu Rajput on 4/23/2020.
 */
@Parcelize
data class OtherTaskDetailMO(
    var reasonName: String? = null,
    var reasonLookUpIdentifier: Int = 0, var subTaskId: Int, var otherTaskName: String) : Parcelable