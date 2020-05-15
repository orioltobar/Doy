package com.napptilians.doy.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.napptilians.doy.util.Notifications

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        try {
            context?.let {
                Notifications.launchNotification(context, intent?.extras)
            }
        } catch (e: Exception) {
            Log.d(TAG, e.toString())
        }
    }

    companion object {
        private const val TAG = "AlarmReceiver"
    }
}