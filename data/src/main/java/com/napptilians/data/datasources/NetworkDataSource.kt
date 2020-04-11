package com.napptilians.data.datasources

import com.napptilians.commons.Response
import com.napptilians.commons.error.ErrorModel
import com.napptilians.domain.models.category.CategoryModel
import com.napptilians.domain.models.service.ServiceModel

interface NetworkDataSource {

    suspend fun getCategories(
        categoryIds: List<Long> = emptyList(),
        lang: String = "ca"
    ): Response<List<CategoryModel>, ErrorModel>

    suspend fun addUser(
        name: String,
        email: String,
        uid: String,
        token: String
    ): Response<Unit, ErrorModel>

    suspend fun getServices(
        categoryIds: List<Long> = emptyList(),
        serviceId: Long? = null,
        uid: Long? = null
    ): Response<List<ServiceModel>, ErrorModel>

    suspend fun addService(service: ServiceModel): Response<Long, ErrorModel>
}
