package com.sisindia.ai.android.features.units.sheet

import android.app.Application
import android.view.View
import android.widget.Toast
import androidx.databinding.ObservableField
import com.droidcommons.preference.Prefs
import com.sisindia.ai.android.base.IopsBaseViewModel
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.constants.PrefConstants
import com.sisindia.ai.android.features.units.addedit.EquipmentSpinnerListener
import com.sisindia.ai.android.features.units.addedit.EquipmentsMO
import javax.inject.Inject

/**
 * Created by Ashu Rajput on 3/25/2020.
 */
class AddEquipmentViewModel @Inject constructor(val app: Application) : IopsBaseViewModel(app) {

    var equipmentCount = ObservableField("")
    var spinnerEquipmentType = ObservableField("Select Equipment Type")
    var equipmentProvideBy = ObservableField("Select Provided By")

    val equipmentListener = object : EquipmentSpinnerListener {
        override fun onEquipmentTypeSelected(equipmentType: String) {
            spinnerEquipmentType.set(equipmentType)
        }

        override fun onProvidedBySelected(provided: String) {
            equipmentProvideBy.set(provided)
        }
    }

    val providedByList = ObservableField(arrayListOf("Select Provided By",
        "SIS", "Client"))

    val equipmentTypeList = ObservableField(arrayListOf("Select Equipment Type",
        "Vehicle(4 Wheeler)",
        "Motor Cycle",
        "Metal Detector",
        "Cycle",
        "Wireless Handheld",
        "Wireless On Vehicle",
        "Wireless base station",
        "Cleaning equipment"))

    fun initUI() {
        when (Prefs.getInt(PrefConstants.COMPANY_ID, 1)) {
            1 -> providedByList.set(arrayListOf("Select Provided By",
                "SIS", "Client"))
            2 -> providedByList.set(arrayListOf("Select Provided By",
                "UNIQ", "Client"))
            3 -> providedByList.set(arrayListOf("Select Provided By",
                "SLV", "Client"))
        }
    }

    fun onSaveBtnClick(view: View) {
        when {
            spinnerEquipmentType.get().toString().equals("Select Equipment Type",
                ignoreCase = true) -> Toast.makeText(app, "Please select equipment type",
                Toast.LENGTH_LONG).show()
            equipmentProvideBy.get().toString().equals("Select Provided By",
                ignoreCase = true) -> Toast.makeText(app, "Please select provided by",
                Toast.LENGTH_LONG).show()
            equipmentCount.get().toString().isEmpty() -> Toast.makeText(app,
                "Please enter equipment count", Toast.LENGTH_LONG).show()
            else -> {
                message.what = NavigationConstants.EQUIPMENT_SAVE
                message.obj = EquipmentsMO(spinnerEquipmentType.get().toString(),
                    equipmentCount.get().toString().toInt(),
                    equipmentProvideBy.get().toString())
                liveData.postValue(message)
            }
        }
    }
}