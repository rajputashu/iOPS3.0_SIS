package  com.sisindia.ai.android.features.dynamictask.models

/**
 * Created by Ashu_Rajput on 6/5/2021.
 */
data class DynamicRadioWithChildMO(
    var controllerId: String? = null,
    var controllerName: String? = null,
    var isMandatory: Boolean = false,
    val radioButtonLabel: String = "",
    var isRadioSelected: Boolean = false,
    var selectedRadioValue: String? = null,
    val dependantController: String? = null,
    val dependantControllerHint: String? = null,
    val dependantSpinnerList: List<String>? = null,
    var selectedSpinnerPosition: Int = 0,
    var enteredValue: String? = null)