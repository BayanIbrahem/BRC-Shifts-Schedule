package com.dev_bayan_ibrahim.brc_shifting.data.repo

import com.dev_bayan_ibrahim.brc_shifting.data_source.local.LocalDataSource
import com.dev_bayan_ibrahim.brc_shifting.data_source.remote.RemoteDataSource
import com.dev_bayan_ibrahim.brc_shifting.domain.model.Employee
import com.dev_bayan_ibrahim.brc_shifting.domain.model.exception.EmployeeInsertDataConflictException
import com.dev_bayan_ibrahim.brc_shifting.domain.model.util.asResult
import com.dev_bayan_ibrahim.brc_shifting.domain.repo.EmployeeRepo
import com.dev_bayan_ibrahim.brc_shifting.util.WorkGroup
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock

class EmployeeRepoImpl(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
) : EmployeeRepo {
    override suspend fun getRemoteEmployee(
        employNumber: Int,
        password: String,
        employeeGroup: WorkGroup?,
    ): Result<Employee> {
        val result = remoteDataSource.getEmploy(employNumber, password)
        val oldEmployee = localDataSource.getEmployee(employNumber).asResult().firstOrNull()?.getOrNull()
        return result.fold(
            onFailure = {
                Result.failure(it)
            }, onSuccess = { employee ->
                val newEmployee =
                    employee.copy(
                        createdAt = oldEmployee?.createdAt ?: Clock.System.now(),
                        updatedAt = Clock.System.now(),
                        group = employeeGroup
                    )

                localDataSource.insertEmployee(newEmployee)
                Result.success(employee)
            }
        )
    }

    override suspend fun addEmployee(employee: Employee, override: Boolean): Result<Employee> {
        val oldEmployee = localDataSource.getEmployee(employee.employeeNumber).asResult().firstOrNull()?.getOrNull()
        if (oldEmployee != null && !override) return Result.failure(EmployeeInsertDataConflictException())
        return localDataSource.insertEmployee(employee).fold(
            onFailure = {
                Result.failure(it)
            },
            onSuccess = {
                Result.success(employee.copy(updatedAt = Clock.System.now()))
            }
        )

    }

    override suspend fun deleteEmployee(employeeNumber: Int): Result<Boolean> {
        val oldEmployee = localDataSource.getEmployee(employeeNumber).asResult().firstOrNull()?.getOrNull()
            ?: Result.success(false)

        return localDataSource.deleteEmployee(employeeNumber).fold(
            onFailure = {
                Result.failure(it)
            },
            onSuccess = {
                Result.success(true)
            }
        )
    }

    override suspend fun deleteAllEmployees(): Result<Boolean> {
        val allNumbers = localDataSource.getAllEmployees().map {
            it.map { it.employeeNumber }.toSet()
        }.firstOrNull() ?: return Result.success(false)
        return deleteEmployees(allNumbers)
    }

    override suspend fun deleteEmployees(numbers: Set<Int>): Result<Boolean> {
        return localDataSource.deleteEmployees(numbers).map { true }
    }

    override fun getEmployee(employeeNumber: Int): Flow<Employee?> {
        return localDataSource.getEmployee(employeeNumber)
    }

    override fun getEmployee(remoteId: String): Flow<Employee?> {
        return localDataSource.getEmployee(remoteId)
    }

    override fun getAllEmployees(): Flow<List<Employee>> {
        return localDataSource.getAllEmployees()
    }

    override fun getEmployees(group: WorkGroup): Flow<List<Employee>> {
        return localDataSource.getEmployees(group)
    }
}