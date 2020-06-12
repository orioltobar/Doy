package com.napptilians.diskdatasource.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chatMessages")
data class ChatMessageDbModel(
    val chatId: String = "",
    val message: String = "",
    val senderName: String = "",
    val senderId: String = "",
    @PrimaryKey
    val timeStamp: Long,
    val read: Boolean
)