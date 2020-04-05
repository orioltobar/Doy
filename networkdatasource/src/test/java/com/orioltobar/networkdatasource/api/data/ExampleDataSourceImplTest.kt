package com.orioltobar.networkdatasource.api.data

import com.orioltobar.networkdatasource.api.mappers.MovieMapper
import com.orioltobar.networkdatasource.api.models.MovieApiModel
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ExampleDataSourceImplTest {

    init {
        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    @MockK
    private lateinit var movieService: MovieService

    @MockK
    private lateinit var movieMapper: MovieMapper

    @MockK
    private lateinit var movieMock: MovieApiModel

    private val dataSource by lazy {
        ExampleDataSourceImpl(
            movieService,
            movieMapper
        )
    }

    @Before
    fun setup() {
//        val mockMovieListApiModel =
//            Gson().fromJson(MockObjects.movieListJson, MovieListApiModel::class.java)

        coEvery { movieService.getMovie(any()) } returns movieMock
        every { movieMapper.map(any()) } returns mockk()
    }

    @Test
    fun `getMovie() must call API service getMovie() and map the result`() =
        runBlockingTest {
            // Given
            val movieId = 550L

            // When
            dataSource.getMovie(movieId)

            // Then
            coVerify(exactly = 1) {
                movieService.getMovie(movieId)
                movieMapper.map(any())
            }
        }
}