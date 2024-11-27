package  com.sisindia.ai.android.features.dynamictask.models

/**
 * Created by Ashu_Rajput on 6/28/2021.
 */
data class DynamicStaticSpinnerMO(
    var controllerId: String? = null,
    var controllerName: String? = null,
    var isMandatory: Boolean = false,
    val label: String? = null,
    var selectedSpinnerPosition: Int = 0,
    var spinnerList: ArrayList<String>? = null,
    var selectedSpinnerValue: String? = null)