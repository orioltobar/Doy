package com.orioltobar.networkdatasource.api.mappers

import com.google.gson.Gson
import com.orioltobar.commons.Constants
import com.orioltobar.commons.MockObjects
import com.orioltobar.networkdatasource.api.models.MovieApiModel
import org.junit.Assert.assertEquals
import org.junit.Test

class MovieMapperTest {

    private val mapper = MovieMapper()

    @Test
    fun `Map non-null received API values`() {
        val apiModel = Gson().fromJson(MockObjects.movieJson, MovieApiModel::class.java)

        val model = mapper.map(apiModel)

        assertEquals(apiModel.id, model.id)
        assertEquals(apiModel.originalTitle, model.originalTitle)
        assertEquals(apiModel.title, model.title)
        assertEquals(apiModel.popularity, model.popularity)
        assertEquals(apiModel.voteCount, model.voteCount)
        assertEquals(apiModel.video, model.video)
        assertEquals(Constants.IMAGE_BASE_URL + apiModel.frontImageUrl, model.frontImageUrl)
        assertEquals(apiModel.adult, model.adult)
        assertEquals(Constants.IMAGE_BASE_URL + apiModel.backImageUrl, model.backImageUrl)
        assertEquals(apiModel.originalLanguage, model.originalLanguage)
        assertEquals(apiModel.genreIds, model.genreIds)
        assertEquals(apiModel.genreIds?.getOrNull(0), model.mainGenreId)
        assertEquals(apiModel.voteAverage, model.voteAverage)
        assertEquals(apiModel.overview, model.overview)
        assertEquals(apiModel.releaseDate, model.releaseDate)
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
        assertEquals(Constants.IMAGE_BASE_URL, model.frontImageUrl)
        assertEquals(false, model.adult)
        assertEquals(Constants.IMAGE_BASE_URL, model.backImageUrl)
        assertEquals("", model.originalLanguage)
        assertEquals(0, model.genreIds.size)
        assertEquals(Constants.DEFAULT_GENRE_ID, model.mainGenreId)
        assertEquals(0.0F, model.voteAverage)
        assertEquals("", model.overview)
        assertEquals("", model.releaseDate)
    }
}