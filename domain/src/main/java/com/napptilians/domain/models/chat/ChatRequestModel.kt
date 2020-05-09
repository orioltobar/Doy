package com.napptilians.domain.models.chat

import java.io.Serializable

data class ChatRequestModel(
    val currentUserId: Long,
    val serviceId: Long,
    val senderName: String,
    val serviceName: String
): Serializable