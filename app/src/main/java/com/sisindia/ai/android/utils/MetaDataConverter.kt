package com.sisindia.ai.android.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sisindia.ai.android.models.GeoPointItem
import com.sisindia.ai.android.uimodels.attachments.AttachmentMetadataMO

/**
 * Created by Ashu Rajput on 5/11/2020.
 */
class MetaDataConverter {
    @TypeConverter
    fun toJson(attachmentMetadataObject: AttachmentMetadataMO): String {
        val type = object : TypeToken<AttachmentMetadataMO>() {}.type
        return Gson().toJson(attachmentMetadataObject, type)
    }

    @TypeConverter
    fun toAttachmentMetadataObj(json: String): AttachmentMetadataMO {
        val type = object : TypeToken<AttachmentMetadataMO>() {}.type
        return Gson().fromJson(json, type)
    }
}