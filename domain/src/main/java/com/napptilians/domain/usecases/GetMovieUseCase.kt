package com.napptilians.domain.usecases

import com.napptilians.commons.AppDispatchers
import com.napptilians.domain.repositories.DoyRepository
import javax.inject.Inject

class GetMovieUseCase @Inject constructor(
    appDispatchers: AppDispatchers,
    private val doyRepository: DoyRepository
) {

    private val ioDispatcher = appDispatchers.io

    suspend fun execute(id: Long) = Unit
}
