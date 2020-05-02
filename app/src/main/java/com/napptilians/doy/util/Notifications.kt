package com.napptilians.doy.util

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.os.bundleOf
import androidx.navigation.NavDeepLinkBuilder
import com.napptilians.doy.R
import com.napptilians.doy.receiver.AlarmReceiver

object Notifications {

    const val NOTIFICATIONS_CHANNEL_ID = "com.napptilians.doy.notifications"
    const val REQUEST_CODE_KEY = "requestCode"
    const val TITLE_KEY = "title"
    const val SUBTITLE_KEY = "subtitle"
    const val SERVICE_ID_KEY = "serviceId"

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

    fun preparePendingIntent(
        context: Context,
        requestCode: Int,
        title: String?,
        subtitle: String?,
        serviceId: Long?
    ): PendingIntent {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra(REQUEST_CODE_KEY, requestCode)
            putExtra(TITLE_KEY, title)
            putExtra(SUBTITLE_KEY, subtitle)
            putExtra(SERVICE_ID_KEY, serviceId)
        }
        return PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    fun launchNotification(context: Context, requestCode: Int, title: String, subtitle: String, serviceId: Long) {
        val pendingIntent = NavDeepLinkBuilder(context)
            .setGraph(R.navigation.nav_graph)
            .setDestination(R.id.chatListFragment)
            .setArguments(bundleOf(SERVICE_ID_KEY to serviceId))
            .createPendingIntent()

        val notification = NotificationCompat.Builder(context, NOTIFICATIONS_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_doy_logo_green)
            .setContentTitle(title)
            .setStyle(NotificationCompat.BigTextStyle().bigText(subtitle))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            // Set the intent that will fire when the user taps the notification
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setPriority(Notification.VISIBILITY_PRIVATE)
            .build()
        with(NotificationManagerCompat.from(context)) {
            // requestCode is a unique int for each notification that you must define
            notify(requestCode, notification)
        }
    }

    fun cancelNotification(context: Context, requestCode: Int) {
        with(NotificationManagerCompat.from(context)) {
            cancel(requestCode)
        }
    }
}