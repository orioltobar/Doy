package com.napptilians.data.repositories

import com.napptilians.commons.Response
import com.napptilians.commons.error.ErrorModel
import com.napptilians.data.datasources.DbDataSource
import com.napptilians.data.datasources.NetworkDataSource
import com.napptilians.domain.models.device.DeviceModel
import com.napptilians.domain.models.movie.CategoryModel
import com.napptilians.domain.repositories.DoyRepository
import javax.inject.Inject

class DoyRepositoryImpl @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val dbDataSource: DbDataSource
) : DoyRepository {

    override suspend fun getCategories(categoryIds: List<Long>): Response<List<CategoryModel>, ErrorModel> {
        return networkDataSource.getCategories(categoryIds)
    }

    override suspend fun getDeviceInfo(): Response<DeviceModel, ErrorModel> =
        dbDataSource.getDeviceInfo()

    override suspend fun saveDeviceInfo(device: DeviceModel) = dbDataSource.saveDeviceInfo(device)

}
