package com.napptilians.domain.models.movie

import com.napptilians.domain.models.service.ServiceModel
import java.time.ZonedDateTime

data class EventsModel(
    var upcomingEvents: List<ServiceModel> = emptyList(),
    var pastEvents: List<ServiceModel> = emptyList()

)
