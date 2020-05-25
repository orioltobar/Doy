package com.napptilians.domain.usecases

import com.google.firebase.auth.AuthCredential
import com.napptilians.commons.AppDispatchers
import com.napptilians.domain.repositories.DoyRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoginWithGoogleUseCase @Inject constructor(
    appDispatchers: AppDispatchers,
    private val doyRepository: DoyRepository
) {

    private val ioDispatcher = appDispatchers.io

    suspend operator fun invoke(credential: AuthCredential, idToken: String) = withContext(ioDispatcher) {
        doyRepository.loginWithGoogle(credential, idToken)
    }
}