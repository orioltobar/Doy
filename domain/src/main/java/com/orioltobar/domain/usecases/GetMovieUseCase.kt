package com.orioltobar.domain.usecases

import com.orioltobar.domain.repositories.ExampleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetMovieUseCase @Inject constructor(private val exampleRepository: ExampleRepository) {

    suspend fun execute(id: Long) = withContext(Dispatchers.IO) { exampleRepository.getMovie(id) }
}