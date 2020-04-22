package com.napptilians.domain.models.push

data class ChatSenderModel(
    val notification: ChatNotificationModel,
    val toToken: String
)
