package com.dev_bayan_ibrahim.brc_shifting.data_source.local.local_database.dao

import androidx.room.Dao

import androidx.room.*
import com.dev_bayan_ibrahim.brc_shifting.data_source.local.local_database.entity.BonusEntity
import kotlinx.coroutines.flow.Flow
import java.time.Instant

@Dao
interface BonusDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBonus(bonus: BonusEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBonusList(bonuses: List<BonusEntity>)

    @Update
    suspend fun updateBonus(bonus: BonusEntity)

    @Update
    suspend fun updateBonusList(bonuses: List<BonusEntity>)

    @Delete
    suspend fun deleteBonus(bonus: BonusEntity)

    @Query("DELETE FROM ${BonusEntity.BONUS_TABLE} WHERE ${BonusEntity.BONUS_ID} = :id")
    suspend fun deleteBonusById(id: Long)

    @Query("DELETE FROM ${BonusEntity.BONUS_TABLE} WHERE ${BonusEntity.BONUS_EMPLOYEE_NUMBER} = :employeeNumber")
    suspend fun deleteBonusByEmployeeNumber(employeeNumber: Int)

    @Query("DELETE FROM ${BonusEntity.BONUS_TABLE} WHERE ${BonusEntity.BONUS_DATE} BETWEEN :startDate AND :endDate ")
    suspend fun deleteBonusesInDateRange(startDate: Int, endDate: Int)

    @Query("SELECT * FROM ${BonusEntity.BONUS_TABLE} WHERE ${BonusEntity.BONUS_EMPLOYEE_NUMBER} = :employeeNumber ORDER BY ${BonusEntity.BONUS_DATE} DESC")
    fun getBonusesByEmployeeNumber(employeeNumber: Int): Flow<List<BonusEntity>>

    @Query("SELECT * FROM ${BonusEntity.BONUS_TABLE} WHERE ${BonusEntity.BONUS_ID} = :id")
    fun getBonusById(id: Long): Flow<BonusEntity?>

    @Query("SELECT * FROM ${BonusEntity.BONUS_TABLE} WHERE ${BonusEntity.BONUS_DATE} BETWEEN :startDate AND :endDate ORDER BY ${BonusEntity.BONUS_DATE} DESC")
    fun getBonusesInDateRange(startDate: Int, endDate: Int): Flow<List<BonusEntity>>

    @Query("SELECT * FROM ${BonusEntity.BONUS_TABLE} WHERE ${BonusEntity.BONUS_DATE} BETWEEN :startDate AND :endDate AND ${BonusEntity.BONUS_EMPLOYEE_NUMBER} = :employeeNumber ORDER BY ${BonusEntity.BONUS_DATE} DESC")
    fun getBonusesInDateRangeAndEmployeeNumber(startDate: Int, endDate: Int, employeeNumber: Int): Flow<List<BonusEntity>>
}