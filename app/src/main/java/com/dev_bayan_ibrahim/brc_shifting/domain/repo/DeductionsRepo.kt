package com.dev_bayan_ibrahim.brc_shifting.domain.repo

import com.dev_bayan_ibrahim.brc_shifting.data_source.remote.endpoint.EndPointMonthVariance
import com.dev_bayan_ibrahim.brc_shifting.domain.model.EmployeeDeduction
import com.dev_bayan_ibrahim.brc_shifting.domain.model.MonthDeductions
import kotlinx.coroutines.flow.Flow

interface DeductionsRepo {
    suspend fun getRemoteDeductions(
        employeeNumber: Int,
        variances: Collection<EndPointMonthVariance> = EndPointMonthVariance.entries
    ): Map<EndPointMonthVariance, Result<List<EmployeeDeduction>>>

    fun getEmployeeDeductions(
        employeeNumber: Int,
        startMonth: Int,
        startYear: Int,
        endMonth: Int,
        endYear: Int,
    ): Flow<List<MonthDeductions>>
    fun getAvailableDeductionsForEmployee(employeeNumber: Int): Flow<Set<Pair<Int, Int>>>
}