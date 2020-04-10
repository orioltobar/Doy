package com.napptilians.data.datasources

import com.napptilians.commons.Response
import com.napptilians.commons.error.ErrorModel
import com.napptilians.domain.models.movie.CategoryModel

interface NetworkDataSource {

    suspend fun getCategories(categoryIds: List<Long> = emptyList()): Response<List<CategoryModel>, ErrorModel>

    suspend fun addUser(name: String, email: String, uid: String, token: String): Response<Unit, ErrorModel>
}
