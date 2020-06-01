package com.napptilians.doy.util

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.os.bundleOf
import androidx.navigation.NavDeepLinkBuilder
import com.napptilians.domain.models.chat.ChatRequestModel
import com.napptilians.doy.R
import com.napptilians.doy.receiver.AlarmReceiver
import org.apache.commons.lang3.SerializationUtils

object Notifications {

    const val NOTIFICATIONS_CHANNEL_ID = "com.napptilians.doy.notifications"
    const val REQUEST_CODE_KEY = "requestCode"
    const val TITLE_KEY = "title"
    const val SUBTITLE_KEY = "subtitle"
    const val CHAT_REQUEST_KEY = "chatRequest"
    const val MINUTES = 5

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
        chatRequestModel: ChatRequestModel?
    ): PendingIntent {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra(REQUEST_CODE_KEY, requestCode)
            putExtra(TITLE_KEY, title)
            putExtra(SUBTITLE_KEY, subtitle)
            putExtra(CHAT_REQUEST_KEY, SerializationUtils.serialize(chatRequestModel))
        }
        return PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    fun launchNotification(
        context: Context,
        extras: Bundle?
    ) {
        val chatRequest =
            SerializationUtils.deserialize<ChatRequestModel>(extras?.getByteArray(CHAT_REQUEST_KEY))
        val pendingIntent = NavDeepLinkBuilder(context)
            .setGraph(R.navigation.nav_graph)
            .setDestination(R.id.chatFragment)
            .setArguments(bundleOf(CHAT_REQUEST_KEY to chatRequest))
            .createPendingIntent()

        val requestCode = extras?.getInt(REQUEST_CODE_KEY) ?: 0
        val title = extras?.getString(TITLE_KEY) ?: ""
        val subtitle = extras?.getString(SUBTITLE_KEY) ?: ""
        val notification = NotificationCompat.Builder(context, NOTIFICATIONS_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_doy_logo_green)
            .setContentTitle(title)
            .setStyle(NotificationCompat.BigTextStyle().bigText(subtitle))
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
}