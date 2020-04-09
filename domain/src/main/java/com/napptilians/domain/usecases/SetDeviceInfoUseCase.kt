package com.napptilians.domain.usecases

import com.napptilians.commons.AppDispatchers
import com.napptilians.commons.Response
import com.napptilians.commons.error.ErrorModel
import com.napptilians.domain.models.device.DeviceModel
import com.napptilians.domain.repositories.ExampleRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SetDeviceInfoUseCase @Inject constructor(
    appDispatchers: AppDispatchers,
    private val exampleRepository: ExampleRepository
) {

    private val ioDispatcher = appDispatchers.io

    suspend fun execute(device: DeviceModel): Response<Unit, ErrorModel> =
        withContext(ioDispatcher) { exampleRepository.saveDeviceInfo(device) }
}
