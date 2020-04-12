package com.napptilians.data.datasources

import com.napptilians.commons.Response
import com.napptilians.commons.error.ErrorModel
import com.napptilians.domain.models.category.CategoryModel
import com.napptilians.domain.models.service.ServiceModel
import com.napptilians.domain.models.user.UserModel

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

    suspend fun updateUser(
        userUid: String,
        name: String? = null,
        token: String? = null,
        description: String? = null,
        image: String? = null
    ): Response<UserModel, ErrorModel>

    suspend fun getUser(userUid: String): Response<UserModel, ErrorModel>

    suspend fun getServices(
        categoryIds: List<Long> = emptyList(),
        serviceId: Long? = null,
        uid: String? = null
    ): Response<List<ServiceModel>, ErrorModel>

    suspend fun addService(service: ServiceModel): Response<Long, ErrorModel>

    suspend fun addAttendee(userUid: String, serviceId: Long): Response<Unit, ErrorModel>

    suspend fun deleteAttendee(userUid: String, serviceId: Long): Response<Unit, ErrorModel>
}
