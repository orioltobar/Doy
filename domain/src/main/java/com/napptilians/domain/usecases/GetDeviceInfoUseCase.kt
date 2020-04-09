package com.napptilians.domain.usecases

import com.napptilians.commons.AppDispatchers
import com.napptilians.domain.repositories.ExampleRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetDeviceInfoUseCase @Inject constructor(
    appDispatchers: AppDispatchers,
    private val exampleRepository: ExampleRepository
) {

    private val ioDispatcher = appDispatchers.io

    suspend fun execute() = withContext(ioDispatcher) { exampleRepository.getDeviceInfo() }
}
