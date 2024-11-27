package  com.sisindia.ai.android.features.dynamictask.models

import android.net.Uri

/**
 * Created by Ashu_Rajput on 6/5/2021.
 */
data class DynamicPictureMO(
    var controllerId: String? = null,
    var controllerName: String? = null,
    var isMandatory: Boolean = false,
    var imageUri: Uri? = null,
    var isImageCaptured: Boolean = false,
    val takePicCount: String = "1")