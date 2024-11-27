package com.sisindia.ai.android.models.performance

import com.google.gson.annotations.SerializedName
import com.sisindia.ai.android.base.BaseNetworkResponse

/**
 * Created by Ashu Rajput on 6/3/2020.
 */
data class PerformanceEffortsApiResponseMO(
    @SerializedName("data")
    val data: PerformanceEffortsMO) : BaseNetworkResponse()

data class PerformanceEffortsMO(
    @SerializedName("bsTotalTask")
    val bsTotalTask: Int? = null,

    @SerializedName("bsCompletedTask")
    val bsCompletedTask: Int? = null,

    @SerializedName("bsDue")
    val bsDue: Int? = null,

    @SerializedName("bsOverDue")
    val bsOverDue: Int? = null,

    @SerializedName("monTotalTask")
    val monTotalTask: Int? = null,

    @SerializedName("monCompletedTask")
    val monCompletedTask: Int? = null,

    @SerializedName("monPending")
    val monPending: Int? = null,

    @SerializedName("collectionDone")
    val collectionDone: Int? = null,

    @SerializedName("outstatnding")
    val outstanding: Int? = null,

    @SerializedName("poaPending")
    val poaPending: Int? = 0,

    @SerializedName("poaClosed")
    val poaClosed: Int? = null,

    @SerializedName("dcTarget")
    val dcTarget: Int? = null,

    @SerializedName("dcDone")
    val dcDone: Int? = null,

    @SerializedName("ncTarget")
    val ncTarget: Int? = null,

    @SerializedName("ncDone")
    val ncDone: Int? = null
)

/*data class PerformanceEffortsMO(

    @SerializedName("billSubmissionSummary")
    val billSubmissionSummary: BillSubmissionSummary,

    @SerializedName("monInputSummary")
    val monInputSummary: MonInputSummary,

    @SerializedName("billCollectionSummary")
    val billCollectionSummary: BillCollectionSummary,

    @SerializedName("poASummary")
    val poASummary: PoASummary,

    @SerializedName("dayCheckSummary")
    val dayCheckSummary: DayCheckSummary,

    @SerializedName("nightCheckSummary")
    val nightCheckSummary: NightCheckSummary) {

    constructor() : this(BillSubmissionSummary(),
        MonInputSummary(),
        BillCollectionSummary(),
        PoASummary(),
        DayCheckSummary(),
        NightCheckSummary())
}*/

/*
data class BillSubmissionSummary(

    @SerializedName("complete")
    val complete: Int?,

    @SerializedName("total")
    val total: Int?,

    @SerializedName("due")
    val due: Int?,

    @SerializedName("overdue")
    val overdue: Int?
) {
    constructor() : this(0, 0, 0, 0)
}

data class MonInputSummary(

    @SerializedName("total")
    val total: Int?,

    @SerializedName("complete")
    val complete: Int?,

    @SerializedName("pending")
    val pending: Int?
) {
    constructor() : this(0, 0, 0)
}


data class BillCollectionSummary(

    @SerializedName("collectionDone")
    val collectionDone: Int?,

    @SerializedName("outstatnding")
    val outstatnding: Int?
) {
    constructor() : this(0, 0)
}


data class PoASummary(

    @SerializedName("poAsClosed")
    val poAsClosed: Int?,

    @SerializedName("poAsPending")
    val poAsPending: Int?
) {
    constructor() : this(0, 0)
}


data class DayCheckSummary(

    @SerializedName("target")
    val target: Int?,

    @SerializedName("complete")
    val complete: Int?
) {
    constructor() : this(0, 0)
}


data class NightCheckSummary(

    @SerializedName("target")
    val target: Int?,

    @SerializedName("complete")
    val complete: Int?
) {
    constructor() : this(0, 0)
}
*/
