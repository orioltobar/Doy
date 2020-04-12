package com.napptilians.domain.usecases

import com.napptilians.commons.AppDispatchers
import com.napptilians.domain.repositories.DoyRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetDeviceInfoUseCase @Inject constructor(
    appDispatchers: AppDispatchers,
    private val doyRepository: DoyRepository
) {

    private val ioDispatcher = appDispatchers.io

    suspend fun execute() = withContext(ioDispatcher) { doyRepository.getDeviceInfo() }
}
