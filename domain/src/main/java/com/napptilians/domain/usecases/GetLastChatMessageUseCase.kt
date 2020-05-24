package com.napptilians.domain.usecases

import com.napptilians.commons.AppDispatchers
import com.napptilians.commons.Response
import com.napptilians.commons.error.ErrorModel
import com.napptilians.domain.models.chat.ChatModel
import com.napptilians.domain.repositories.DoyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetLastChatMessageUseCase @Inject constructor(
    appDispatchers: AppDispatchers,
    private val doyRepository: DoyRepository
) {

    private val ioDispatcher = appDispatchers.io

    operator fun invoke(chatId: String): Flow<Response<Pair<ChatModel, Int>, ErrorModel>> =
        doyRepository.getLastChatMessage(chatId).flowOn(ioDispatcher)
}