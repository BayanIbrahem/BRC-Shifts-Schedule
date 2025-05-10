package com.dev_bayan_ibrahim.brc_shifting.data.repo

import com.dev_bayan_ibrahim.brc_shifting.data_source.local.LocalDataSource
import com.dev_bayan_ibrahim.brc_shifting.data_source.remote.RemoteDataSource
import com.dev_bayan_ibrahim.brc_shifting.data_source.remote.endpoint.EndPointMonthVariance
import com.dev_bayan_ibrahim.brc_shifting.domain.model.Employee
import com.dev_bayan_ibrahim.brc_shifting.domain.model.exception.SalaryDeleteDataConflictException
import com.dev_bayan_ibrahim.brc_shifting.domain.model.exception.SalaryInsertDataConflictException
import com.dev_bayan_ibrahim.brc_shifting.domain.model.salary.EmployeeSalary
import com.dev_bayan_ibrahim.brc_shifting.domain.model.util.asResult
import com.dev_bayan_ibrahim.brc_shifting.domain.repo.SalaryRepo
import com.dev_bayan_ibrahim.brc_shifting.util.months
import com.dev_bayan_ibrahim.brc_shifting.util.now
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toInstant

class SalaryRepoImpl(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
) : SalaryRepo {
    override suspend fun getEmployee(employeeNumber: Int): Employee? {
        return localDataSource.getEmployee(employeeNumber).asResult().firstOrNull()?.getOrNull()
    }

    override fun getEmployeeSalaries(employeeNumber: Int): Flow<List<EmployeeSalary>> {
        return localDataSource.getEmployeeSalaries(employeeNumber)
    }

    override suspend fun getEmployeeRemoteSalary(
        employeeNumber: Int,
        currentMonth: Boolean,
    ): Result<EmployeeSalary> = kotlin.runCatching {
        val result = remoteDataSource.getSalary(
            employNumber = employeeNumber,
            variance = if (currentMonth) EndPointMonthVariance.ThisMonth else EndPointMonthVariance.PreviousMonth
        )
        result.fold(
            onSuccess = {
                val now = LocalDateTime.now()
                val nowInstant = now.toInstant(TimeZone.currentSystemDefault())
                val date = now.date.let {
                    if (currentMonth) it else it.minus(1.months)
                }
                val monthNumber = date.monthNumber
                val year = date.year

                val oldSalary = localDataSource.getSalariesByYearAndOptionalMonthAndEmployeeNumber(
                    year = year,
                    month = monthNumber,
                    employeeNumber = employeeNumber
                ).asResult().firstOrNull()?.getOrNull()?.firstOrNull()
                var employeeSalary = EmployeeSalary(
                    employeeNumber = employeeNumber,
                    createdAt = oldSalary?.createdAt ?: nowInstant,
                    updatedAt = nowInstant,
                    id = oldSalary?.id,
                    monthNumber = monthNumber,
                    year = year,
                    salary = it
                )
                val id = if (oldSalary == null) {
                    localDataSource.insertSalary(employeeSalary).getOrThrow()
                } else {
                    localDataSource.updateSalary(employeeSalary)
                    oldSalary.id!!
                }
                // we refetch the salary for its id
                employeeSalary = localDataSource.getSalary(id).first()!!
                employeeSalary
            },
            onFailure = {
                throw SalaryInsertDataConflictException(it.message)
            }
        )
    }

    override suspend fun deleteSalary(
        employeeNumber: Int,
        month: Int,
        year: Int,
    ): Result<Boolean> = runCatching {
        val oldSalary = localDataSource.getSalariesByYearAndOptionalMonthAndEmployeeNumber(
            year = year,
            month = month,
            employeeNumber = employeeNumber
        ).asResult().firstOrNull()?.getOrNull()?.firstOrNull() ?: throw SalaryDeleteDataConflictException()
        val id = oldSalary.id ?: throw SalaryDeleteDataConflictException()
        localDataSource.deleteSalaryById(id)
        true
    }
}