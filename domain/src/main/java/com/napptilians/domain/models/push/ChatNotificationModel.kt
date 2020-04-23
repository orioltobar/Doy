package com.napptilians.domain.models.push

data class ChatNotificationModel(
    val user: String,
    val icon: Int,
    val title: String,
    val body: String,
    val senderUid: String
)
