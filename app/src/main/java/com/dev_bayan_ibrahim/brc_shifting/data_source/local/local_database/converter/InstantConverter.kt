package com.dev_bayan_ibrahim.brc_shifting.data_source.local.local_database.converter

import androidx.room.TypeConverter
import kotlinx.datetime.Instant

object InstantConverter {
    @TypeConverter
    fun instantToEpoch(instant: Instant): Long = instant.toEpochMilliseconds()
    @TypeConverter
    fun epochToInstant(epoch: Long): Instant = Instant.fromEpochMilliseconds(epoch)
}