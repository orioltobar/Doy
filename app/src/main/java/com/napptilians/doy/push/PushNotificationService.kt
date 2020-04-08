package com.napptilians.doy.push

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class PushNotificationService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // TODO: Save retrieved token
        Log.d(TAG, "New token retrieved: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d(TAG, "Push notification retrieved: $remoteMessage")
    }

    companion object {
        private val TAG = PushNotificationService::class.java.simpleName
    }
}
