package com.orioltobar.diskdatasource.dao

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.orioltobar.diskdatasource.DbMocks.getDbModel
import com.orioltobar.diskdatasource.Helper
import com.orioltobar.diskdatasource.database.AppDataBase
import com.orioltobar.diskdatasource.models.MovieDbModel
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ExampleDaoTest {

    private lateinit var appDataBase: AppDataBase

    @Before
    fun setup() {
        appDataBase = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            AppDataBase::class.java
        ).build()
    }

    @After
    fun tearDown() {
        appDataBase.close()
    }

    @Test
    fun insertMovieWithTimeStampAndGetTheResultTest() {
        Helper.testBlocking {
            val dbModel = getDbModel()

            appDataBase.movieDao().insertWithTimestamp(dbModel)
            val result = appDataBase.movieDao().getMovie(dbModel.id)

            assertEquals(dbModel.id, result?.id)
            assertEquals(dbModel.mainGenreId, result?.mainGenreId)
        }
    }

    @Test
    fun deleteMovieWithTimeStampAndGetTheResultTest() {
        Helper.testBlocking {
            val dbModel = getDbModel()

            appDataBase.movieDao().insertWithTimestamp(dbModel)

            appDataBase.movieDao().deleteMovie(dbModel)
            val result = appDataBase.movieDao().getMovie(dbModel.id)

            assertNull(result)
            assertNull(result?.mainGenreId)
        }
    }

    @Test
    fun getMovieListByGenreIdTest() {
        Helper.testBlocking {
            val movie1 = getDbModel()
            val movie2 = getDbModel(id = 2)
            val movie3 = getDbModel(id = 3)

            appDataBase.movieDao().insertWithTimestamp(movie1)
            appDataBase.movieDao().insertWithTimestamp(movie2)
            appDataBase.movieDao().insertWithTimestamp(movie3)

            val result = appDataBase.movieDao().getMoviesByGenre(movie1.mainGenreId)

            result.forEach {
                assertEquals(12, it.mainGenreId)
            }
            assertEquals(3, result.size)
        }
    }

    @Test
    fun getMovieListTest() {
        Helper.testBlocking {
            val movie1 = getDbModel()
            val movie2 = getDbModel(id = 2)
            val movie3 = getDbModel(mainGenreId= 29, id = 3)
            val movie4 = getDbModel(id = 4)

            appDataBase.movieDao().insertWithTimestamp(movie1)
            appDataBase.movieDao().insertWithTimestamp(movie2)
            appDataBase.movieDao().insertWithTimestamp(movie3)
            appDataBase.movieDao().insertWithTimestamp(movie4)

            val result = appDataBase.movieDao().getMovies()

            assertEquals(4, result.size)
        }
    }
}