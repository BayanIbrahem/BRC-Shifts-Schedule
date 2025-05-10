package com.dev_bayan_ibrahim.brc_shifting.data_source.local.local_database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dev_bayan_ibrahim.brc_shifting.data_source.local.local_database.entity.SALARY_EMPLOYEE_NUMBER
import com.dev_bayan_ibrahim.brc_shifting.data_source.local.local_database.entity.SALARY_ID
import com.dev_bayan_ibrahim.brc_shifting.data_source.local.local_database.entity.SALARY_MONTH_NUMBER
import com.dev_bayan_ibrahim.brc_shifting.data_source.local.local_database.entity.SALARY_TABLE
import com.dev_bayan_ibrahim.brc_shifting.data_source.local.local_database.entity.SALARY_YEAR
import com.dev_bayan_ibrahim.brc_shifting.data_source.local.local_database.entity.SalaryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SalaryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSalary(salary: SalaryEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSalaryList(salaries: List<SalaryEntity>)

    @Update
    suspend fun updateSalary(salary: SalaryEntity)

    @Update
    suspend fun updateSalaryList(salaries: List<SalaryEntity>)

    @Delete
    suspend fun deleteSalary(salary: SalaryEntity)

    @Query("DELETE FROM $SALARY_TABLE WHERE $SALARY_ID = :id")
    suspend fun deleteSalaryById(id: Long)

    @Query("DELETE FROM $SALARY_TABLE WHERE $SALARY_EMPLOYEE_NUMBER = :employeeNumber")
    suspend fun deleteSalaryByEmployeeNumber(employeeNumber: Int)

    @Query("DELETE FROM $SALARY_TABLE WHERE $SALARY_YEAR = :year AND ($SALARY_MONTH_NUMBER = :month OR :month IS NULL) AND $SALARY_EMPLOYEE_NUMBER = :employeeNumber")
    suspend fun deleteSalariesByYearAndOptionalMonthAndEmployeeNumber(year: Int, month: Int?, employeeNumber: Int)

    @Query("SELECT * FROM $SALARY_TABLE WHERE $SALARY_EMPLOYEE_NUMBER = :employeeNumber")
    fun getSalariesByEmployeeNumber(employeeNumber: Int): Flow<List<SalaryEntity>>

    @Query("SELECT * FROM $SALARY_TABLE WHERE $SALARY_ID = :id")
    fun getSalaryById(id: Long): Flow<SalaryEntity?>

    @Query("SELECT * FROM $SALARY_TABLE WHERE $SALARY_YEAR = :year AND ($SALARY_MONTH_NUMBER = :month OR :month IS NULL)")
    fun getSalariesByYearAndOptionalMonth(year: Int, month: Int?): Flow<List<SalaryEntity>>

    @Query("SELECT * FROM $SALARY_TABLE WHERE $SALARY_YEAR = :year AND ($SALARY_MONTH_NUMBER = :month OR :month IS NULL) AND $SALARY_EMPLOYEE_NUMBER = :employeeNumber")
    fun getSalariesByYearAndOptionalMonthAndEmployeeNumber(year: Int, month: Int?, employeeNumber: Int): Flow<List<SalaryEntity>>
}