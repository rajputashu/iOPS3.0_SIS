package com.sisindia.ai.android.uimodels.akr

data class AkrUIResult(
    val cardMO: KitBySiteDetailsMO,
    val pendingList: List<KitAssignedDistributedMO>,
    val completedList: List<KitAssignedDistributedMO>
)
