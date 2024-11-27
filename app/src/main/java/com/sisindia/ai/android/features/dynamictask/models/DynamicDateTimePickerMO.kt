package  com.sisindia.ai.android.features.dynamictask.models

/**
 * Created by Ashu_Rajput on 12/02/2024.
 */
data class DynamicDateTimePickerMO(
    var controllerId: String? = null,
    var controllerName: String? = null,
    var isMandatory: Boolean = false,
    val label: String? = null,
    var selectedDateTime: String? = null)