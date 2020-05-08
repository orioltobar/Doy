package com.napptilians.doy.util

import android.content.Context
import com.napptilians.doy.R
import java.util.regex.Pattern

class HourFormatter {

    fun formatHour(context: Context?, numberOfMinutes: Int): String {
        val hours = numberOfMinutes / 60
        val minutes = numberOfMinutes % 60
        return when {
            hours == 0 -> {
                context?.resources?.getString(R.string.minutes, minutes).toString()
            }
            minutes == 0 -> {
                context?.resources?.getQuantityString(
                    R.plurals.hours,
                    hours,
                    hours
                ).toString()
            }
            else -> {
                context?.resources?.getString(R.string.hours_and_minutes, hours, minutes).toString()
            }
        }
    }
}
