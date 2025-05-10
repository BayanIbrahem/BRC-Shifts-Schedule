package com.dev_bayan_ibrahim.brc_shifting.domain.repo

import com.dev_bayan_ibrahim.brc_shifting.domain.model.Employee
import com.dev_bayan_ibrahim.brc_shifting.util.WorkGroup
import kotlinx.coroutines.flow.Flow

interface EmployeeRepo {
    /**
     * get employee from remote and if result successes update the current result
     * if the employee already exists then after success remote fetch, employee update date is set to now
     * if [employeeGroup] is not null then it will be used with the employ on success
     */
    suspend fun getRemoteEmployee(
        employNumber: Int,
        password: String,
        employeeGroup: WorkGroup? = null,
    ): Result<Employee>

    /**
     * add or update the employee (update only if [override] is true
     * return failure if employee exists and [override] is disabled
     */
    suspend fun addEmployee(employee: Employee, override: Boolean = false): Result<Employee>

    /**
     * delete employee,
     * @return result of ture if deleted and result of false if not exists
     * */
    suspend fun deleteEmployee(employeeNumber: Int): Result<Boolean>
    suspend fun deleteAllEmployees(): Result<Boolean>
    suspend fun deleteEmployees(numbers: Set<Int>): Result<Boolean>

    fun getEmployee(employeeNumber: Int): Flow<Employee?>

    fun getEmployee(remoteId: String): Flow<Employee?>

    fun getAllEmployees(): Flow<List<Employee>>

    fun getEmployees(group: WorkGroup): Flow<List<Employee>>
}