package com.dev_bayan_ibrahim.brc_shifting.data_source.local

import androidx.room.withTransaction
import com.dev_bayan_ibrahim.brc_shifting.data_source.local.local_database.dao.BonusDao
import com.dev_bayan_ibrahim.brc_shifting.data_source.local.local_database.dao.DayOffDao
import com.dev_bayan_ibrahim.brc_shifting.data_source.local.local_database.dao.DeductionDao
import com.dev_bayan_ibrahim.brc_shifting.data_source.local.local_database.dao.EmployeeDao
import com.dev_bayan_ibrahim.brc_shifting.data_source.local.local_database.dao.SalaryDao
import com.dev_bayan_ibrahim.brc_shifting.data_source.local.local_database.db.BrcDatabase
import com.dev_bayan_ibrahim.brc_shifting.data_source.local.local_database.entity.toBonus
import com.dev_bayan_ibrahim.brc_shifting.data_source.local.local_database.entity.toDayOff
import com.dev_bayan_ibrahim.brc_shifting.data_source.local.local_database.entity.toEmployee
import com.dev_bayan_ibrahim.brc_shifting.data_source.local.local_database.entity.toEmployeeDeduction
import com.dev_bayan_ibrahim.brc_shifting.data_source.local.local_database.entity.toEntity
import com.dev_bayan_ibrahim.brc_shifting.data_source.local.local_database.entity.toSalary
import com.dev_bayan_ibrahim.brc_shifting.data_source.local.local_database.entity.toSalaryEntity
import com.dev_bayan_ibrahim.brc_shifting.domain.model.Bonus
import com.dev_bayan_ibrahim.brc_shifting.domain.model.DayOff
import com.dev_bayan_ibrahim.brc_shifting.domain.model.Employee
import com.dev_bayan_ibrahim.brc_shifting.domain.model.EmployeeDeduction
import com.dev_bayan_ibrahim.brc_shifting.domain.model.exception.NoDataException
import com.dev_bayan_ibrahim.brc_shifting.domain.model.salary.EmployeeSalary
import com.dev_bayan_ibrahim.brc_shifting.domain.model.util.SimpleDate
import com.dev_bayan_ibrahim.brc_shifting.util.WorkGroup
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant

class LocalDataSource(private val db: BrcDatabase) {
    suspend fun <R> withTransaction(body: suspend () -> R): R {
        return db.withTransaction(body)
    }
    private val bonusDao = db.getBonusDao()
    private val dayOffDao = db.getDayOffDao()
    private val deductionDao = db.getDeductionDao()
    private val employeeDao = db.getEmployeeDao()
    private val salaryDao = db.getSalaryDao()

    suspend fun insertBonus(bonus: Bonus): Result<Long> = Result.success(bonusDao.insertBonus(bonus.toEntity()))

    /**
     * insert or replace
     */
    suspend fun insertBonusList(bonuses: List<Bonus>): List<Long> = bonusDao.insertBonusList(bonuses.map { it.toEntity() })

    suspend fun updateBonus(bonus: Bonus): Result<Unit> = Result.success(bonusDao.updateBonus(bonus.toEntity()))

    suspend fun updateBonusList(bonuses: List<Bonus>): Result<Unit> = Result.success(bonusDao.updateBonusList(bonuses.map { it.toEntity() }))

    suspend fun deleteBonus(bonus: Bonus): Result<Unit> = Result.success(bonusDao.deleteBonus(bonus.toEntity()))

    suspend fun deleteBonus(id: Long): Result<Unit> = Result.success(bonusDao.deleteBonusById(id))

    suspend fun deleteBonus(ids: Set<Long>): Result<Unit> = Result.success(bonusDao.deleteBonusListById(ids))

    suspend fun deleteBonus(employeeNumber: Int): Result<Unit> = Result.success(bonusDao.deleteBonusByEmployeeNumber(employeeNumber))

    suspend fun deleteBonusesInDateRange(startDate: SimpleDate, endDate: SimpleDate): Result<Unit> =
        Result.success(bonusDao.deleteBonusesInDateRange(startDate.daysSinceEpoch, endDate.daysSinceEpoch))

