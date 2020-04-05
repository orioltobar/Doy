package com.orioltobar.networkdatasource.api.mappers

import com.orioltobar.commons.Constants
import com.orioltobar.commons.Mapper
import com.orioltobar.domain.models.movie.MovieModel
import com.orioltobar.networkdatasource.api.models.MovieApiModel
import javax.inject.Inject

class MovieMapper @Inject constructor() : Mapper<MovieApiModel, MovieModel> {

    override fun map(from: MovieApiModel?): MovieModel = MovieModel(
        from?.id ?: -1L,
        from?.originalTitle ?: "",
        from?.title ?: "",
        from?.popularity ?: 0.0F,
        from?.voteCount ?: 0L,
        from?.video ?: false,
        Constants.IMAGE_BASE_URL + (from?.frontImageUrl ?: ""),
        from?.adult ?: false,
        Constants.IMAGE_BASE_URL + (from?.backImageUrl ?: ""),
        from?.originalLanguage ?: "",
        from?.genreIds?.filterNotNull() ?: emptyList(),
        from?.genreIds?.getOrNull(0) ?: Constants.DEFAULT_GENRE_ID,
        from?.voteAverage ?: 0.0F,
        from?.overview ?: "",
        from?.releaseDate ?: ""
    )
}