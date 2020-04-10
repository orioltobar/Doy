package com.napptilians.domain.usecases

import com.napptilians.commons.AppDispatchers
import com.napptilians.domain.repositories.DoyRepository
import javax.inject.Inject
import kotlinx.coroutines.withContext

class GetServicesUseCase @Inject constructor(
    appDispatchers: AppDispatchers,
    private val doyRepository: DoyRepository
) {
    private val ioDispatcher = appDispatchers.io

    suspend fun execute(categoryId: Long) =
        withContext(ioDispatcher) { doyRepository.getServices(categoryId) }
}
