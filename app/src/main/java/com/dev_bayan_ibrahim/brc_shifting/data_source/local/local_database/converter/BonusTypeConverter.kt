package com.dev_bayan_ibrahim.brc_shifting.data_source.local.local_database.converter

import androidx.room.TypeConverter
import com.dev_bayan_ibrahim.brc_shifting.domain.model.BonusType

object BonusTypeConverter {
    @TypeConverter
    fun bonusTypeToKey(bonusType: BonusType): String = bonusType.key

    @TypeConverter
    fun keyToBonusType(key: String): BonusType = BonusType.ofKey(key)
}