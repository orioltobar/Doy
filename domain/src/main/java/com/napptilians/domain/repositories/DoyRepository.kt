package com.napptilians.domain.repositories

import com.napptilians.commons.Response
import com.napptilians.commons.error.ErrorModel
import com.napptilians.domain.models.movie.CategoryModel

interface DoyRepository {

//    suspend fun getMovie(id: Long): Response<MovieModel, ErrorModel>
//
//    fun getMovieFlow(): Flow<Response<MovieModel, ErrorModel>>

    suspend fun getCategories(categoryIds: List<Long> = emptyList()): Response<List<CategoryModel>, ErrorModel>
}
