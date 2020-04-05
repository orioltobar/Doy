package com.orioltobar.diskdatasource.data

import com.orioltobar.commons.Failure
import com.orioltobar.commons.Response
import com.orioltobar.commons.error.ErrorModel
import com.orioltobar.data.datasources.DbDataSource
import com.orioltobar.diskdatasource.Cache
import com.orioltobar.diskdatasource.dao.ExampleDao
import com.orioltobar.diskdatasource.mappers.MovieDbMapper
import com.orioltobar.domain.models.movie.MovieModel
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