package com.napptilians.doy.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.napptilians.doy.util.Notifications

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        var requestCode = 0
        var title = ""
        var subtitle = ""
        try {
            val bundle = intent?.extras
            requestCode = bundle?.getInt("requestCode") ?: 0
            title = bundle?.getString("title") ?: ""
            subtitle = bundle?.getString("subtitle") ?: ""
        } catch (e: Exception) {
            Log.e("AlarmReceiver", e.toString())
        }
        context?.let {
            Notifications.launchNotification(context, title, subtitle, requestCode)
        }
    }
}