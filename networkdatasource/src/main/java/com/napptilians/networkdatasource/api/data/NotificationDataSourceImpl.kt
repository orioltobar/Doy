package com.napptilians.networkdatasource.api.data

import com.napptilians.commons.Response
import com.napptilians.commons.error.ErrorModel
import com.napptilians.data.datasources.NotificationDataSource
import com.napptilians.domain.models.push.ChatSenderModel
import com.napptilians.networkdatasource.utils.safeApiCall
import javax.inject.Inject

class NotificationDataSourceImpl @Inject constructor(private val firebaseService: FirebaseService) :
    NotificationDataSource {

    override suspend fun sendNotification(notification: ChatSenderModel): Response<Unit, ErrorModel> {
        return safeApiCall {
            firebaseService.sendNotification(notification)
        }
    }
}
