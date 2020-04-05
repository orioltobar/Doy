package com.orioltobar.networkdatasource.api.data

import com.orioltobar.commons.Response
import com.orioltobar.commons.error.ErrorModel
import com.orioltobar.commons.map
import com.orioltobar.data.datasources.NetworkDataSource
import com.orioltobar.domain.models.movie.MovieModel
import com.orioltobar.networkdatasource.api.mappers.MovieMapper
import com.orioltobar.networkdatasource.utils.safeApiCall
import javax.inject.Inject

class ExampleDataSourceImpl @Inject constructor(
    private val movieService: MovieService,
    private val movieMapper: MovieMapper
) : NetworkDataSource {

    override suspend fun getMovie(id: Long): Response<MovieModel, ErrorModel> =
        safeApiCall { movieService.getMovie(id) }.map(movieMapper::map)
}