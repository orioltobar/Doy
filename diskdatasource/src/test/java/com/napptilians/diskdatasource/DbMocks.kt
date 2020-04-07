package com.napptilians.diskdatasource

import com.napptilians.diskdatasource.models.MovieDbModel

object DbMocks {

    fun getDbModel(mainGenreId: Int = 12, id: Long = 1L) = MovieDbModel(
        id, mainGenreId, "Test",
        "Test", 1.0F, 1, true,
        "/front", false, "/back",
        "en", listOf(12, 34), 5.0F,
        "overview", "1978", 12345L
    )
}