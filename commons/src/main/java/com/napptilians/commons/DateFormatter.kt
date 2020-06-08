package com.napptilians.commons

import org.threeten.bp.Instant
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object DateFormatter {

    /**
     * Given a [timestamp] returns the time current past time from that time. If the time from now
     * is lower than a minute it returns Xs, if lower than an hour it returns Xh.
     * Finally, if the time is greater than 24h it returns the date formatted as 01-01-1973.
     */
    fun format(timestamp: Long): String {
        val lastMessageSeconds = Instant.ofEpochMilli(timestamp).epochSecond
        val currentSeconds = Instant.now().epochSecond
        val difference = currentSeconds - lastMessageSeconds

        return if (difference < 60) {
            // SECONDS
            "${difference}s"
        } else if (difference >= 60 && difference < (60 * 60)) {
            // Minutes
            "${TimeUnit.SECONDS.toMinutes(difference)}m"
        } else if (difference >= (60 * 60) && difference < (60 * 60 * 24)) {
            // Hours
            "${TimeUnit.SECONDS.toHours(difference)}h"
        } else {
            // Date
            val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.US)
            formatter.format(Date(timestamp))
        }
    }
}
