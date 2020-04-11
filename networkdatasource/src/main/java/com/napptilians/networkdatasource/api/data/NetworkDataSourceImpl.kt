package com.napptilians.networkdatasource.api.data

import com.napptilians.commons.Response
import com.napptilians.commons.error.ErrorModel
import com.napptilians.data.datasources.NetworkDataSource
import com.napptilians.domain.models.category.CategoryModel
import com.napptilians.domain.models.service.ServiceModel
import com.napptilians.networkdatasource.api.mappers.CategoryMapper
import com.napptilians.networkdatasource.api.models.UserRequestApiModel
import com.napptilians.networkdatasource.api.mappers.ServiceInMapper
import com.napptilians.networkdatasource.utils.safeApiCall
import javax.inject.Inject

class NetworkDataSourceImpl @Inject constructor(
    private val categoryService: CategoryService,
    private val categoryMapper: CategoryMapper,
    private val serviceService: ServiceService,
    private val serviceInMapper: ServiceInMapper,
    private val userService: UserService
) : NetworkDataSource {

    override suspend fun getCategories(categoryIds: List<Long>, lang: String): Response<List<CategoryModel>, ErrorModel> {
        return safeApiCall {
            categoryService.getCategories(categoryIds).map {
                categoryMapper.map(it)
            }
        }
    }

    override suspend fun getServices(categoryIds: List<Long>, serviceId: Long?, uid: Long?): Response<List<ServiceModel>, ErrorModel> {
        return safeApiCall {
            serviceService.getServices(categoryIds, null, null).map {
                serviceInMapper.map(it)
            }
        }
    }
    override suspend fun addService(service: ServiceModel): Response<Long, ErrorModel> {
        return safeApiCall {
            serviceService.addService(serviceInMapper.map(service))
        }
    }

    override suspend fun addUser(name: String, email: String, uid: String, token: String):
            Response<Unit, ErrorModel> =
        safeApiCall {
            val body = UserRequestApiModel(name, email, uid, token)
            userService.addUser(body)
        }
}
