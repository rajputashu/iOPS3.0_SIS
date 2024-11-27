package com.sisindia.ai.android.features.addgrievances

import com.sisindia.ai.android.models.GrievanceModel

interface GrievanceItemListener {
    fun onGrievanceItemClick(item: GrievanceModel)
}