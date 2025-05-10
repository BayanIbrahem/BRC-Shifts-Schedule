package com.dev_bayan_ibrahim.brc_shifting.data_source.local.local_database.converter

import androidx.room.TypeConverter
import com.dev_bayan_ibrahim.brc_shifting.domain.model.util.SimpleDate

object SimpleDateConverter {
    @TypeConverter
    fun simpleDateToEpoch(simpleDate: SimpleDate): Int = simpleDate.daysSinceEpoch

    @TypeConverter
    fun epochToSimpleDate(epoch: Int): SimpleDate = SimpleDate(epoch)
}