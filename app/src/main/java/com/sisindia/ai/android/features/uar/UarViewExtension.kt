package com.sisindia.ai.android.features.uar

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatSpinner
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.sisindia.ai.android.R
import com.sisindia.ai.android.commons.SpinnersListener
import com.sisindia.ai.android.features.uar.add.PoaActionPointMO
import com.sisindia.ai.android.models.poa.UarEmployeeDataMO
import com.sisindia.ai.android.room.entities.LookUpEntity

@BindingAdapter(value = ["empList", "spinnerListener", "typeId"])
fun AppCompatSpinner.bindUarSpinner0(empList: List<UarEmployeeDataMO>?,
                                    listener: SpinnersListener, typeId: Int) {
    if (!empList.isNullOrEmpty()) {

        val itemList = empList.map {
            it.employeeName
        }

        val modeAdapter: ArrayAdapter<String> = ArrayAdapter(context,
            R.layout.support_simple_spinner_dropdown_item, itemList)
        adapter = modeAdapter
        onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, v: View?, pos: Int, id: Long) {
                listener.onSpinnerItemAndTypeSelected(pos, typeId)
            }
        }
    }
}

@BindingAdapter(value = ["spinnerItemList", "spinnerListener", "typeId"])
fun AppCompatSpinner.bindUarSpinner(lookupList: List<LookUpEntity>?, listener: SpinnersListener,
                                    typeId: Int) {
    if (!lookupList.isNullOrEmpty()) {

        val itemList = lookupList.map {
            it.displayName
        }

        val modeAdapter: ArrayAdapter<String> = ArrayAdapter(context,
            R.layout.support_simple_spinner_dropdown_item, itemList)
        adapter = modeAdapter
        onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, v: View?, pos: Int, id: Long) {
                listener.onSpinnerItemAndTypeSelected(pos, typeId)
            }
        }
    }
}

@BindingAdapter(value = ["spinnerItemList", "spinnerListener", "typeId"])
fun AppCompatSpinner.bindUarSpinner2(lookupList: List<PoaActionPointMO>?, listener: SpinnersListener,
                                     typeId: Int) {
    if (!lookupList.isNullOrEmpty()) {

        val itemList = lookupList.map {
            it.actionPlanName
        }

        val modeAdapter: ArrayAdapter<String> = ArrayAdapter(context,
            R.layout.support_simple_spinner_dropdown_item, itemList)
        adapter = modeAdapter
        onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, v: View?, pos: Int, id: Long) {
                listener.onSpinnerItemAndTypeSelected(pos, typeId)
            }
        }
    }
}


@BindingAdapter(value = ["uarStatusCode"])
fun AppCompatTextView.showStatus(uarStatusCode: Int) {

    when (uarStatusCode) {
        0 -> {
            text = resources.getString(R.string.string_open)
            setTextColor(ContextCompat.getColor(context, R.color.red_color))
            setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.light_red_color))
        }
        1 -> {
            text = resources.getString(R.string.string_open)
            setTextColor(ContextCompat.getColor(context, R.color.red_color))
            setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.light_red_color))
        }
        2 -> {
            text = resources.getString(R.string.string_inprogress)
            setTextColor(ContextCompat.getColor(context, R.color.colorYellowGold))
            setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.light_yellow))
        }
        3 -> {
            text = resources.getString(R.string.string_status_closed)
            setTextColor(ContextCompat.getColor(context, R.color.colorGreen))
            setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.light_green))
        }
        4 -> {
            text = resources.getString(R.string.string_completed)
            setTextColor(ContextCompat.getColor(context, R.color.colorGreen))
            setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.light_green))
        }
    }
}