package com.dev_bayan_ibrahim.brc_shifting.domain.model.salary

import com.dev_bayan_ibrahim.brc_shifting.domain.model.core.EmployeeRelated
import com.dev_bayan_ibrahim.brc_shifting.domain.model.core.Identified
import com.dev_bayan_ibrahim.brc_shifting.domain.model.core.MonthRelated
import com.dev_bayan_ibrahim.brc_shifting.domain.model.core.Syncable
import kotlinx.datetime.Instant

/**
 * Represents the base salary calculation components and logic
 * @property baseSalary Base salary before adjustments (Positive)
 * @property insurance Employee's insurance contribution (Negative)
 * @property salaryCompensation Additional compensation/benefits (Positive)
 * @property illnessDaysOffCount Unpaid sick days count (Negative impact)
 * @property unpaidDaysOff Other unpaid leave count (Negative impact)
 * @property punishmentDaysOff Disciplinary deductions count (Negative impact)
 * @property childrenCount total count of children
 * @property wivesCount total count of wives
 */
interface BaseSalary {
    val baseSalary: Int
    val insurance: Int
    val salaryCompensation: Int
    val illnessDaysOffCount: Int
    val unpaidDaysOff: Int
    val punishmentDaysOff: Int
    val childrenCount: Int
    val wivesCount: Int
}

data class BaseSalaryDelegate(
    override val baseSalary: Int,
    override val insurance: Int,
    override val salaryCompensation: Int,
    override val illnessDaysOffCount: Int,
    override val unpaidDaysOff: Int,
    override val punishmentDaysOff: Int,
    override val childrenCount: Int,
    override val wivesCount: Int,
) : BaseSalary

/**
 * base salary [BaseSalary] with extra identifiers:
 * - [EmployeeRelated]
 * - [Syncable]
 * - [MonthRelated]
 * - [Identified]
 */
interface EmployeeBaseSalary : BaseSalary, EmployeeRelated, Syncable, MonthRelated, Identified

data class EmployeeBaseSalaryDelegate(
    override val employeeNumber: Int,
    override val createdAt: Instant,
    override val updatedAt: Instant,
    override val monthNumber: Int,
    override val year: Int,
    override val id: Long? = null,
    override val baseSalary: Int,
    override val insurance: Int,
    override val salaryCompensation: Int,
    override val illnessDaysOffCount: Int,
    override val unpaidDaysOff: Int,
    override val punishmentDaysOff: Int,
    override val childrenCount: Int,
    override val wivesCount: Int,
) : EmployeeBaseSalary