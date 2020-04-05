package com.orioltobar.diskdatasource.mappers

import com.orioltobar.commons.Constants
import com.orioltobar.commons.Mapper
import com.orioltobar.diskdatasource.models.MovieDbModel
import com.orioltobar.domain.models.movie.MovieModel
import java.util.*
import javax.inject.Inject

class MovieDbMapper @Inject constructor() : Mapper<MovieDbModel, MovieModel> {

    override fun map(from: MovieDbModel?): MovieModel =
        MovieModel(
            from?.id ?: -1L,
            from?.originalTitle ?: "",
            from?.title ?: "",
            from?.popularity ?: 0.0F,
            from?.voteCount ?: 0L,
            from?.video ?: false,
            from?.frontImageUrl ?: "",
            from?.adult ?: false,
            from?.backImageUrl ?: "",
            from?.originalLanguage ?: "",
            from?.genreIds ?: emptyList(),
            from?.genreIds?.getOrNull(0) ?: Constants.DEFAULT_GENRE_ID,
            from?.voteAverage ?: 0.0F,
            from?.overview ?: "",
            from?.releaseDate ?: ""
        )

    fun mapToDbModel(from: MovieModel): MovieDbModel =
        MovieDbModel(
            from.id,
            from.genreIds.getOrNull(0) ?: Constants.DEFAULT_GENRE_ID,
            from.originalTitle,
            from.title,
            from.popularity,
            from.voteCount,
            from.video,
            from.frontImageUrl,
            from.adult,
            from.backImageUrl,
            from.originalLanguage,
            from.genreIds,
            from.voteAverage,
            from.overview,
            from.releaseDate,
            Date().time
        )
}