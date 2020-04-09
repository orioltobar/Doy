package com.napptilians.doy.push

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.napptilians.commons.AppDispatchers
import com.napptilians.domain.models.device.DeviceModel
import com.napptilians.domain.usecases.GetDeviceInfoUseCase
import com.napptilians.domain.usecases.SetDeviceInfoUseCase
import dagger.android.AndroidInjection
import kotlinx.coroutines.*
import javax.inject.Inject

class PushNotificationService : FirebaseMessagingService() {

    @Inject
    lateinit var appDispatchers: AppDispatchers
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
    }

    companion object {
        private val TAG = PushNotificationService::class.java.simpleName
    }
}
