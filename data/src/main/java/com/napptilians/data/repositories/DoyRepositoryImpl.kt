package com.napptilians.data.repositories

import com.napptilians.commons.Response
import com.napptilians.commons.error.ErrorModel
import com.napptilians.data.datasources.DbDataSource
import com.napptilians.data.datasources.NetworkDataSource
import com.napptilians.domain.models.movie.CategoryModel
import com.napptilians.domain.models.movie.MovieModel
import com.napptilians.domain.repositories.DoyRepository
import javax.inject.Inject

class DoyRepositoryImpl @Inject constructor(
    private val dataSource: NetworkDataSource,
    private val dbDataSource: DbDataSource
) : DoyRepository {

//    override suspend fun getMovie(id: Long): Response<MovieModel, ErrorModel> {
//        // TODO: Example.
//        val genresList = Any()
//        return singleSourceOfTruth(
//            dbDataSource = {
//                val movie = dbDataSource.getMovie(id)
//                if (movie is Success) {
// //                    movie.result.genres = getSelectedGenresFromList(movie.result)
//                }
//                movie
//            },
//            networkDataSource = { dataSource.getMovie(id) },
//            dbCallback = { apiResult ->
//                dbDataSource.saveMovie(apiResult)
//                val movie = dbDataSource.getMovie(id)
//                if (movie is Success) {
// //                    movie.result.genres = getSelectedGenresFromList(movie.result)
//                }
//                movie
//            }
//        )
//    }
//
//    override fun getMovieFlow(): Flow<Response<MovieModel, ErrorModel>> = flow {
//        for (x in 0 until 10) {
//            val id = Random.nextLong(1L, 999L)
//            emit(dataSource.getMovie(id))
//            delay(60000L)
//        }
//    }

    override suspend fun getCategories(categoryIds: List<Long>): Response<List<CategoryModel>, ErrorModel> {
        return dataSource.getCategories(categoryIds)
    }

    /**
     * Retrieves the main genre of the [movie] using a [genres] list.
     */
    private fun getSelectedGenresFromList(
        movie: MovieModel
    ): Any = Any()
}
