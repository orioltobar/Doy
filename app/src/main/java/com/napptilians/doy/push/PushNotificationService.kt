package com.napptilians.doy.push

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.napptilians.commons.AppDispatchers
import com.napptilians.domain.models.device.DeviceModel
import com.napptilians.domain.usecases.GetDeviceInfoUseCase
import com.napptilians.domain.usecases.SetDeviceInfoUseCase
import com.napptilians.doy.extensions.showNotification
import dagger.android.AndroidInjection
import javax.inject.Inject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class PushNotificationService : FirebaseMessagingService() {

    @Inject
    lateinit var appDispatchers: AppDispatchers

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    @Inject
    lateinit var getDeviceInfoUseCase: GetDeviceInfoUseCase

    @Inject
    lateinit var setDeviceInfoUseCase: SetDeviceInfoUseCase

    private val errorHandler: CoroutineExceptionHandler by lazy { CoroutineExceptionHandler { _, e -> e.printStackTrace() } }
    private val scope: CoroutineScope by lazy { CoroutineScope(appDispatchers.main + SupervisorJob() + errorHandler) }

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "New Firebase token retrieved: $token")
        scope.launch {
            setDeviceInfoUseCase.execute(DeviceModel(0, token))
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d(TAG, "Push notification retrieved: $remoteMessage")
        if (remoteMessage.data["sent"]?.equals(firebaseAuth.currentUser?.uid ?: "") == true) {
            showChatNotification(remoteMessage)
        }
    }

    private fun showChatNotification(remoteMessage: RemoteMessage) {
        Log.d(TAG, "Chat notification")
        val notification = remoteMessage.notification
        val user = remoteMessage.data["user"] ?: return
        val icon = remoteMessage.data["icon"] ?: return
        val title = remoteMessage.data["title"] ?: return
        val body = remoteMessage.data["body"] ?: return
        baseContext.showNotification(Integer.parseInt(icon), title, body)
    }

    companion object {
        private val TAG = PushNotificationService::class.java.simpleName
    }
}
