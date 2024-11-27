package  com.sisindia.ai.android.features.dynamictask.models

/**
 * Created by Ashu_Rajput on 6/5/2021.
 */
data class DynamicSpinnerMO(
    var controllerId: String? = null,
    var controllerName: String? = null,
    var isMandatory: Boolean = false,
    val label: String? = null,
    var spinnerList: ArrayList<String> = arrayListOf(),
    var selectedSpinnerValue: String? = null)