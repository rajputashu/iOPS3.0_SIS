package com.sisindia.ai.android.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sisindia.ai.android.models.GeoPointItem

/**
 * Created by Ashu Rajput on 7/9/2020.
 */
class GeoLocationConverter {
    @TypeConverter
    fun aiGeoLocationToJson(aiGeoPointsItem: GeoPointItem): String {
        val type = object : TypeToken<GeoPointItem>() {}.type
        return Gson().toJson(aiGeoPointsItem, type)
    }

    @TypeConverter
    fun aiGeoLocationToObj(json: String): GeoPointItem {
        val type = object : TypeToken<GeoPointItem>() {}.type
        return Gson().fromJson(json, type)
    }
}