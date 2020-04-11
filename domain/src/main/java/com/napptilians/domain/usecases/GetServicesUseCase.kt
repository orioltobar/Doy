package com.napptilians.domain.usecases

import com.napptilians.commons.AppDispatchers
import com.napptilians.commons.map
import com.napptilians.domain.repositories.DoyRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetServicesUseCase @Inject constructor(
    appDispatchers: AppDispatchers,
    private val doyRepository: DoyRepository
) {
    private val ioDispatcher = appDispatchers.io

    suspend fun execute(
        categoryIds: List<Long> = emptyList(),
        serviceId: Long? = null,
        uid: String? = null
    ) =
        withContext(ioDispatcher) { doyRepository.getServices(categoryIds, serviceId, uid) }
}
