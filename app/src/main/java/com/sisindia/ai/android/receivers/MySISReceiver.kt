package com.sisindia.ai.android.receivers

import android.content.Context
import android.content.Intent
import dagger.android.DaggerBroadcastReceiver
import timber.log.Timber

class MySISReceiver : DaggerBroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        intent!!.extras?.apply {

            if (this.containsKey("MySISTaskID"))
                Timber.e("MySISReceiver : Task Id Done ${this.getInt("MySISTaskID")}")
            else
                Timber.e("MySISReceiver : Nothing in the intent....")

        }
    }
}