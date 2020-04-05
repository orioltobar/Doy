package com.orioltobar.networkdatasource.api.models

import com.google.gson.annotations.SerializedName

data class MovieApiModel(
    @SerializedName("id")
    val id: Long? = null,

    @SerializedName("original_title")
    val originalTitle: String? = null,

    @SerializedName("title")
    val title: String? = null,

    @SerializedName("popularity")
    val popularity: Float? = null,

    @SerializedName("vote_count")
    val voteCount: Long? = null,

    @SerializedName("video")
    val video: Boolean? = null,

    @SerializedName("poster_path")
    val frontImageUrl: String? = null,

    @SerializedName("adult")
    val adult: Boolean? = null,

    @SerializedName("backdrop_path")
    val backImageUrl: String? = null,

    @SerializedName("original_language")
    val originalLanguage: String? = null,

    @SerializedName("genre_ids")
    val genreIds: List<Int?>? = null,

    @SerializedName("vote_average")
    val voteAverage: Float? = null,

    @SerializedName("overview")
    val overview: String? = null,

    @SerializedName("release_date")
    val releaseDate: String? = null
)