    fun getEmployeeBonus(employeeNumber: Int): Flow<List<Bonus>> = bonusDao.getEmployeeBonus(employeeNumber).map { list ->
        list.map { it.toBonus() }
    }

    fun getBonusById(id: Long): Flow<Result<Bonus>> = bonusDao.getBonusById(id).map { bonus ->
        bonus?.let { Result.success(it.toBonus()) } ?: Result.failure(NoDataException("No bonus found with id $id"))
    }.catch { emit(Result.failure(it)) }

    fun getEmployeeBonusLastFetch(employeeNumber: Int): Flow<Instant?> = bonusDao.getEmployeeBonusLastFetch(employeeNumber)

    fun getBonusesInDateRange(startDate: SimpleDate, endDate: SimpleDate): Flow<Result<List<Bonus>>> =
        bonusDao.getBonusesInDateRange(startDate.daysSinceEpoch, endDate.daysSinceEpoch).map { list ->
            if (list.isEmpty()) {
                Result.failure(NoDataException("No bonuses found in date range $startDate to $endDate"))
            } else {
                Result.success(list.map { it.toBonus() })
            }
        }.catch { emit(Result.failure(it)) }

    fun getBonusesInDateRangeAndEmployeeNumber(startDate: SimpleDate, endDate: SimpleDate, employeeNumber: Int): Flow<Result<List<Bonus>>> =
        bonusDao.getBonusesInDateRangeAndEmployeeNumber(startDate.daysSinceEpoch, endDate.daysSinceEpoch, employeeNumber).map { list ->
            if (list.isEmpty()) {
                Result.failure(NoDataException("No bonuses found in date range $startDate to $endDate for employee number $employeeNumber"))
            } else {
                Result.success(list.map { it.toBonus() })
            }
        }.catch { emit(Result.failure(it)) }

    // DayOff methods (similar structure to Bonus)
    suspend fun insertDayOff(dayOff: DayOff): Result<Long> = Result.success(dayOffDao.insertDayOff(dayOff.toEntity()))

    suspend fun insertDayOffList(dayOffs: List<DayOff>): Result<Unit> = Result.success(dayOffDao.insertDayOffList(dayOffs.map { it.toEntity() }))

    suspend fun updateDayOff(dayOff: DayOff): Result<Unit> = Result.success(dayOffDao.updateDayOff(dayOff.toEntity()))

    suspend fun updateDayOffList(dayOffs: List<DayOff>): Result<Unit> = Result.success(dayOffDao.updateDayOffList(dayOffs.map { it.toEntity() }))

    suspend fun deleteDayOff(dayOff: DayOff): Result<Unit> = Result.success(dayOffDao.deleteDayOff(dayOff.toEntity()))

    suspend fun deleteDayOffById(id: Long): Result<Unit> = Result.success(dayOffDao.deleteDayOffById(id))

    suspend fun deleteDayOffByEmployeeNumber(employeeNumber: Int): Result<Unit> =
        Result.success(dayOffDao.deleteDayOffByEmployeeNumber(employeeNumber))

    suspend fun deleteDayOffsInDateRange(startDate: SimpleDate, endDate: SimpleDate): Result<Unit> =
        Result.success(dayOffDao.deleteDayOffsInDateRange(startDate.daysSinceEpoch, endDate.daysSinceEpoch))

    fun getDayOffsByEmployeeNumber(employeeNumber: Int): Flow<Result<List<DayOff>>> =
        dayOffDao.getDayOffsByEmployeeNumber(employeeNumber).map { list ->
            if (list.isEmpty()) {
                Result.failure(NoDataException("No day-offs found for employee number $employeeNumber"))
            } else {
                Result.success(list.map { it.toDayOff() })
            }
        }.catch { emit(Result.failure(it)) }

    fun getDayOffById(id: Long): Flow<Result<DayOff>> = dayOffDao.getDayOffById(id).map { dayOff ->
        dayOff?.let { Result.success(it.toDayOff()) } ?: Result.failure(NoDataException("No day-off found with id $id"))
    }.catch { emit(Result.failure(it)) }

