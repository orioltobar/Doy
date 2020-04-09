package com.napptilians.data.datasources

import com.napptilians.commons.Response
import com.napptilians.commons.error.ErrorModel
import com.napptilians.domain.models.movie.MovieModel

interface DbDataSource {

    suspend fun getMovie(id: Long): Response<MovieModel, ErrorModel>

    suspend fun saveMovie(movie: MovieModel)
}
