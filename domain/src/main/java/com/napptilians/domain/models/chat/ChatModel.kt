package com.napptilians.domain.models.chat

data class ChatModel(
    val chatId: String = "",
    val message: String = "",
    val senderName: String = "",
    val senderId: String = "",
    val timeStamp: Long = System.currentTimeMillis(),
    val read: Boolean = false
)