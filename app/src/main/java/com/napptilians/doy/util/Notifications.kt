package com.napptilians.doy.util

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.napptilians.doy.MainActivity
import com.napptilians.doy.R
import com.napptilians.doy.receiver.AlarmReceiver

object Notifications {

    const val NOTIFICATIONS_CHANNEL_ID = "com.napptilians.doy.notifications"

    @TargetApi(Build.VERSION_CODES.O)
    fun createChannel(
        context: Context,
        channelId: String,
        channelName: String,
        channelDescription: String,
        importance: Int = NotificationManager.IMPORTANCE_DEFAULT
    ) {
        val channel = NotificationChannel(channelId, channelName, importance).apply {
            description = channelDescription
            shouldShowLights()
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    fun launchNotification(context: Context, title: String, subtitle: String, requestCode: Int) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(context, requestCode, intent, 0)

        val notification = NotificationCompat.Builder(context, NOTIFICATIONS_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_logo_colour_green)
            .setContentTitle(title)
            .setContentText(subtitle)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            // Set the intent that will fire when the user taps the notification
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(requestCode, notification)
        }
    }

    fun preparePendingIntent(
        context: Context,
        title: String?,
        subtitle: String?,
        requestCode: Int
    ): PendingIntent {
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.apply {
            putExtra("requestCode", requestCode)
            putExtra("title", title)
            putExtra("subtitle", subtitle)
        }
        return PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }
}