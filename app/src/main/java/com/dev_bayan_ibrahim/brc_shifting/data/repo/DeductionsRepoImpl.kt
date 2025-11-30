package com.dev_bayan_ibrahim.brc_shifting.data.repo

import com.dev_bayan_ibrahim.brc_shifting.data_source.local.LocalDataSource
import com.dev_bayan_ibrahim.brc_shifting.data_source.remote.RemoteDataSource
import com.dev_bayan_ibrahim.brc_shifting.data_source.remote.endpoint.EndPointMonthVariance
import com.dev_bayan_ibrahim.brc_shifting.domain.model.EmployeeDeduction
import com.dev_bayan_ibrahim.brc_shifting.domain.model.MonthDeductions
import com.dev_bayan_ibrahim.brc_shifting.domain.model.toEmployeeDeductions
import com.dev_bayan_ibrahim.brc_shifting.domain.repo.DeductionsRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DeductionsRepoImpl(
    val localDataSource: LocalDataSource,
    val remoteDataSource: RemoteDataSource,
) : DeductionsRepo {
    override fun getEmployeeDeductions(
        employeeNumber: Int,
        startMonth: Int,
        startYear: Int,
        endMonth: Int,
        endYear: Int,
    ): Flow<List<MonthDeductions>> = localDataSource.getDeductionsByEmployeeNumber(employeeNumber).map {
        it.getOrNull()?.let { deductions ->
            val validDateRange = startYear * 12 + startMonth..endYear * 12 + endMonth
            deductions.mapNotNull {
                if ((it.year * 12 + it.monthNumber) in validDateRange) {
                    it
                } else {
                    null
                }
            }.groupBy {
                Pair(it.year, it.monthNumber)
            }.map { (date, deductions) ->
                MonthDeductions(
                    employeeNumber = employeeNumber,
                    year = date.first,
                    monthNumber = date.second,
                    deductions = deductions
                )
            }
        } ?: emptyList()
    }

    /**
     * get and refresh deductions
     */
    override suspend fun getRemoteDeductions(
        employeeNumber: Int,
        variances: Collection<EndPointMonthVariance>,
    ): Map<EndPointMonthVariance, Result<List<EmployeeDeduction>>> {
        return variances.associateWith { variance ->
            val result = remoteDataSource.getDeductions(
                employNumber = employeeNumber,
                variance = variance
            )
            result.map { monthlyDeduction ->
                localDataSource.withTransaction {
                    localDataSource.deleteDeductionYearAndMonthAndEmployeeNumber(
                        employeeNumber = employeeNumber,
                        year = monthlyDeduction.year,
                        month = monthlyDeduction.monthNumber
                    )
                    val deductions = monthlyDeduction.toEmployeeDeductions()
                    localDataSource.insertDeductionList(deductions)
                    deductions
                }
            }
        }
    }

    override fun getAvailableDeductionsForEmployee(employeeNumber: Int): Flow<Set<Pair<Int, Int>>> {
        return localDataSource.getDeductionsByEmployeeNumber(employeeNumber).map {
            it.map {
                it.map {
                    it.year to it.monthNumber
                }.toSet()
            }.getOrNull() ?: emptySet()
        }
    }
}