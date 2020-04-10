package com.napptilians.domain.repositories

import com.google.firebase.auth.AuthResult
import com.napptilians.commons.Response
import com.napptilians.commons.error.ErrorModel
import com.napptilians.domain.models.device.DeviceModel
import com.napptilians.domain.models.movie.CategoryModel
import com.napptilians.domain.models.movie.ServiceModel

interface DoyRepository {

//    suspend fun getMovie(id: Long): Response<MovieModel, ErrorModel>
//
//    fun getMovieFlow(): Flow<Response<MovieModel, ErrorModel>>

    suspend fun getCategories(categoryIds: List<Long> = emptyList()): Response<List<CategoryModel>, ErrorModel>

    suspend fun getDeviceInfo(): Response<DeviceModel, ErrorModel>

    suspend fun saveDeviceInfo(device: DeviceModel): Response<Unit, ErrorModel>

    suspend fun addService(service: ServiceModel): Response<Long, ErrorModel>

    suspend fun login(email: String, password: String): Response<AuthResult, ErrorModel>

    suspend fun register(email: String, password: String): Response<AuthResult, ErrorModel>

    suspend fun getServices(categoryId: Long): Response<List<ServiceModel>, ErrorModel>
}
