package com.napptilians.domain.usecases

import com.napptilians.commons.AppDispatchers
import com.napptilians.domain.repositories.DoyRepository
import javax.inject.Inject
import kotlinx.coroutines.withContext

class GetCategoriesUseCase @Inject constructor(
    appDispatchers: AppDispatchers,
    private val doyRepository: DoyRepository
) {
    private val ioDispatcher = appDispatchers.io

    suspend fun execute(categoryIds: List<Long> = emptyList(), lang: String = "ca") =
        withContext(ioDispatcher) { doyRepository.getCategories(categoryIds, lang) }
}
