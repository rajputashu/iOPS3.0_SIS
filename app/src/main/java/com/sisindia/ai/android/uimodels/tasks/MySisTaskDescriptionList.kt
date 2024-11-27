package com.sisindia.ai.android.uimodels.tasks

import com.google.gson.annotations.SerializedName

/**
 * Created by Ashu Rajput on 03-03-2022
 */

//data class MySisTaskDescriptionList(val descriptionList: List<MySiSTaskDescription>)

data class MySiSTaskDescription(
    @SerializedName("task")
    val task: String? = null,
    @SerializedName("taskdate")
    val taskDate: String? = null,
    @SerializedName("rotadate")
    val rotadate: String? = null,
    @SerializedName("closeby")
    val closeBy: String? = null,
    @SerializedName("sites")
    val sites: Int = 0,
    @SerializedName("sitescompleted")
    val sitesCompleted: Int = 0)