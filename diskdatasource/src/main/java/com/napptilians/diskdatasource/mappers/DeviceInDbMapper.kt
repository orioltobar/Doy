package com.napptilians.diskdatasource.mappers

import com.napptilians.commons.Mapper
import com.napptilians.diskdatasource.models.DeviceDbModel
import com.napptilians.domain.models.device.DeviceModel
import javax.inject.Inject

class DeviceInDbMapper @Inject constructor() : Mapper<DeviceModel, DeviceDbModel> {

    override fun map(from: DeviceModel?): DeviceDbModel =
        DeviceDbModel(from?.id ?: -1L, from?.firebaseToken ?: "")
}
