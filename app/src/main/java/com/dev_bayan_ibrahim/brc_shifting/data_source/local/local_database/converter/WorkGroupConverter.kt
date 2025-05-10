package com.dev_bayan_ibrahim.brc_shifting.data_source.local.local_database.converter

import androidx.room.TypeConverter
import com.dev_bayan_ibrahim.brc_shifting.util.WorkGroup

object WorkGroupConverter {
    @TypeConverter
    fun workGroupToKey(workGroup: WorkGroup): String = workGroup.key

    @TypeConverter
    fun keyToWorkGroup(key: String): WorkGroup = WorkGroup(key)
}