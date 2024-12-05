package  com.sisindia.ai.android.features.dynamictask.models

/**
 * Created by Ashu_Rajput on 12/04/2024.
 */
data class DynamicRadioGroupMO(
    var controllerId: String? = null,
    var controllerName: String? = null,
    var isMandatory: Boolean = false,
    val label: String? = null,
    var selectedRadioPosition: Int = 0,
    var radioButtonList: ArrayList<String>? = null,
    var selectedRadioValue: String? = null)