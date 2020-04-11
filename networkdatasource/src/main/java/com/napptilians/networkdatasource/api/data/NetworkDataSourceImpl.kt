package com.napptilians.networkdatasource.api.data

import com.napptilians.commons.Response
import com.napptilians.commons.error.ErrorModel
import com.napptilians.commons.map
import com.napptilians.data.datasources.NetworkDataSource
import com.napptilians.domain.models.category.CategoryModel
import com.napptilians.domain.models.service.ServiceModel
import com.napptilians.domain.models.user.UserModel
import com.napptilians.networkdatasource.api.mappers.CategoryMapper
import com.napptilians.networkdatasource.api.mappers.ServiceInMapper
import com.napptilians.networkdatasource.api.mappers.UserMapper
import com.napptilians.networkdatasource.api.models.UserRequestApiModel
import com.napptilians.networkdatasource.api.models.UserUpdateRequestApiModel
import com.napptilians.networkdatasource.utils.safeApiCall
import javax.inject.Inject

class NetworkDataSourceImpl @Inject constructor(
    private val categoryService: CategoryService,
    private val categoryMapper: CategoryMapper,
    private val serviceService: ServiceService,
    private val serviceInMapper: ServiceInMapper,
    private val userService: UserService,
    private val userMapper: UserMapper
) : NetworkDataSource {

    override suspend fun getCategories(
        categoryIds: List<Long>,
        lang: String
    ): Response<List<CategoryModel>, ErrorModel> {
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

    override suspend fun addAttendee(userUid: String, serviceId: Long): Response<Unit, ErrorModel> {
        return safeApiCall {
            serviceService.addAttendee(userUid, serviceId)
        }
    }

    override suspend fun addUser(name: String, email: String, uid: String, token: String):
            Response<Unit, ErrorModel> =
        safeApiCall {
            val body = UserRequestApiModel(name, email, uid, token)
            userService.addUser(body)
        }

    override suspend fun updateUser(
        userUid: String,
        name: String?,
        token: String?,
        description: String?,
        image: String?
    ): Response<UserModel, ErrorModel> =
        safeApiCall {
            val body = UserUpdateRequestApiModel(userUid, name, token, description, image)
            userService.updateUser(body)
        }.map {
            userMapper.map(it[0])
        }

    override suspend fun getUser(userUid: String): Response<UserModel, ErrorModel> =
        safeApiCall {
            userService.getUser(userUid)
        }.map {
            userMapper.map(it[0])
        }
}
