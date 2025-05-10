package com.dev_bayan_ibrahim.brc_shifting.data_source.local.local_database.dao

import androidx.room.Dao

import androidx.room.*
import com.dev_bayan_ibrahim.brc_shifting.data_source.local.local_database.entity.DayOffEntity
import kotlinx.coroutines.flow.Flow
import java.time.Instant

@Dao
interface DayOffDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDayOff(dayOff: DayOffEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDayOffList(dayOffs: List<DayOffEntity>)

    @Update
    suspend fun updateDayOff(dayOff: DayOffEntity)

    @Update
    suspend fun updateDayOffList(dayOffs: List<DayOffEntity>)

    @Delete
    suspend fun deleteDayOff(dayOff: DayOffEntity)

    @Query("DELETE FROM ${DayOffEntity.DAY_OFF_TABLE} WHERE ${DayOffEntity.DAY_OFF_ID} = :id")
    suspend fun deleteDayOffById(id: Long)

    @Query("DELETE FROM ${DayOffEntity.DAY_OFF_TABLE} WHERE ${DayOffEntity.DAY_OFF_EMPLOYEE_NUMBER} = :employeeNumber")
    suspend fun deleteDayOffByEmployeeNumber(employeeNumber: Int)

    @Query("DELETE FROM ${DayOffEntity.DAY_OFF_TABLE} WHERE ${DayOffEntity.DAY_OFF_DATE} BETWEEN :startDate AND :endDate")
    suspend fun deleteDayOffsInDateRange(startDate: Int, endDate: Int)

    @Query("SELECT * FROM ${DayOffEntity.DAY_OFF_TABLE} WHERE ${DayOffEntity.DAY_OFF_EMPLOYEE_NUMBER} = :employeeNumber ORDER BY ${DayOffEntity.DAY_OFF_DATE} DESC")
    fun getDayOffsByEmployeeNumber(employeeNumber: Int): Flow<List<DayOffEntity>>

    @Query("SELECT * FROM ${DayOffEntity.DAY_OFF_TABLE} WHERE ${DayOffEntity.DAY_OFF_ID} = :id")
    fun getDayOffById(id: Long): Flow<DayOffEntity?>

    @Query("SELECT * FROM ${DayOffEntity.DAY_OFF_TABLE} WHERE ${DayOffEntity.DAY_OFF_DATE} BETWEEN :startDate AND :endDate ORDER BY ${DayOffEntity.DAY_OFF_DATE} DESC")
    fun getDayOffsInDateRange(startDate: Int, endDate: Int): Flow<List<DayOffEntity>>

    @Query("SELECT * FROM ${DayOffEntity.DAY_OFF_TABLE} WHERE ${DayOffEntity.DAY_OFF_DATE} BETWEEN :startDate AND :endDate AND ${DayOffEntity.DAY_OFF_EMPLOYEE_NUMBER} = :employeeNumber ORDER BY ${DayOffEntity.DAY_OFF_DATE} DESC")
    fun getDayOffsInDateRangeAndEmployeeNumber(startDate: Int, endDate: Int, employeeNumber: Int): Flow<List<DayOffEntity>>
}