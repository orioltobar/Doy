package com.orioltobar.diskdatasource

import androidx.room.TypeConverter
import java.util.*

/**
 * Utility class used to convert data that SQL don't know how to serialize.
 */
class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromList(from: List<Int>): String = from.joinToString(separator = ",")

    @TypeConverter
    fun toList(from: String): List<Int> = from.takeIf { it.isNotEmpty() }?.split(",")?.map { it.toInt() } ?: emptyList()
}