package com.napptilians.domain.usecases

import com.napptilians.domain.repositories.ExampleRepository
import javax.inject.Inject

class GetMovieUseCaseFlow @Inject constructor(private val exampleRepository: ExampleRepository) {

    fun execute() = exampleRepository.getMovieFlow()
}