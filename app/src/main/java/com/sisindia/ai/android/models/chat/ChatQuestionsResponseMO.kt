package com.sisindia.ai.android.models.chat

import com.google.gson.annotations.SerializedName
import com.sisindia.ai.android.base.BaseNetworkResponse

/**
 * Created by Ashu Rajput on 1/21/2021.
 */
data class ChatQuestionsResponseMO(
    @SerializedName("data")
    val data: ChatDataMO? = null) : BaseNetworkResponse()

data class ChatDataMO(
    @SerializedName("startFrom")
    val startFrom: Int,
    @SerializedName("data")
    val questionsList: ArrayList<ChatQuestionsDataMO>)

data class ChatQuestionsDataMO(
    @SerializedName("id")
    val id: Int,
    @SerializedName("questionTypeId")
    val questionTypeId: Int,
    @SerializedName("question")
    val question: String,
    @SerializedName("isMandatory")
    val isMandatory: Boolean,
    @SerializedName("nextQuetion")
    val nextQuestion: Int,
    @SerializedName("responseId")
    val responseId: Int,
    @SerializedName("child")
    val quesChild: List<QuestionsChildMO>,

    var showWidget: Boolean = true,

    var isListData: Boolean = false,

    var isRatingData: Boolean = false,

    var isFirstNode: Boolean = false)

data class QuestionsChildMO(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("nextQuetion")
    val nextQuestion: Int? = null,
    @SerializedName("responseLabel")
    var responseLabel: String? = null)