package com.napptilians.data.datasources

import com.napptilians.commons.Response
import com.napptilians.commons.error.ErrorModel
import com.napptilians.domain.models.push.ChatSenderModel

interface NotificationDataSource {

    suspend fun sendNotification(notification: ChatSenderModel): Response<Unit, ErrorModel>
}