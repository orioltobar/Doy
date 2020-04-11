package com.napptilians.domain.usecases

import com.napptilians.commons.AppDispatchers
import com.napptilians.commons.Response
import com.napptilians.commons.error.ErrorModel
import com.napptilians.domain.models.user.UserModel
import com.napptilians.domain.repositories.DoyRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    appDispatchers: AppDispatchers,
    private val doyRepository: DoyRepository
) {

    private val ioDispatchers = appDispatchers.io

    suspend operator fun invoke(userUid: String): Response<UserModel, ErrorModel> =
        withContext(ioDispatchers) {
            doyRepository.getUser(userUid)
        }
}