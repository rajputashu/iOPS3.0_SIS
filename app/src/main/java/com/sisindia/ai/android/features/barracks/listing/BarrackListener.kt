package com.sisindia.ai.android.features.barracks.listing

/**
 * Created by Ashu Rajput on 4/18/2020.
 */
interface BarrackListener {
    fun onBarrackSelected(selectedPosition:Int)
    fun onTagQRSelected(barrackId: Int)
}