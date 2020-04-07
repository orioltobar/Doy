package com.napptilians.diskdatasource.data

import com.napptilians.commons.Failure
import com.napptilians.commons.Response
import com.napptilians.commons.error.ErrorModel
import com.napptilians.data.datasources.DbDataSource
import com.napptilians.diskdatasource.Cache
import com.napptilians.diskdatasource.dao.ExampleDao
import com.napptilians.diskdatasource.mappers.MovieDbMapper
import com.napptilians.domain.models.movie.MovieModel
import javax.inject.Inject

class ExampleDataBaseImpl @Inject constructor(
    private val exampleDao: ExampleDao,
    private val movieDbMapper: MovieDbMapper
) : DbDataSource {

    override suspend fun getMovie(id: Long): Response<MovieModel, ErrorModel> =
        exampleDao.getMovie(id)?.let {
            Cache.checkTimestampCache(it.timeStamp, movieDbMapper.map(it))
        } ?: run { Failure(ErrorModel("")) }

    override suspend fun saveMovie(movie: MovieModel) {
        return exampleDao.insertWithTimestamp(movieDbMapper.mapToDbModel(movie))
    }

//    /**
//     * Process the [list] of db models retrieved by the database in order to check if the cache is
//     * expired or the response is empty. In that case a Failure is returned.
//     */
//    private suspend fun processDbResponse(list: List<MovieDbModel>): Response<List<MovieModel>, ErrorModel> {
//        val movieList: MutableList<MovieModel> = mutableListOf()
//        for (model in list) {
//            val movie = Cache.checkTimestampCache(model.timeStamp, movieDbMapper.map(model))
//            if (movie is Success) {
//                val genres = movieGenreDao.getGenres()
//                val genresFiltered = movie.result.genreIds.flatMap { ids ->
//                    genres.filter { it.id == ids }.map(movieGenreDbMapper::map)
//                }
//                movie.result.genres = MovieGenresModel(genresFiltered)
//                movieList.add(movie.result)
//            } else {
//                movieList.clear()
//                break
//            }
//        }
//        return if (movieList.isNotEmpty()) {
//            Success(movieList.toList())
//        } else {
//            Failure(ErrorModel("Error in MovieDataBase"))
//        }
//    }
}