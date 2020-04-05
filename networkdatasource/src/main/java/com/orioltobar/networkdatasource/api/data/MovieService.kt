package com.orioltobar.networkdatasource.api.data

import com.orioltobar.networkdatasource.api.models.MovieApiModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {

    @GET("3/movie/{$MOVIE_ID}")
    suspend fun getMovie(
        @Path(MOVIE_ID) movieId: Long,
        @Query(PAGE_ID) pageId: Int = 1
    ): MovieApiModel

    companion object {
        private const val MOVIE_ID = "movie_id"
        private const val PAGE_ID = "page"
    }
}