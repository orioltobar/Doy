package com.orioltobar.domain.models.movie

data class MovieModel(
    val id: Long,
    val originalTitle: String,
    val title: String,
    val popularity: Float,
    val voteCount: Long,
    val video: Boolean,
    val frontImageUrl: String,
    val adult: Boolean,
    val backImageUrl: String,
    val originalLanguage: String,
    val genreIds: List<Int>,
    var mainGenreId: Int,
    val voteAverage: Float,
    val overview: String,
    val releaseDate: String
)