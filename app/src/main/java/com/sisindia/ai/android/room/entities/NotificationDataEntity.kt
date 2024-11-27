package com.sisindia.ai.android.room.entities

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import org.parceler.Parcel

/**
 * Created by Ashu Rajput on 1/7/2021.
 */
@Parcel
@Entity
data class NotificationDataEntity(

    @PrimaryKey(autoGenerate = true)
    var ids: Int = 0,

    @Ignore
    @SerializedName("id")
    var notificationId: String = "1",

    @SerializedName("mode")
    var mode: String? = null,

    @SerializedName("ishtml")
    var html: String? = null,

    @SerializedName("priority")
    var priority: String? = null,

    @SerializedName("payload")
    var payload: String? = null,

    @SerializedName("message")
    var message: String? = null,

    @SerializedName("moduleName")
    var moduleName: String? = null,

    @SerializedName("jsonData")
    var jsonData: String? = null,

    @SerializedName("localsqlquery")
    var localSqlQuery: String? = null,

    @SerializedName("callbackurl")
    var callBackUrl: String = "",

    var isSynced: Boolean = false)