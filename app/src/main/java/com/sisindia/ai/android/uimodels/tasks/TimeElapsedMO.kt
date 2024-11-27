package com.sisindia.ai.android.uimodels.tasks

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Ashu Rajput on 11/10/2020.
 */
@Parcelize
data class TimeElapsedMO(val timeElapsed: Long, val lastElapsedTime: Long) :Parcelable