    fun getDayOffsInDateRange(startDate: SimpleDate, endDate: SimpleDate): Flow<Result<List<DayOff>>> =
        dayOffDao.getDayOffsInDateRange(startDate.daysSinceEpoch, endDate.daysSinceEpoch).map { list ->
            if (list.isEmpty()) {
                Result.failure(NoDataException("No day-offs found in date range $startDate to $endDate"))
            } else {
                Result.success(list.map { it.toDayOff() })
            }
        }.catch { emit(Result.failure(it)) }

    fun getDayOffsInDateRangeAndEmployeeNumber(startDate: SimpleDate, endDate: SimpleDate, employeeNumber: Int): Flow<Result<List<DayOff>>> =
        dayOffDao.getDayOffsInDateRangeAndEmployeeNumber(startDate.daysSinceEpoch, endDate.daysSinceEpoch, employeeNumber).map { list ->
            if (list.isEmpty()) {
                Result.failure(NoDataException("No day-offs found in date range $startDate to $endDate for employee number $employeeNumber"))
            } else {
                Result.success(list.map { it.toDayOff() })
            }
        }.catch { emit(Result.failure(it)) }

    // Deduction methods
    suspend fun insertDeduction(deduction: EmployeeDeduction): Result<Long> = Result.success(deductionDao.insertDeduction(deduction.toEntity()))

    suspend fun insertDeductionList(deductions: List<EmployeeDeduction>): Result<Unit> =
        Result.success(deductionDao.insertDeductionList(deductions.map { it.toEntity() }))

    suspend fun updateDeduction(deduction: EmployeeDeduction): Result<Unit> = Result.success(deductionDao.updateDeduction(deduction.toEntity()))

    suspend fun updateDeductionList(deductions: List<EmployeeDeduction>): Result<Unit> =
        Result.success(deductionDao.updateDeductionList(deductions.map { it.toEntity() }))

    suspend fun deleteDeduction(deduction: EmployeeDeduction): Result<Unit> = Result.success(deductionDao.deleteDeduction(deduction.toEntity()))

    suspend fun deleteDeductionByEmployeeNumber(employeeNumber: Int): Result<Unit> =
        Result.success(deductionDao.deleteDeductionByEmployeeNumber(employeeNumber))

    suspend fun deleteDeductionYearAndMonthAndEmployeeNumber(employeeNumber: Int, year: Int, month: Int): Result<Unit> =
        Result.success(
            deductionDao.deleteDeductionsByYearAndOptionalMonthAndEmployeeNumber(
                employeeNumber = employeeNumber,
                year = year,
                month = month
            )
        )

    fun getDeductionsByEmployeeNumber(employeeNumber: Int): Flow<Result<List<EmployeeDeduction>>> =
        deductionDao.getDeductionsByEmployeeNumber(employeeNumber).map { list ->
            if (list.isEmpty()) {
                Result.failure(NoDataException("No deductions found for employee number $employeeNumber"))
            } else {
                Result.success(list.map { it.toEmployeeDeduction() })
            }
        }.catch { emit(Result.failure(it)) }

    fun getDeductionById(id: Long): Flow<Result<EmployeeDeduction>> = deductionDao.getDeductionById(id).map { deduction ->
        deduction?.let { Result.success(it.toEmployeeDeduction()) } ?: Result.failure(NoDataException("No deduction found with id $id"))
    }.catch { emit(Result.failure(it)) }

    fun getDeductionsByYearAndOptionalMonthAndEmployeeNumber(
        year: Int,
        month: Int?,
        employeeNumber: Int,
    ): Flow<Result<List<EmployeeDeduction>>> =
        deductionDao.getDeductionsByYearAndOptionalMonthAndEmployeeNumber(year, month, employeeNumber).map { list ->
            if (list.isEmpty()) {
                Result.failure(NoDataException("No deductions found for year $year, month $month, and employee number $employeeNumber"))
            } else {
                Result.success(list.map { it.toEmployeeDeduction() })
            }
        }.catch { emit(Result.failure(it)) }

