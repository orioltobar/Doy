package com.napptilians.diskdatasource.mappers

import com.napptilians.commons.Mapper
import com.napptilians.diskdatasource.models.DeviceDbModel
import com.napptilians.domain.models.device.DeviceModel
import javax.inject.Inject

class DeviceOutDbMapper @Inject constructor() : Mapper<DeviceDbModel, DeviceModel> {

    override fun map(from: DeviceDbModel?): DeviceModel =
        DeviceModel(from?.id ?: -1L, from?.firebaseToken ?: "")
}
