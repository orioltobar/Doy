package com.orioltobar.data.datasources

import com.orioltobar.commons.Response
import com.orioltobar.commons.error.ErrorModel
import com.orioltobar.domain.models.movie.MovieModel

interface NetworkDataSource {

    suspend fun getMovie(id: Long): Response<MovieModel, ErrorModel>
}