package com.napptilians.domain.usecases

import com.napptilians.commons.AppDispatchers
import com.napptilians.commons.Response
import com.napptilians.commons.error.ErrorModel
import com.napptilians.domain.repositories.DoyRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RecoverPasswordUseCase @Inject constructor(
    appDispatchers: AppDispatchers,
    private val doyRepository: DoyRepository
) {

    private val ioDispatcher = appDispatchers.io

    suspend operator fun invoke(email: String): Response<Unit?, ErrorModel> =
        withContext(ioDispatcher) {
            doyRepository.recoverPassword(email)
        }
}