package com.napptilians.data.repositories

import com.google.firebase.auth.AuthResult
import com.napptilians.commons.Failure
import com.napptilians.commons.Response
import com.napptilians.commons.Success
import com.napptilians.commons.either
import com.napptilians.commons.error.ErrorModel
import com.napptilians.data.datasources.DbDataSource
import com.napptilians.data.datasources.FirebaseDataSource
import com.napptilians.data.datasources.NetworkDataSource
import com.napptilians.domain.models.device.DeviceModel
import com.napptilians.domain.models.category.CategoryModel
import com.napptilians.domain.models.service.ServiceModel
import com.napptilians.domain.repositories.DoyRepository
import javax.inject.Inject

class DoyRepositoryImpl @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val dbDataSource: DbDataSource,
    private val firebaseDataSource: FirebaseDataSource
) : DoyRepository {

    override suspend fun getCategories(categoryIds: List<Long>): Response<List<CategoryModel>, ErrorModel> {
        return networkDataSource.getCategories(categoryIds)
    }

    override suspend fun getDeviceInfo(): Response<DeviceModel, ErrorModel> =
        dbDataSource.getDeviceInfo()

    override suspend fun saveDeviceInfo(device: DeviceModel): Response<Unit, ErrorModel> =
        dbDataSource.saveDeviceInfo(device)

    override suspend fun addService(service: ServiceModel): Response<Long, ErrorModel> =
        networkDataSource.addService(service)

    override suspend fun login(
        email: String,
        password: String
    ): Response<AuthResult, ErrorModel> =
        firebaseDataSource.login(email, password)

    override suspend fun register(
        name: String, password: String, email: String, token: String
    ): Response<Unit, ErrorModel> {

        // Register in Firebase
        val fireBaseRequest = firebaseDataSource.register(email, password)

        return fireBaseRequest.either(
            onSuccess = {
                // Then send tokens to serve
                val uid = it.user?.uid ?: ""
                val request = networkDataSource.addUser(name, email, uid, token)
                request.either(
                    onSuccess = { Success(Unit) },
                    onFailure = { error ->
                        firebaseDataSource.removeCurrentUser()
                        Failure(error)
                    }
                )
            },
            onFailure = { Failure(it) }
        )
    }

    override suspend fun getServices(
        categoryIds: List<Long>,
        serviceId: Long?,
        uid: Long?
    ): Response<List<ServiceModel>, ErrorModel> {
        return networkDataSource.getServices(categoryIds, serviceId, uid)
    }
}
