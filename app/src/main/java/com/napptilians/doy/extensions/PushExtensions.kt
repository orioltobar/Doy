package com.napptilians.doy.extensions

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.SystemClock
import androidx.core.app.NotificationCompat
import com.napptilians.doy.MainActivity

private const val CHANNEL_DEFAULT_ID = "Default"
private const val CHANNEL_DEFAULT_NAME = "Default"

fun Context.createNotification(
    icon: Int,
    title: String,
    body: String,
    channelId: String = CHANNEL_DEFAULT_ID,
    channelName: String = CHANNEL_DEFAULT_NAME
) {
    // Generate empty intent that should be launched when notification is clicked
    val intent = Intent(applicationContext, MainActivity::class.java).apply { addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) }
    val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_ONE_SHOT)

    // Generate notification
    val notification = NotificationCompat.Builder(applicationContext, channelId)
        .setSmallIcon(icon)
        .setContentTitle(title)
        .setContentText(body)
        .setStyle(NotificationCompat.BigTextStyle().bigText(body))
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
        .build()

    // Notify the system about a new notification
    showNotification(notification, channelId, channelName)
}

fun Context.showNotification(
    notification: Notification,
    channelId: String,
    channelName: String,
    notificationId: Int = SystemClock.uptimeMillis().toInt()
) {
    val notificationManager =
        getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager?
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager?.createNotificationChannel(channel)
    }
    notificationManager?.notify(notificationId, notification)
}
