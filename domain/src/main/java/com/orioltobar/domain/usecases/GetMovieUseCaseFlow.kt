package com.orioltobar.domain.usecases

import com.orioltobar.domain.repositories.ExampleRepository
import javax.inject.Inject

class GetMovieUseCaseFlow @Inject constructor(private val exampleRepository: ExampleRepository) {

    fun execute() = exampleRepository.getMovieFlow()
}