package com.napptilians.domain.models.chat

data class MessageModel(
    val text: String = "",
    val timeStamp: Long = System.currentTimeMillis()
)