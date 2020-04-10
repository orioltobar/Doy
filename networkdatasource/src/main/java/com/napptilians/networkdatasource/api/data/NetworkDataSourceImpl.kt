package com.napptilians.networkdatasource.api.data

import com.napptilians.commons.Response
import com.napptilians.commons.error.ErrorModel
import com.napptilians.data.datasources.NetworkDataSource
import com.napptilians.domain.models.movie.CategoryModel
import com.napptilians.networkdatasource.api.mappers.CategoryMapper
import com.napptilians.networkdatasource.api.models.UserRequestApiModel
import com.napptilians.networkdatasource.utils.safeApiCall
import javax.inject.Inject

class NetworkDataSourceImpl @Inject constructor(
    private val categoryListService: CategoryListService,
    private val userService: UserService,
    private val categoryMapper: CategoryMapper
) : NetworkDataSource {

    override suspend fun getCategories(categoryIds: List<Long>): Response<List<CategoryModel>, ErrorModel> {
        return safeApiCall {
            categoryListService.getCategories(categoryIds).map {
                categoryMapper.map(it)
            }
        }
    }

    override suspend fun addUser(name: String, email: String, uid: String, token: String):
            Response<Unit, ErrorModel> =
        safeApiCall {
            val body = UserRequestApiModel(name, email, uid, token)
            userService.addUser(body)
        }
}
