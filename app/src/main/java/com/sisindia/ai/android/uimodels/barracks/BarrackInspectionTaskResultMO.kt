package com.sisindia.ai.android.uimodels.barracks

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Ashu Rajput on 5/24/2020.
 */
data class BarrackInspectionTaskResultMO(
    @Expose
    @SerializedName("Strength")
    var strengthJson: BIStrengthMO? = null,

    @Expose
    @SerializedName("Space")
    var spaceJson: BISpaceMO? = null,

    @Expose
    @SerializedName("Others")
    var othersJson: BIOthersMO? = null,

    @Expose
    @SerializedName("MetLandlord")
    var metLandlordJson: BILandlordsMO? = null)