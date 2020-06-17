package com.napptilians.domain.usecases

import com.napptilians.commons.AppDispatchers
import com.napptilians.domain.models.chat.ChatModel
import com.napptilians.domain.repositories.DoyRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateMessageReadStatusUseCase @Inject constructor(
    appDispatchers: AppDispatchers,
    private val doyRepository: DoyRepository
) {

    private val ioDispatcher = appDispatchers.io

    suspend operator fun invoke(message: ChatModel, status: Boolean) = withContext(ioDispatcher) {
        message.read = status
        doyRepository.updateMessageReadStatus(message)
    }
}