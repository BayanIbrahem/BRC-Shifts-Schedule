package com.dev_bayan_ibrahim.brc_shifting.data_source.local.local_database.converter

import androidx.room.TypeConverter
import com.dev_bayan_ibrahim.brc_shifting.domain.model.DayOffType

object DayOffTypeConverter {
    @TypeConverter
    fun dayOffTypeToKey(dayOffType: DayOffType): String = dayOffType.key

    @TypeConverter
    fun keyToBonusType(key: String): DayOffType = DayOffType.ofKey(key) ?: DayOffType.Standard
}