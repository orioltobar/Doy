package com.napptilians.domain.usecases

import android.annotation.SuppressLint
import com.napptilians.commons.AppDispatchers
import com.napptilians.commons.Response
import com.napptilians.commons.Success
import com.napptilians.commons.error.ErrorModel
import com.napptilians.commons.flatMap
import com.napptilians.domain.models.service.ServiceModel
import com.napptilians.domain.repositories.DoyRepository
import kotlinx.coroutines.withContext
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import javax.inject.Inject

class GetMyServicesUseCase @Inject constructor(
    appDispatchers: AppDispatchers,
    private val doyRepository: DoyRepository
) {
    private val ioDispatcher = appDispatchers.io

    @SuppressLint("NewApi")
    suspend fun execute(
        uid: String? = null
    ): Response<Map<String, List<ServiceModel>>, ErrorModel> {
        val myUpcomingEvents = mutableListOf<ServiceModel>()
        val myPastEvents = mutableListOf<ServiceModel>()
        return withContext(ioDispatcher) {
            val request = doyRepository.getMyServices(uid)
            request.flatMap { serviceList ->
                serviceList.map { service ->
                    service.date?.let {
                        if (it >= Instant.now().atZone(ZoneId.systemDefault())) {
                            myUpcomingEvents.add(service)
                        } else {
                            myPastEvents.add(service)
                        }
                    }
                }
                Success(mapOf(UPCOMING to myUpcomingEvents, PAST to myPastEvents))
            }
        }
    }

    companion object {
        const val UPCOMING = "upcoming"
        const val PAST = "past"
    }
}

