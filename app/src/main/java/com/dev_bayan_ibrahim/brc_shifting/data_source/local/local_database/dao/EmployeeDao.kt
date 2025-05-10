package com.dev_bayan_ibrahim.brc_shifting.data_source.local.local_database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dev_bayan_ibrahim.brc_shifting.data_source.local.local_database.entity.EmployeeEntity
import com.dev_bayan_ibrahim.brc_shifting.util.WorkGroup
import kotlinx.coroutines.flow.Flow

@Dao
interface EmployeeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmployee(employee: EmployeeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmployeeList(employees: List<EmployeeEntity>)

    @Update
    suspend fun updateEmployee(employee: EmployeeEntity)

    @Update
    suspend fun updateEmployeeList(employees: List<EmployeeEntity>)

    @Delete
    suspend fun deleteEmployee(employee: EmployeeEntity)

    @Query("DELETE FROM ${EmployeeEntity.EMPLOYEE_TABLE} WHERE ${EmployeeEntity.EMPLOYEE_NUMBER} = :employeeNumber")
    suspend fun deleteEmployeeByNumber(employeeNumber: Int)

    @Query("DELETE FROM ${EmployeeEntity.EMPLOYEE_TABLE} WHERE ${EmployeeEntity.EMPLOYEE_NUMBER} IN (:numbers)")
    suspend fun deleteEmployees(numbers: Set<Int>)

    @Query("SELECT * FROM ${EmployeeEntity.EMPLOYEE_TABLE} WHERE ${EmployeeEntity.EMPLOYEE_NUMBER} = :employeeNumber")
    fun getEmployeeByNumber(employeeNumber: Int): Flow<EmployeeEntity?>

    @Query("SELECT * FROM ${EmployeeEntity.EMPLOYEE_TABLE} WHERE ${EmployeeEntity.EMPLOYEE_REMOTE_ID} = :remoteId")
    fun getEmployeeByRemoteId(remoteId: String): Flow<EmployeeEntity?>

    @Query("SELECT * FROM ${EmployeeEntity.EMPLOYEE_TABLE}")
    fun getAllEmployees(): Flow<List<EmployeeEntity>>

    @Query("SELECT * FROM ${EmployeeEntity.EMPLOYEE_TABLE} WHERE ${EmployeeEntity.EMPLOYEE_GROUP} = :group")
    fun getEmployeesByGroup(group: WorkGroup): Flow<List<EmployeeEntity>>
}