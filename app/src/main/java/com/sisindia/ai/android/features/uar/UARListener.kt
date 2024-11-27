package com.sisindia.ai.android.features.uar


/**
 * Created by Ashu Rajput on 4/3/2020.
 */
interface UARListener {
    fun onUnitAtRiskSelected(selectedUAR: Any)
    fun onPendingPOASelected(selectedPOA: Any)
}