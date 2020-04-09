package com.napptilians.domain.usecases

import com.napptilians.commons.AppDispatchers
import com.napptilians.domain.repositories.ExampleRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    appDispatchers: AppDispatchers,
    private val exampleRepository: ExampleRepository
) {

    private val ioDispatcher = appDispatchers.io

    suspend operator fun invoke(email: String, password: String) = withContext(ioDispatcher) {
        exampleRepository.login(email, password)
    }
}