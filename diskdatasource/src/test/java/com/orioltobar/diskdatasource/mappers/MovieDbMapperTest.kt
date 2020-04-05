package com.orioltobar.diskdatasource.mappers

import com.orioltobar.commons.Constants
import com.orioltobar.diskdatasource.models.MovieDbModel
import com.orioltobar.domain.models.movie.MovieModel
import org.junit.Assert.assertEquals
import org.junit.Test

class MovieDbMapperTest {

    private val mapper = MovieDbMapper()

    @Test
    fun `Map non-null received API values`() {
        val dbModel =
            MovieDbModel(
                12, 1, "Test",
                "Test", 1.0F, 1, true,
                "/front", false, "/back",
                "en", listOf(12, 34), 5.0F,
                "overview", "1978", 12345L
            )

        val model = mapper.map(dbModel)

        assertEquals(dbModel.id, model.id)
        assertEquals(dbModel.originalTitle, model.originalTitle)
        assertEquals(dbModel.title, model.title)
        assertEquals(dbModel.popularity, model.popularity)
        assertEquals(dbModel.voteCount, model.voteCount)
        assertEquals(dbModel.video, model.video)
        assertEquals(dbModel.frontImageUrl, model.frontImageUrl)
        assertEquals(dbModel.adult, model.adult)
        assertEquals(dbModel.backImageUrl, model.backImageUrl)
        assertEquals(dbModel.originalLanguage, model.originalLanguage)
        assertEquals(dbModel.genreIds, model.genreIds)
        assertEquals(dbModel.genreIds.getOrNull(0), model.mainGenreId)
        assertEquals(dbModel.voteAverage, model.voteAverage)
        assertEquals(dbModel.overview, model.overview)
        assertEquals(dbModel.releaseDate, model.releaseDate)
    }

    @Test
    fun `MapToDb non-null received API values`() {
        val model = MovieModel(
            1, "Test",
            "Test", 1.0F, 1, true,
            "/front", false, "/back",
            "en", listOf(12, 34), 12, 5.0F,
            "overview", "1978"
        )

        val dbModel = mapper.mapToDbModel(model)

        assertEquals(model.id, dbModel.id)
        assertEquals(model.originalTitle, dbModel.originalTitle)
        assertEquals(model.title, dbModel.title)
        assertEquals(model.popularity, dbModel.popularity)
        assertEquals(model.voteCount, dbModel.voteCount)
        assertEquals(model.video, dbModel.video)
        assertEquals(model.frontImageUrl, dbModel.frontImageUrl)
        assertEquals(model.adult, dbModel.adult)
        assertEquals(model.backImageUrl, dbModel.backImageUrl)
        assertEquals(model.originalLanguage, dbModel.originalLanguage)
        assertEquals(model.genreIds, dbModel.genreIds)
        assertEquals(model.genreIds.getOrNull(0), dbModel.mainGenreId)
        assertEquals(model.voteAverage, dbModel.voteAverage)
        assertEquals(model.overview, dbModel.overview)
        assertEquals(model.releaseDate, dbModel.releaseDate)
    }

    @Test
    fun `Map null received API values to default ones`() {
        val model = mapper.map(null)

        assertEquals(-1L, model.id)
        assertEquals("", model.originalTitle)
        assertEquals("", model.title)
        assertEquals(0.0F, model.popularity)
        assertEquals(0L, model.voteCount)
        assertEquals(false, model.video)
        assertEquals("", model.frontImageUrl)
        assertEquals(false, model.adult)
        assertEquals("", model.backImageUrl)
        assertEquals("", model.originalLanguage)
        assertEquals(0, model.genreIds.size)
        assertEquals(Constants.DEFAULT_GENRE_ID, model.mainGenreId)
        assertEquals(0.0F, model.voteAverage)
        assertEquals("", model.overview)
        assertEquals("", model.releaseDate)
    }
}