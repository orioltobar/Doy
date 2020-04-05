package com.orioltobar.domain.repositories

import com.orioltobar.commons.Response
import com.orioltobar.commons.error.ErrorModel
import com.orioltobar.domain.models.movie.MovieModel
import kotlinx.coroutines.flow.Flow

interface ExampleRepository {

    suspend fun getMovie(id: Long): Response<MovieModel, ErrorModel>

    fun getMovieFlow(): Flow<Response<MovieModel, ErrorModel>>
}