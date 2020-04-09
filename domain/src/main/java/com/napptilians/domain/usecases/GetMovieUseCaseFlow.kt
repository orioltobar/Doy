package com.napptilians.domain.usecases

import com.napptilians.domain.repositories.DoyRepository
import javax.inject.Inject

class GetMovieUseCaseFlow @Inject constructor(private val doyRepository: DoyRepository) {

    fun execute() = Unit
}
