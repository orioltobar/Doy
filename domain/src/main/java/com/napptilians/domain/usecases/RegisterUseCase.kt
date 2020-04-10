package com.napptilians.domain.usecases

import com.napptilians.commons.AppDispatchers
import com.napptilians.commons.Response
import com.napptilians.commons.error.ErrorModel
import com.napptilians.domain.repositories.DoyRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    appDispatchers: AppDispatchers,
    private val doyRepository: DoyRepository
) {

    private val ioDispatcher = appDispatchers.io

    suspend operator fun invoke(
        name: String,
        email: String,
        password: String,
        token: String
    ): Response<Unit, ErrorModel> =
        withContext(ioDispatcher) {
            doyRepository.register(name, password, email, token)
        }
}