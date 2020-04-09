package com.napptilians.data.repositories

import com.google.firebase.auth.AuthResult
import com.napptilians.commons.Response
import com.napptilians.commons.error.ErrorModel
import com.napptilians.data.datasources.DbDataSource
import com.napptilians.data.datasources.FirebaseDataSource
import com.napptilians.data.datasources.NetworkDataSource
import com.napptilians.domain.models.device.DeviceModel
import com.napptilians.domain.models.movie.CategoryModel
import com.napptilians.domain.repositories.DoyRepository
import javax.inject.Inject

class DoyRepositoryImpl @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val dbDataSource: DbDataSource,
    private val firebaseDataSource: FirebaseDataSource
) : DoyRepository {

    override suspend fun getCategories(categoryIds: List<Long>, lang: String): Response<List<CategoryModel>, ErrorModel> {
        return networkDataSource.getCategories(categoryIds, lang)
    }

    override suspend fun getDeviceInfo(): Response<DeviceModel, ErrorModel> =
        dbDataSource.getDeviceInfo()

    override suspend fun saveDeviceInfo(device: DeviceModel) = dbDataSource.saveDeviceInfo(device)

    override suspend fun login(
        email: String,
        password: String
    ): Response<AuthResult, ErrorModel> =
        firebaseDataSource.login(email, password)

    override suspend fun register(
        email: String,
        password: String
    ): Response<AuthResult, ErrorModel> = firebaseDataSource.register(email, password)
}
