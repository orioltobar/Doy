package com.napptilians.doy.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.napptilians.domain.models.service.ServiceModel
import com.napptilians.doy.util.Notifications

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        try {
            val bundle = intent?.extras
            val requestCode = bundle?.getInt(Notifications.REQUEST_CODE_KEY) ?: 0
            val title = bundle?.getString(Notifications.TITLE_KEY) ?: ""
            val subtitle = bundle?.getString(Notifications.SUBTITLE_KEY) ?: ""
            val serviceId = bundle?.getLong(Notifications.SERVICE_ID_KEY) ?: 0L
            context?.let {
                Notifications.launchNotification(context, requestCode, title, subtitle, serviceId)
            }
        } catch (e: Exception) {
            Log.d(TAG, e.toString())
        }
    }

    companion object {
        const val TAG = "AlarmReceiver"
    }
}