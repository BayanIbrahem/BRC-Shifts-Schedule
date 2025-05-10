package com.dev_bayan_ibrahim.brc_shifting.data_source.local.local_database.dao

import androidx.room.Dao

import androidx.room.*
import com.dev_bayan_ibrahim.brc_shifting.data_source.local.local_database.entity.DeductionEntity
import kotlinx.coroutines.flow.Flow
import java.time.Instant

@Dao
interface DeductionDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDeduction(deduction: DeductionEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDeductionList(deductions: List<DeductionEntity>)

    @Update
    suspend fun updateDeduction(deduction: DeductionEntity)

    @Update
    suspend fun updateDeductionList(deductions: List<DeductionEntity>)

    @Delete
    suspend fun deleteDeduction(deduction: DeductionEntity)

    @Query("DELETE FROM ${DeductionEntity.DEDUCTION_TABLE} WHERE ${DeductionEntity.DEDUCTION_ID} = :id")
    suspend fun deleteDeductionById(id: Long)

    @Query("DELETE FROM ${DeductionEntity.DEDUCTION_TABLE} WHERE ${DeductionEntity.DEDUCTION_EMPLOYEE_NUMBER} = :employeeNumber")
    suspend fun deleteDeductionByEmployeeNumber(employeeNumber: Int)

    @Query("DELETE FROM ${DeductionEntity.DEDUCTION_TABLE} WHERE ${DeductionEntity.DEDUCTION_YEAR} = :year AND (${DeductionEntity.DEDUCTION_MONTH} = :month OR :month IS NULL)")
    suspend fun deleteDeductionsByYearAndOptionalMonth(year: Int, month: Int?)

    @Query("SELECT * FROM ${DeductionEntity.DEDUCTION_TABLE} WHERE ${DeductionEntity.DEDUCTION_EMPLOYEE_NUMBER} = :employeeNumber ORDER BY ${DeductionEntity.DEDUCTION_YEAR} DESC, ${DeductionEntity.DEDUCTION_MONTH} DESC")
    fun getDeductionsByEmployeeNumber(employeeNumber: Int): Flow<List<DeductionEntity>>

    @Query("SELECT * FROM ${DeductionEntity.DEDUCTION_TABLE} WHERE ${DeductionEntity.DEDUCTION_ID} = :id")
    fun getDeductionById(id: Long): Flow<DeductionEntity?>

    @Query("SELECT * FROM ${DeductionEntity.DEDUCTION_TABLE} WHERE ${DeductionEntity.DEDUCTION_YEAR} = :year AND (${DeductionEntity.DEDUCTION_MONTH} = :month OR :month IS NULL)  ORDER BY ${DeductionEntity.DEDUCTION_YEAR} DESC, ${DeductionEntity.DEDUCTION_MONTH} DESC")
    fun getDeductionsByYearAndOptionalMonth(year: Int, month: Int?): Flow<List<DeductionEntity>>

    @Query("SELECT * FROM ${DeductionEntity.DEDUCTION_TABLE} WHERE ${DeductionEntity.DEDUCTION_YEAR} = :year AND (${DeductionEntity.DEDUCTION_MONTH} = :month OR :month IS NULL) AND ${DeductionEntity.DEDUCTION_EMPLOYEE_NUMBER} = :employeeNumber ORDER BY ${DeductionEntity.DEDUCTION_YEAR} DESC, ${DeductionEntity.DEDUCTION_MONTH} DESC")
    fun getDeductionsByYearAndOptionalMonthAndEmployeeNumber(year: Int, month: Int?, employeeNumber: Int): Flow<List<DeductionEntity>>
}