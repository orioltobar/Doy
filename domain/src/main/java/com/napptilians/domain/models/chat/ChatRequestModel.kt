package com.napptilians.domain.models.chat

import org.threeten.bp.ZonedDateTime
import java.io.Serializable

data class ChatRequestModel(
    val currentUserId: Long,
    val serviceId: Long,
    val senderName: String,
    val serviceName: String,
    val serviceStartDate: ZonedDateTime,
    val serviceDuration: Int
): Serializable