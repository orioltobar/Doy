package com.napptilians.domain.models.chat

import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import java.util.*

/**
 * Defines the model to fill the data of a chat view holder. [id] is the service id,
 * [name] defines the name of the chat, or the event title.
 * [lastSenderName], [lastMessage] defines the last data from the chat and
 * [lastMessageTime] is the hour of the last message. [lastMessageTime] time should be always
 * formatted. e.g.: "1m", "5h", "01/01/1973". [status] returns if the chat is from a past event
 * or upcoming.
 */
data class ChatListItemModel(
    val id: Long,
    val name: String,
    val imageUrl: String,
    val lastSenderName: String,
    val lastMessage: String,
    val lastMessageTime: String,
    val status: Status,
    val read: Boolean = false,
    val unreadMessages: Int = 0,
    val date: ZonedDateTime,
    val duration: Int
) {

    sealed class Status(label: String) {
        object Past : Status(PAST)
        object Active : Status(ACTIVE)
        object Upcoming : Status(UPCOMING)
        object Unknown : Status("")
    }

    companion object {
        const val UPCOMING = "upcoming"
        const val ACTIVE = "active"
        const val PAST = "past"

        fun getCurrentStatus(date: ZonedDateTime?, duration: Int?): Status {
            val currentTime = Instant.now().atZone(ZoneId.systemDefault())
            val serviceEndTime = date?.plusMinutes(duration?.toLong() ?: 0)
            return date?.let {
                if (currentTime >= date && currentTime <= serviceEndTime) {
                    Status.Active
                } else if (date >= currentTime) {
                    Status.Upcoming
                } else {
                    Status.Past
                }
            } ?: Status.Unknown
        }
    }
}