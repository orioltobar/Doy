package com.napptilians.data.repositories

import com.google.firebase.auth.AuthResult
import com.napptilians.commons.Failure
import com.napptilians.commons.Response
import com.napptilians.commons.Success
import com.napptilians.commons.either
import com.napptilians.commons.error.ErrorModel
import com.napptilians.commons.map
import com.napptilians.commons.flatMap
import com.napptilians.commons.singleSourceOfTruth
import com.napptilians.data.datasources.DbDataSource
import com.napptilians.data.datasources.FirebaseDataSource
import com.napptilians.data.datasources.NetworkDataSource
import com.napptilians.domain.models.device.DeviceModel
import com.napptilians.domain.models.category.CategoryModel
import com.napptilians.domain.models.service.ServiceModel
import com.napptilians.domain.models.user.UserModel
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

    override suspend fun addAttendee(userUid: String, serviceId: Long): Response<Unit, ErrorModel> =
        networkDataSource.addAttendee(userUid, serviceId)

    override suspend fun deleteAttendee(userUid: String, serviceId: Long): Response<Unit, ErrorModel> =
        networkDataSource.deleteAttendee(userUid, serviceId)

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

    override suspend fun logout(userUid: String) {
        dbDataSource.removeUser(userUid)
        networkDataSource.updateUser(userUid = userUid, token = "")
    }

    override suspend fun getServices(
        categoryIds: List<Long>,
        serviceId: Long?,
        uid: String?
    ): Response<List<ServiceModel>, ErrorModel> {
        return networkDataSource.getServices(categoryIds, serviceId, uid).map {
            it.sortedWith(comparator)
        }
    }

    override suspend fun getUser(userUid: String): Response<UserModel, ErrorModel> =
        singleSourceOfTruth(
            dbDataSource = { dbDataSource.getUser(userUid) },
            networkDataSource = { networkDataSource.getUser(userUid) },
            dbCallback = { user ->
                dbDataSource.saveUser(user)
                dbDataSource.getUser(user.uid)
            }
        )

    override suspend fun updateUser(
        userUid: String,
        name: String?,
        token: String?,
        description: String?,
        image: String?
    ): Response<UserModel, ErrorModel> {
        val request = networkDataSource.updateUser(userUid, name, token, description, image)
        return request.flatMap { user ->
            dbDataSource.saveUser(user)
            dbDataSource.getUser(user.uid)
        }
    }

    private val comparator: Comparator<ServiceModel>
        get() = compareBy({ it.date }, { it.name })

}
