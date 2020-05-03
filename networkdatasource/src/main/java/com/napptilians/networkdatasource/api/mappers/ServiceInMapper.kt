package com.napptilians.networkdatasource.api.mappers

import android.annotation.SuppressLint
import android.util.Log
import com.napptilians.commons.Mapper
import com.napptilians.domain.models.service.ServiceModel
import com.napptilians.networkdatasource.api.models.ServiceApiModel
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import javax.inject.Inject

class ServiceInMapper @Inject constructor() : Mapper<ServiceModel, ServiceApiModel> {

    override fun map(from: ServiceModel?): ServiceApiModel =
        ServiceApiModel(
            from?.serviceId ?: -1L,
            from?.categoryId ?: -1L,
            from?.name ?: "",
            from?.description ?: "",
            from?.image ?: "",
            from?.day ?: "",
            from?.spots ?: 1,
            from?.attendees ?: 0,
            from?.durationMin ?: 30,
            from?.hour ?: "",
            from?.ownerId ?: "",
            from?.ownerImage ?: "",
            null
        )

    fun map(from: ServiceApiModel): ServiceModel =
        ServiceModel(
            from.serviceId ?: -1L,
            from.categoryId ?: -1L,
            from.name ?: "",
            from.description ?: "",
            from.image ?: "",
            from.day ?: "",
            from.hour ?: "",
            parseDate(from),
            from.spots ?: 1,
            from.attendees ?: 0,
            from.durationMin ?: 30,
            from.ownerId ?: "",
            from.ownerImage ?: "",
            from.assistance?.equals("1") ?: false
        )

    private fun parseDate(model: ServiceApiModel): ZonedDateTime? {
        return if (model.day == null || model.hour == null) {
            null
        } else {
            // Hardcoded to Spanish time
            val dateString = "${model.day}T${model.hour}$TIMEZONE"
            try {
                ZonedDateTime.parse(dateString, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
            } catch (e: Exception) {
                Log.d(TAG, "There was an error parsing: $dateString")
                null
            }
        }
    }

    companion object {
        private const val TAG = "ServiceInMapper"
        private const val TIMEZONE = "+01:00"
    }
}

