package com.dev_bayan_ibrahim.brc_shifting.domain.model.salary

import com.dev_bayan_ibrahim.brc_shifting.domain.model.Employee
import com.dev_bayan_ibrahim.brc_shifting.domain.model.core.EmployeeRelated
import com.dev_bayan_ibrahim.brc_shifting.domain.model.core.Identified
import com.dev_bayan_ibrahim.brc_shifting.domain.model.core.MonthRelated
import com.dev_bayan_ibrahim.brc_shifting.domain.model.core.Syncable
import kotlinx.datetime.Instant


/**
 * Calculates final net salary using components from other classes
 * - [BaseSalary] contains base salary data and the start off point
 * - [SalaryAllowance] some fields that is added to the base salary
 * - [SalaryDeduction] some fields that is removed from the base salary
 * - [NetSalary] it is the rounded value of
 * @property netExpectedTotal calculated total amount (without rounding of net salary)
 * @property netExpectedRounded calculated total amount with rounding
 * @property netExpectedRounding calculated routing amount
 */

interface Salary : BaseSalary, SalaryAllowance, SalaryDeduction, NetSalary {
    /**
     * value of (without rounding): [baseSalary] + [allowanceExpectedSum] - [deductionExpectedSum]
     */
    val netExpectedTotal: Int
        get() = baseSalary + allowanceExpectedSum - deductionExpectedSum

    /**
     * rounded value of [netExpectedTotal] (mod 100)
     */
    val netExpectedRounded: Int
        get() = netExpectedTotal % 100

    /**
     * difference between [netExpectedTotal] and [netExpectedRounded] calculated by:
     * [netExpectedTotal] / 100
     */
    val netExpectedRounding: Int
        get() = netExpectedTotal / 100
}

class SalaryDelegate(
    baseSalary: BaseSalary,
    allowance: SalaryAllowance,
    deduction: SalaryDeduction,
    netSalary: NetSalary,
) : Salary, BaseSalary by baseSalary, SalaryAllowance by allowance, SalaryDeduction by deduction, NetSalary by netSalary

interface EmployeeSalary : Salary, EmployeeRelated, Syncable, MonthRelated, Identified {
    companion object {
        operator fun invoke(
            employeeNumber: Int,
            createdAt: Instant,
            updatedAt: Instant,
            monthNumber: Int,
            year: Int,
            id: Long?,
            salary: Salary,
        ): EmployeeSalary = EmployeeSalaryDelegate(
            employeeNumber = employeeNumber,
            createdAt = createdAt,
            updatedAt = updatedAt,
            monthNumber = monthNumber,
            year = year,
            id = id,
            salary = salary
        )

        operator fun invoke(
            employeeNumber: Int,
            createdAt: Instant,
            updatedAt: Instant,
            monthNumber: Int,
            year: Int,
            id: Long?,
            baseSalary: BaseSalary,
            allowance: SalaryAllowance,
            deduction: SalaryDeduction,
            netSalary: NetSalary,
        ): EmployeeSalary = EmployeeSalaryDelegate(
            employeeNumber = employeeNumber,
            createdAt = createdAt,
            updatedAt = updatedAt,
            monthNumber = monthNumber,
            year = year,
            id = id,
            baseSalary = baseSalary,
            allowance = allowance,
            deduction = deduction,
            netSalary = netSalary
        )
    }

}

class EmployeeSalaryDelegate(
    override val employeeNumber: Int,
    override val createdAt: Instant,
    override val updatedAt: Instant,
    override val monthNumber: Int,
    override val year: Int,
    override val id: Long?,
    salary: Salary,
) : EmployeeSalary, Salary by salary {
    constructor(salary: EmployeeSalary): this(
        employeeNumber = salary.employeeNumber,
        createdAt = salary.createdAt,
        updatedAt = salary.updatedAt,
        monthNumber = salary.monthNumber,
        year = salary.year,
        id = salary.id,
        salary = salary
    )
    constructor(
        employeeNumber: Int,
        createdAt: Instant,
        updatedAt: Instant,
        monthNumber: Int,
        year: Int,
        id: Long?,
        baseSalary: BaseSalary,
        allowance: SalaryAllowance,
        deduction: SalaryDeduction,
        netSalary: NetSalary,
    ) : this(
        employeeNumber = employeeNumber,
        createdAt = createdAt,
        updatedAt = updatedAt,
        monthNumber = monthNumber,
        year = year,
        id = id,
        salary = SalaryDelegate(
            baseSalary = baseSalary,
            allowance = allowance,
            deduction = deduction,
            netSalary = netSalary
        )
    )
}