package com.dev_bayan_ibrahim.brc_shifting.domain.model.salary

import com.dev_bayan_ibrahim.brc_shifting.domain.model.core.EmployeeRelated
import com.dev_bayan_ibrahim.brc_shifting.domain.model.core.Identified
import com.dev_bayan_ibrahim.brc_shifting.domain.model.core.MonthRelated
import com.dev_bayan_ibrahim.brc_shifting.domain.model.core.Syncable
import kotlinx.datetime.Instant

/**
 * Represents all salary deductions with computed expected sum
 * @property socialInsurance Mandatory social insurance
 * @property salaryInsurance Salary-related insurance
 * @property financialCommitments Loan repayments
 * @property incomeTax Calculated income tax
 * @property engineerAssociation Engineer association fees
 * @property freedomOrganization Membership fees
 * @property workersUnion Workers' union fees
 * @property charityBox Charity contributions
 * @property agriculturalAssociation Agricultural group fees
 * @property ministryFund Government ministry fund
 * @property solidarityTax Social solidarity tax
 * @property deductionProvidedTotal the provided and precalculated total used to compare with [deductionExpectedSum] to check for any mistakes
 *
 * @property deductionExpectedSum Computed sum of all deductions for validation
 */
interface SalaryDeduction {
    val socialInsurance: Int
    val salaryInsurance: Int
    val financialCommitments: Int
    val incomeTax: Int
    val engineerAssociation: Int
    val freedomOrganization: Int
    val workersUnion: Int
    val charityBox: Int
    val agriculturalAssociation: Int
    val ministryFund: Int
    val solidarityTax: Int
    val deductionProvidedTotal: Int

    val deductionExpectedSum: Int
        get() = listOf(
            socialInsurance,
            salaryInsurance,
            financialCommitments,
            incomeTax,
            engineerAssociation,
            freedomOrganization,
            workersUnion,
            charityBox,
            agriculturalAssociation,
            ministryFund,
            solidarityTax
        ).sum()
}

/**
 * delegate of [SalaryDeduction]
 */

data class SalaryDeductionDelegate(
    override val socialInsurance: Int,
    override val salaryInsurance: Int,
    override val financialCommitments: Int,
    override val incomeTax: Int,
    override val engineerAssociation: Int,
    override val freedomOrganization: Int,
    override val workersUnion: Int,
    override val charityBox: Int,
    override val agriculturalAssociation: Int,
    override val ministryFund: Int,
    override val solidarityTax: Int,
    override val deductionProvidedTotal: Int,
) : SalaryDeduction

/**
 * salary deduction with extra metadata:
 * - [EmployeeRelated]
 * - [Syncable]
 * - [MonthRelated]
 * - [Identified]
 */
interface EmployeeSalaryDeduction : SalaryDeduction, EmployeeRelated, Syncable, MonthRelated, Identified

/**
 * delegate of [EmployeeSalaryDeduction]
 */
data class EmployeeSalaryDeductionDelegate(
    override val employeeNumber: Int,
    override val socialInsurance: Int,
    override val salaryInsurance: Int,
    override val financialCommitments: Int,
    override val incomeTax: Int,
    override val engineerAssociation: Int,
    override val freedomOrganization: Int,
    override val workersUnion: Int,
    override val charityBox: Int,
    override val agriculturalAssociation: Int,
    override val ministryFund: Int,
    override val solidarityTax: Int,
    override val deductionProvidedTotal: Int,
    override val createdAt: Instant,
    override val updatedAt: Instant,
    override val monthNumber: Int,
    override val year: Int,
    override val id: Long?,
) : EmployeeSalaryDeduction

