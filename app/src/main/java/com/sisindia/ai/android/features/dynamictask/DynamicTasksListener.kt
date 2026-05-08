package com.sisindia.ai.android.features.dynamictask

/**
 * Created by Ashu_Rajput on 6/7/2021.
 */
interface DynamicTasksListener {
    fun onClickDateTimePicker(position: Int) {}
    fun onRadioButtonSelected(position: Int, selectedValue: String) {}
    fun onCheckBoxClicked(position: Int, checkedValue: Boolean)
    fun onSpinnerSelected(position: Int, selectedValue: String)
    fun onClickPicture(picturePos: Int)
    fun onAddingAudio(position: Int)
    fun onScanningQR(position: Int)
}