    // Employee methods
    /** insert or update */
    suspend fun insertEmployee(employee: Employee): Result<Unit> = Result.success(employeeDao.insertEmployee(employee.toEntity()))

    suspend fun insertEmployeeList(employees: List<Employee>): Result<Unit> =
        Result.success(employeeDao.insertEmployeeList(employees.map { it.toEntity() }))

    suspend fun updateEmployee(employee: Employee): Result<Unit> = Result.success(employeeDao.updateEmployee(employee.toEntity()))

    suspend fun updateEmployeeList(employees: List<Employee>): Result<Unit> =
        Result.success(employeeDao.updateEmployeeList(employees.map { it.toEntity() }))

    suspend fun deleteEmployee(employee: Employee): Result<Unit> = Result.success(employeeDao.deleteEmployee(employee.toEntity()))

    suspend fun deleteEmployee(employeeNumber: Int): Result<Unit> = Result.success(employeeDao.deleteEmployeeByNumber(employeeNumber))
    suspend fun deleteEmployees(numbers: Set<Int>): Result<Unit> = Result.success(employeeDao.deleteEmployees(numbers))

    fun getEmployee(employeeNumber: Int): Flow<Employee?> = employeeDao.getEmployeeByNumber(employeeNumber).map { employee ->
        employee?.toEmployee()
    }

    fun getEmployee(remoteId: String): Flow<Employee?> = employeeDao.getEmployeeByRemoteId(remoteId).map { employee ->
        employee?.toEmployee()
    }

    fun getAllEmployees(): Flow<List<Employee>> = employeeDao.getAllEmployees().map { list ->
        list.map { it.toEmployee() }
    }

    fun getEmployees(group: WorkGroup): Flow<List<Employee>> = employeeDao.getEmployeesByGroup(group).map { list ->
        list.map { it.toEmployee() }
    }

    // Salary methods
    suspend fun insertSalary(salary: EmployeeSalary): Result<Long> = Result.success(salaryDao.insertSalary(salary.toSalaryEntity()))

    suspend fun insertSalaryList(salaries: List<EmployeeSalary>): Result<Unit> =
        Result.success(salaryDao.insertSalaryList(salaries.map { it.toSalaryEntity() }))

    suspend fun updateSalary(salary: EmployeeSalary): Result<Unit> = Result.success(salaryDao.updateSalary(salary.toSalaryEntity()))

    suspend fun updateSalaryList(salaries: List<EmployeeSalary>): Result<Unit> =
        Result.success(salaryDao.updateSalaryList(salaries.map { it.toSalaryEntity() }))

    suspend fun deleteSalary(salary: EmployeeSalary): Result<Unit> = Result.success(salaryDao.deleteSalary(salary.toSalaryEntity()))

    suspend fun deleteSalaryById(id: Long): Result<Unit> = Result.success(salaryDao.deleteSalaryById(id))

    suspend fun deleteSalaryByEmployeeNumber(employeeNumber: Int): Result<Unit> =
        Result.success(salaryDao.deleteSalaryByEmployeeNumber(employeeNumber))

    suspend fun deleteSalariesByYearAndOptionalMonthAndEmployeeNumber(
        year: Int,
        month: Int?,
        employeeNumber: Int,
    ): Result<Unit> = Result.success(salaryDao.deleteSalariesByYearAndOptionalMonthAndEmployeeNumber(year, month, employeeNumber))

    fun getEmployeeSalaries(employeeNumber: Int): Flow<List<EmployeeSalary>> = salaryDao.getSalariesByEmployeeNumber(employeeNumber).map { list ->
        list.map { it.toSalary() }
    }

    fun getSalary(
        id: Long,
    ): Flow<EmployeeSalary?> = salaryDao.getSalaryById(id).map { salary ->
        salary?.toSalary()
    }

    fun getSalariesByYearAndOptionalMonthAndEmployeeNumber(
        year: Int,
        month: Int?,
        employeeNumber: Int,
    ): Flow<List<EmployeeSalary>> = salaryDao.getSalariesByYearAndOptionalMonthAndEmployeeNumber(year, month, employeeNumber).map { list ->
        list.map { it.toSalary() }
    }
}