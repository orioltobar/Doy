package com.napptilians.networkdatasource.api.mappers

import com.napptilians.commons.Mapper
import com.napptilians.domain.models.movie.ServiceModel
import com.napptilians.networkdatasource.api.models.ServiceApiModel
import javax.inject.Inject

class ServiceInMapper @Inject constructor() : Mapper<ServiceModel, ServiceApiModel> {

    override fun map(from: ServiceModel?): ServiceApiModel =
        ServiceApiModel(
            from?.serviceId ?: -1L,
            from?.categoryId ?: -1L,
            from?.name ?: "",
            from?.description ?: "",
            from?.image?.toString(),
            from?.day ?: "",
            from?.spots ?: 1,
            from?.durationMin ?: 30,
            from?.ownerId ?: ""
        )
}
