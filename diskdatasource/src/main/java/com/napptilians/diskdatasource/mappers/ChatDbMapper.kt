package com.napptilians.diskdatasource.mappers

import com.napptilians.commons.Mapper
import com.napptilians.diskdatasource.models.ChatMessageDbModel
import com.napptilians.domain.models.chat.ChatModel
import javax.inject.Inject

class ChatDbMapper @Inject constructor() : Mapper<ChatModel, ChatMessageDbModel> {

    override fun map(from: ChatModel?): ChatMessageDbModel = ChatMessageDbModel(
        from?.chatId ?: "",
        from?.message ?: "",
        from?.senderName ?: "",
        from?.senderId ?: "",
        from?.timeStamp ?: -1L,
        from?.read ?: false
    )

    fun mapFromDb(from: ChatMessageDbModel?): ChatModel = ChatModel(
        from?.chatId ?: "",
        from?.message ?: "",
        from?.senderName ?: "",
        from?.senderId ?: "",
        from?.timeStamp ?: -1L,
        from?.read ?: false
    )
}