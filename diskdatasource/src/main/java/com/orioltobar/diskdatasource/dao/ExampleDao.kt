package com.orioltobar.diskdatasource.dao

import androidx.room.*
import com.orioltobar.diskdatasource.models.MovieDbModel

@Dao
interface ExampleDao {

    @Query("SELECT * FROM movies")
    suspend fun getMovies(): List<MovieDbModel>

    @Query("SELECT * FROM movies WHERE main_genre_id == :genreId")
    suspend fun getMoviesByGenre(genreId: Int): List<MovieDbModel>

    @Query("SELECT * FROM movies WHERE id == :id")
    suspend fun getMovie(id: Long): MovieDbModel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWithTimestamp(movie: MovieDbModel)

    @Delete
    suspend fun deleteMovie(user: MovieDbModel)
}