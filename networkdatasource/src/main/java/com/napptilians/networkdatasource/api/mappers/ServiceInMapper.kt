package com.napptilians.networkdatasource.api.mappers

import android.util.Log
import com.napptilians.commons.Mapper
import com.napptilians.domain.models.service.ServiceModel
import com.napptilians.networkdatasource.api.models.ServiceApiModel
import org.threeten.bp.ZoneId
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
            from?.hour ?: "",
            convertDateToUtc(from?.date),
            from?.spots ?: 1,
            from?.attendees ?: 0,
            from?.durationMin ?: 30,
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
            parseDate(from.day, from.hour),
            from.spots ?: 1,
            from.attendees ?: 0,
            from.durationMin ?: 30,
            from.ownerId ?: "",
            from.ownerImage ?: "",
            from.assistance?.equals("1") ?: false
        )

    private fun convertDateToUtc(date: ZonedDateTime?): String? {
        return date?.toOffsetDateTime()?.atZoneSameInstant(ZoneId.of(UTC_REGION))
            ?.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    }

    private fun parseDate(day: String?, hour: String?): ZonedDateTime? {
        return if (day == null || hour == null) {
            null
        } else {
            // TODO: Use the date field directly once it's in the backend
            val dateString = "${day}T$hour$TIMEZONE"
            try {
                ZonedDateTime.parse(dateString, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                    .toOffsetDateTime().atZoneSameInstant(ZoneId.systemDefault())
            } catch (e: Exception) {
                Log.d(TAG, "There was an error parsing: $dateString")
                null
            }
        }
    }

    companion object {
        private const val TAG = "ServiceInMapper"
        private const val TIMEZONE = "Z"
        private const val UTC_REGION = "Europe/London"
    }
}

