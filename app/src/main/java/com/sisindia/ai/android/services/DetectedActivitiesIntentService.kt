package com.sisindia.ai.android.services

import android.app.IntentService
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.location.ActivityRecognitionResult
import com.google.android.gms.location.DetectedActivity
import com.sisindia.ai.android.constants.IntentConstants
import timber.log.Timber

/**
 * Created by Ashu Rajput on 2/2/2021.
 */
class DetectedActivitiesIntentService : IntentService(TAG) {

    override fun onHandleIntent(intent: Intent?) {
        val result = ActivityRecognitionResult.extractResult(intent!!)
        if (result != null) {
            val detectedActivities = result.probableActivities as ArrayList<*>
            for (activity in detectedActivities) {
                if ((activity as DetectedActivity).confidence > 70)
                    handleUserActivity(activity.type, activity.confidence)
            }
        }
    }

    private fun handleUserActivity(type: Int, confidence: Int) {
        var label = "DEFAULT"
        var activitySymbol = ""

        when (type) {
            DetectedActivity.IN_VEHICLE -> {
                label = "You are in Vehicle"
                activitySymbol = "VEHICLE"
            }
            DetectedActivity.ON_BICYCLE -> {
                label = "You are on Bicycle"
                activitySymbol = "ON_BICYCLE"
            }
            DetectedActivity.ON_FOOT -> {
                label = "You are on Foot"
                activitySymbol = "ON_FOOT"
            }
            DetectedActivity.RUNNING -> {
                label = "You are Running"
                activitySymbol = "RUNNING"
            }
            DetectedActivity.STILL -> {
                label = "You are Still"
                activitySymbol = "STILL"
            }
            DetectedActivity.TILTING -> {
                label = "Your phone is Tilted"
                activitySymbol = "TILTING"
            }
            DetectedActivity.WALKING -> {
                label = "You are Walking"
                activitySymbol = "WALKING"
            }
            DetectedActivity.UNKNOWN -> {
                label = "Unkown Activity"
                activitySymbol = "UNKNOWN"
            }
        }

        Timber.e("User activity detected as : $label, Confidence: $confidence")
        val intent = Intent(IntentConstants.BROADCAST_DETECTED_ACTIVITY)
        intent.putExtra("ACTIVITY_SYMBOL", activitySymbol)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    companion object {
        private val TAG = DetectedActivitiesIntentService::class.java.simpleName
    }
}