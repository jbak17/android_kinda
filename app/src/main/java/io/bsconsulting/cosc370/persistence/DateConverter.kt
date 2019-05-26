package io.bsconsulting.cosc370.persistence

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

class DateConverter {

    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDateTime? {
         return value?.let {
            LocalDateTime.ofInstant(Instant.ofEpochMilli(value), TimeZone.getDefault().toZoneId())
        }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime): Long = date.toEpochSecond(ZoneOffset.UTC);

}
