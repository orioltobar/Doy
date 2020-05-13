package com.napptilians.domain.models.chat

/**
 * Defines the model to fill the data of a chat view holder. [id] is the service id,
 * [name] defines the name of the chat, or the event title.
 * [lastSenderName], [lastMessage] defines the last data from the chat and
 * [lastMessageTime] is the hour of the last message. [lastMessageTime] time should be always
 * formatted. e.g.: "1m", "5h", "01/01/1973"
 */
data class ChatListItemModel(
    val id: Long,
    val name: String,
    val imageUrl: String,
    val lastSenderName: String,
    val lastMessage: String,
    val lastMessageTime: String
)