package com.orioltobar.diskdatasource.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieDbModel(
    @PrimaryKey
    val id: Long,

    @ColumnInfo(name = "main_genre_id")
    val mainGenreId: Int,

    @ColumnInfo(name = "original_title")
    val originalTitle: String,

    @ColumnInfo(name = "tittle")
    val title: String,

    @ColumnInfo(name = "popularity")
    val popularity: Float,

    @ColumnInfo(name = "vote_count")
    val voteCount: Long,

    @ColumnInfo(name = "video")
    val video: Boolean,

    @ColumnInfo(name = "poster_path")
    val frontImageUrl: String,

    @ColumnInfo(name = "adult")
    val adult: Boolean,

    @ColumnInfo(name = "backdrop_path")
    val backImageUrl: String,

    @ColumnInfo(name = "original_language")
    val originalLanguage: String,

    @ColumnInfo(name = "genre_ids")
    val genreIds: List<Int>,

    @ColumnInfo(name = "vote_average")
    val voteAverage: Float,

    @ColumnInfo(name = "overview")
    val overview: String,

    @ColumnInfo(name = "release_date")
    val releaseDate: String,

    @ColumnInfo(name = "timestamp")
    val timeStamp: Long
)