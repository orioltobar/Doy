package com.napptilians.domain.models.movie

import java.time.ZonedDateTime

data class EventsModel(
    var upcomingEvents: List<ServiceModel> = emptyList(),
    var pastEvents: List<ServiceModel> = emptyList()

)
