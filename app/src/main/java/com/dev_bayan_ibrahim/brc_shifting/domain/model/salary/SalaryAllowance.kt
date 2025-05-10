package com.dev_bayan_ibrahim.brc_shifting.domain.model.salary

import com.dev_bayan_ibrahim.brc_shifting.domain.model.core.EmployeeRelated
import com.dev_bayan_ibrahim.brc_shifting.domain.model.core.Identified
import com.dev_bayan_ibrahim.brc_shifting.domain.model.core.MonthRelated
import com.dev_bayan_ibrahim.brc_shifting.domain.model.core.Syncable
import kotlinx.datetime.Instant

/**
 * Represents all salary allowances and bonuses with computed expected sum
 * @property basicAllowance Basic living allowance
 * @property familyCompensation Family support compensation
 * @property managementBonus Good management bonus
 * @property responsibilityAllowance Additional responsibility allowance
 * @property specialistBonus Specialist position bonus
 * @property positionAllowance Job position allowance
 * @property generalCompensations Miscellaneous compensations
 * @property overtimePay Calculated overtime pay
 * @property extraHoursPay Additional hours compensation
 * @property committeeBonus Committee participation bonus
 * @property competenceAllowance Skill competence allowance
 * @property effortBonus Extra effort recognition
 * @property warmingAllowance Climate-related compensation
 * @property salaryRoundingAdjustment Initial salary rounding adjustment
 * @property allowanceProvidedTotal the provided and precalculated total used to compare with [allowanceExpectedSum] to check for any mistakes
 *
 * @property allowanceExpectedSum Computed sum of all allowances for validation
 */
interface SalaryAllowance {
    val basicAllowance: Int
    val familyCompensation: Int
    val managementBonus: Int
    val responsibilityAllowance: Int
    val specialistBonus: Int
    val positionAllowance: Int
    val generalCompensations: Int
    val overtimePay: Int
    val extraHoursPay: Int
    val committeeBonus: Int
    val competenceAllowance: Int
    val effortBonus: Int
    val warmingAllowance: Int
    val salaryRoundingAdjustment: Int
    val allowanceProvidedTotal: Int

    val allowanceExpectedSum: Int
        get() = listOf(
            basicAllowance,
            familyCompensation,
            managementBonus,
            responsibilityAllowance,
            specialistBonus,
            positionAllowance,
            generalCompensations,
            overtimePay,
            extraHoursPay,
            committeeBonus,
            competenceAllowance,
            effortBonus,
            warmingAllowance,
            salaryRoundingAdjustment
        ).sum()
}

/**
 * delegate of [SalaryAllowance]
 */

data class SalaryAllowanceDelegate(
    override val basicAllowance: Int,
    override val familyCompensation: Int,
    override val managementBonus: Int,
    override val responsibilityAllowance: Int,
    override val specialistBonus: Int,
    override val positionAllowance: Int,
    override val generalCompensations: Int,
    override val overtimePay: Int,
    override val extraHoursPay: Int,
    override val committeeBonus: Int,
    override val competenceAllowance: Int,
    override val effortBonus: Int,
    override val warmingAllowance: Int,
    override val salaryRoundingAdjustment: Int,
    override val allowanceProvidedTotal: Int,
) : SalaryAllowance


/**
 * salary allowance with extra metadata:
 * - [EmployeeRelated]
 * - [Syncable]
 * - [MonthRelated]
 * - [Identified]
 */
interface EmployeeSalaryAllowance : SalaryAllowance, EmployeeRelated, Syncable, MonthRelated, Identified

/**
 * delegate of [EmployeeSalaryAllowance]
 */
data class EmployeeSalaryAllowanceDelegate(
    override val employeeNumber: Int,
    override val basicAllowance: Int,
    override val familyCompensation: Int,
    override val managementBonus: Int,
    override val responsibilityAllowance: Int,
    override val specialistBonus: Int,
    override val positionAllowance: Int,
    override val generalCompensations: Int,
    override val overtimePay: Int,
    override val extraHoursPay: Int,
    override val committeeBonus: Int,
    override val competenceAllowance: Int,
    override val effortBonus: Int,
    override val warmingAllowance: Int,
    override val salaryRoundingAdjustment: Int,
    override val allowanceProvidedTotal: Int,
    override val createdAt: Instant,
    override val updatedAt: Instant,
    override val monthNumber: Int,
    override val year: Int,
    override val id: Long?,
) : EmployeeSalaryAllowance