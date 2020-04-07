package com.napptilians.data.repositories

import com.napptilians.commons.Response
import com.napptilians.commons.Success
import com.napptilians.commons.error.ErrorModel
import com.napptilians.commons.singleSourceOfTruth
import com.napptilians.data.datasources.DbDataSource
import com.napptilians.data.datasources.NetworkDataSource
import com.napptilians.domain.models.movie.MovieModel
import com.napptilians.domain.repositories.ExampleRepository
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