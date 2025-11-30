package com.dev_bayan_ibrahim.brc_shifting.data_source.local.local_database.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dev_bayan_ibrahim.brc_shifting.data_source.local.local_database.converter.BonusTypeConverter
import com.dev_bayan_ibrahim.brc_shifting.data_source.local.local_database.converter.DayOffTypeConverter
import com.dev_bayan_ibrahim.brc_shifting.data_source.local.local_database.converter.InstantConverter
import com.dev_bayan_ibrahim.brc_shifting.data_source.local.local_database.converter.SimpleDateConverter
import com.dev_bayan_ibrahim.brc_shifting.data_source.local.local_database.converter.WorkGroupConverter
import com.dev_bayan_ibrahim.brc_shifting.data_source.local.local_database.dao.BonusDao
import com.dev_bayan_ibrahim.brc_shifting.data_source.local.local_database.dao.DayOffDao
import com.dev_bayan_ibrahim.brc_shifting.data_source.local.local_database.dao.DeductionDao
import com.dev_bayan_ibrahim.brc_shifting.data_source.local.local_database.dao.EmployeeDao
import com.dev_bayan_ibrahim.brc_shifting.data_source.local.local_database.dao.SalaryDao
import com.dev_bayan_ibrahim.brc_shifting.data_source.local.local_database.entity.BonusEntity
import com.dev_bayan_ibrahim.brc_shifting.data_source.local.local_database.entity.DayOffEntity
import com.dev_bayan_ibrahim.brc_shifting.data_source.local.local_database.entity.DeductionEntity
import com.dev_bayan_ibrahim.brc_shifting.data_source.local.local_database.entity.EmployeeEntity
import com.dev_bayan_ibrahim.brc_shifting.data_source.local.local_database.entity.SalaryEntity
import com.dev_bayan_ibrahim.brc_shifting.util.WORK_GROUP_KEY_A

@Database(
    entities = [
        BonusEntity::class,
        DayOffEntity::class,
        DeductionEntity::class,
        EmployeeEntity::class,
        SalaryEntity::class,
    ],
    version = 3,
    exportSchema = false
)
@TypeConverters(
    BonusTypeConverter::class,
    DayOffTypeConverter::class,
    InstantConverter::class,
    SimpleDateConverter::class,
    WorkGroupConverter::class,
)
abstract class BrcDatabase : RoomDatabase() {
    abstract fun getBonusDao(): BonusDao
    abstract fun getDayOffDao(): DayOffDao
    abstract fun getDeductionDao(): DeductionDao
    abstract fun getEmployeeDao(): EmployeeDao
    abstract fun getSalaryDao(): SalaryDao
}