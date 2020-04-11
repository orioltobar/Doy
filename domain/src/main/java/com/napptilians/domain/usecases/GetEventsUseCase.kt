package com.napptilians.domain.usecases

import android.annotation.SuppressLint
import com.napptilians.commons.AppDispatchers
import com.napptilians.commons.Response
import com.napptilians.commons.Success
import com.napptilians.commons.error.ErrorModel
import com.napptilians.commons.flatMap
import com.napptilians.domain.models.movie.ServiceModel
import com.napptilians.domain.repositories.DoyRepository
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.ZoneId
import javax.inject.Inject

class GetEventsUseCase @Inject constructor(
    appDispatchers: AppDispatchers,
    private val doyRepository: DoyRepository
) {
    private val ioDispatcher = appDispatchers.io

    @SuppressLint("NewApi")
    suspend fun execute(
        categoryIds: List<Long> = emptyList(),
        serviceId: Long? = null,
        uid: String? = null
    ): Response<Map<String, List<ServiceModel>>, ErrorModel> {
        val upcomingEvents = mutableListOf<ServiceModel>()
        val pastEvents = mutableListOf<ServiceModel>()
        return withContext(ioDispatcher) {
            val request = doyRepository.getServices(categoryIds, serviceId, uid)
            request.flatMap { serviceList ->
                serviceList.map { service ->
                    service.date?.let {
                        if (service.date >= Instant.now().atZone(ZoneId.of(TIMEZONE))) {
                            upcomingEvents.add(service)
                        } else {
                            pastEvents.add(service)
                        }
                    }
                }
                Success(mapOf("upcoming" to upcomingEvents, "past" to pastEvents))
            }
        }
    }

    companion object {
        private const val TIMEZONE = "Europe/Madrid"
    }
}

