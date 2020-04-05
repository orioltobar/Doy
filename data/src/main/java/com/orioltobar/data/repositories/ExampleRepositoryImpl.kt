package com.orioltobar.data.repositories

import com.orioltobar.commons.Response
import com.orioltobar.commons.Success
import com.orioltobar.commons.error.ErrorModel
import com.orioltobar.commons.singleSourceOfTruth
import com.orioltobar.data.datasources.DbDataSource
import com.orioltobar.data.datasources.NetworkDataSource
import com.orioltobar.domain.models.movie.MovieModel
import com.orioltobar.domain.repositories.ExampleRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlin.random.Random

class ExampleRepositoryImpl @Inject constructor(
    private val dataSource: NetworkDataSource,
    private val dbDataSource: DbDataSource
) :
    ExampleRepository {

    override suspend fun getMovie(id: Long): Response<MovieModel, ErrorModel> {
        // TODO: Example.
        val genresList = Any()
        return singleSourceOfTruth(
            dbDataSource = {
                val movie = dbDataSource.getMovie(id)
                if (movie is Success) {
//                    movie.result.genres = getSelectedGenresFromList(movie.result)
                }
                movie
            },
            networkDataSource = { dataSource.getMovie(id) },
            dbCallback = { apiResult ->
                dbDataSource.saveMovie(apiResult)
                val movie = dbDataSource.getMovie(id)
                if (movie is Success) {
//                    movie.result.genres = getSelectedGenresFromList(movie.result)
                }
                movie
            }
        )
    }

    override fun getMovieFlow(): Flow<Response<MovieModel, ErrorModel>> = flow {
        for (x in 0 until 10) {
            val id = Random.nextLong(1L, 999L)
            emit(dataSource.getMovie(id))
            delay(60000L)
        }
    }

    /**
     * Retrieves the main genre of the [movie] using a [genres] list.
     */
    private fun getSelectedGenresFromList(
        movie: MovieModel
    ): Any = Any()
}