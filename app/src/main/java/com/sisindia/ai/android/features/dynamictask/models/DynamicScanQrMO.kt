package  com.sisindia.ai.android.features.dynamictask.models

/**
 * Created by Ashu_Rajput on 6/7/2021.
 */
data class DynamicScanQrMO(
    var controllerId: String? = null,
    var controllerName: String? = null,
    var isMandatory: Boolean = false,
    val label: String? = null,
    var scannedQrCode: String? = null)