package com.sisindia.ai.android.uimodels.handshake

import androidx.room.Ignore
import com.google.gson.annotations.Expose

/**
 * Created by Ashu Rajput on 4/14/2020.
 */
data class RatingQuestionsMO(
    var Id: Int = 0,
    var companyId: Int? = 0,
    var questionTypeId: Int? = 0,
    @Expose
    var question: String? = null,
    var clientHandshakeQuestionId: Int? = 0,
    @Ignore
    var isOpted: Boolean = false)