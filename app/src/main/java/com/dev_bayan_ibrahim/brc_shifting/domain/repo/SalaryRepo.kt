package com.dev_bayan_ibrahim.brc_shifting.domain.repo

import com.dev_bayan_ibrahim.brc_shifting.domain.model.Employee
import com.dev_bayan_ibrahim.brc_shifting.domain.model.salary.EmployeeSalary
import kotlinx.coroutines.flow.Flow

interface SalaryRepo {
    fun getEmployeeSalaries(employeeNumber: Int): Flow<List<EmployeeSalary>>
    suspend fun getEmployeeRemoteSalary(employeeNumber: Int, currentMonth: Boolean = true): Result<EmployeeSalary>
    suspend fun deleteSalary(employeeNumber: Int, month: Int, year: Int): Result<Boolean>
}