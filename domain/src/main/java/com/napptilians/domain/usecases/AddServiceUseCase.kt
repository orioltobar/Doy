package com.napptilians.domain.usecases

import com.napptilians.commons.AppDispatchers
import com.napptilians.commons.Response
import com.napptilians.commons.error.ErrorModel
import com.napptilians.domain.models.movie.ServiceModel
import com.napptilians.domain.repositories.DoyRepository
import javax.inject.Inject
import kotlinx.coroutines.withContext

class AddServiceUseCase @Inject constructor(
    appDispatchers: AppDispatchers,
    private val doyRepository: DoyRepository
) {

    private val ioDispatcher = appDispatchers.io

    suspend fun execute(service: ServiceModel): Response<Long, ErrorModel> =
        withContext(ioDispatcher) { doyRepository.addService(service) }
}
