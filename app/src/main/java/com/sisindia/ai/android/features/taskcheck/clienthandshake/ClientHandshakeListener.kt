package com.sisindia.ai.android.features.taskcheck.clienthandshake

import com.sisindia.ai.android.room.entities.CustomerContactEntity

/**
 * Created by Ashu Rajput on 4/9/2020.
 */
interface ClientHandshakeListener {
    fun onContinueAddFeedbackClicked()
    fun onFinalSummaryHandshakeView(optedQuestions: String)
    fun onCompleteClientHandshake()
    fun onClientSelected(client: CustomerContactEntity)
}