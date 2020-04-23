package com.napptilians.domain.models.push

data class ChatSenderModel(
    val data: ChatNotificationModel,
    val to: String
)
