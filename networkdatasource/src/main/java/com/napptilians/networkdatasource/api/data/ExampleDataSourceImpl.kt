package com.napptilians.networkdatasource.api.data

import com.napptilians.commons.Response
import com.napptilians.commons.error.ErrorModel
import com.napptilians.commons.map
import com.napptilians.data.datasources.NetworkDataSource
import com.napptilians.domain.models.movie.MovieModel
import com.napptilians.networkdatasource.api.mappers.MovieMapper
import com.napptilians.networkdatasource.utils.safeApiCall
import javax.inject.Inject

class ExampleDataSourceImpl @Inject constructor(
    private val movieService: MovieService,
    private val movieMapper: MovieMapper
) : NetworkDataSource {

    override suspend fun getMovie(id: Long): Response<MovieModel, ErrorModel> =
        safeApiCall { movieService.getMovie(id) }.map(movieMapper::map)
}