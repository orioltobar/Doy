package com.napptilians.diskdatasource.data

import com.napptilians.diskdatasource.DbMocks.getDbModel
import com.napptilians.diskdatasource.dao.ExampleDao
import com.napptilians.diskdatasource.mappers.MovieDbMapper
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class MovieDataBaseTestImpl {

    init {
        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    @MockK
    private lateinit var exampleDao: ExampleDao

    @MockK
    private lateinit var movieDbMapper: MovieDbMapper

    private val dbDataSource by lazy {
        ExampleDataBaseImpl(
            exampleDao,
            movieDbMapper
        )
    }

    @Before
    fun setup() {
        coEvery { exampleDao.getMovie(any()) } returns getDbModel()
        coEvery { exampleDao.getMovies() } returns listOf(getDbModel(), getDbModel())
        every { movieDbMapper.map(any()) } returns mockk()
        every { movieDbMapper.mapToDbModel(any()) } returns mockk()
    }

    @Test
    fun `getMoviePage() must call database getMoviePage() and map the result`() =
        runBlockingTest {
            // Given
            val id = 1L

            // When
            dbDataSource.getMovie(id)

            // Then
            coVerify(exactly = 1) {
                exampleDao.getMovie(id)
                movieDbMapper.map(any())
            }
        }

    @Test
    fun `saveMovie() must call database insertWithTimestamp()`() =
        runBlockingTest {
            // When
            dbDataSource.saveMovie(mockk())

            // Then
            coVerify(exactly = 1) {
                exampleDao.insertWithTimestamp(any())
            }
        }
}