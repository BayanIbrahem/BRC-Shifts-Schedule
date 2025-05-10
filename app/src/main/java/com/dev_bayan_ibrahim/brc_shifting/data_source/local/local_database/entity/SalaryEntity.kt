package com.dev_bayan_ibrahim.brc_shifting.data_source.local.local_database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.dev_bayan_ibrahim.brc_shifting.domain.model.salary.BaseSalaryDelegate
import com.dev_bayan_ibrahim.brc_shifting.domain.model.salary.EmployeeSalary
import com.dev_bayan_ibrahim.brc_shifting.domain.model.salary.EmployeeSalaryDelegate
import com.dev_bayan_ibrahim.brc_shifting.domain.model.salary.NetSalaryDelegate
import com.dev_bayan_ibrahim.brc_shifting.domain.model.salary.SalaryAllowanceDelegate
import com.dev_bayan_ibrahim.brc_shifting.domain.model.salary.SalaryDeductionDelegate
import kotlinx.datetime.Instant

const val SALARY_TABLE = "salary"

// Column name constants
const val SALARY_ID = "salary_id"
const val SALARY_EMPLOYEE_NUMBER = "salary_employee_number"
const val SALARY_BASE_SALARY = "salary_base_salary"
const val SALARY_INSURANCE = "salary_insurance"
const val SALARY_COMPENSATION = "salary_compensation"
const val SALARY_ILLNESS_DAYS_OFF_COUNT = "salary_illness_days_off_count"
const val SALARY_UNPAID_DAYS_OFF = "salary_unpaid_days_off"
const val SALARY_PUNISHMENT_DAYS_OFF = "salary_punishment_days_off"
const val SALARY_CHILDREN_COUNT = "salary_children_count"
const val SALARY_WIVES_COUNT = "salary_wives_count"
const val SALARY_BASIC_ALLOWANCE = "salary_basic_allowance"
const val SALARY_FAMILY_COMPENSATION = "salary_family_compensation"
const val SALARY_MANAGEMENT_BONUS = "salary_management_bonus"
const val SALARY_RESPONSIBILITY_ALLOWANCE = "salary_responsibility_allowance"
const val SALARY_SPECIALIST_BONUS = "salary_specialist_bonus"
const val SALARY_POSITION_ALLOWANCE = "salary_position_allowance"
const val SALARY_GENERAL_COMPENSATIONS = "salary_general_compensations"
const val SALARY_OVERTIME_PAY = "salary_overtime_pay"
const val SALARY_EXTRA_HOURS_PAY = "salary_extra_hours_pay"
const val SALARY_COMMITTEE_BONUS = "salary_committee_bonus"
const val SALARY_COMPETENCE_ALLOWANCE = "salary_competence_allowance"
const val SALARY_EFFORT_BONUS = "salary_effort_bonus"
const val SALARY_WARMING_ALLOWANCE = "salary_warming_allowance"
const val SALARY_ROUNDING_ADJUSTMENT = "salary_rounding_adjustment"
const val SALARY_ALLOWANCE_TOTAL = "salary_allowance_total"
const val SALARY_SOCIAL_INSURANCE = "salary_social_insurance"
const val SALARY_SALARY_INSURANCE = "salary_salary_insurance"
const val SALARY_FINANCIAL_COMMITMENTS = "salary_financial_commitments"
const val SALARY_INCOME_TAX = "salary_income_tax"
const val SALARY_ENGINEER_ASSOCIATION = "salary_engineer_association"
const val SALARY_FREEDOM_ORGANIZATION = "salary_freedom_organization"
const val SALARY_WORKERS_UNION = "salary_workers_union"
const val SALARY_CHARITY_BOX = "salary_charity_box"
const val SALARY_AGRICULTURAL_ASSOCIATION = "salary_agricultural_association"
const val SALARY_MINISTRY_FUND = "salary_ministry_fund"
const val SALARY_SOLIDARITY_TAX = "salary_solidarity_tax"
const val SALARY_DEDUCTION_TOTAL = "salary_deduction_total"
const val SALARY_NET_TOTAL = "salary_net_total"
const val SALARY_NET_ROUNDED = "salary_net_rounded"
const val SALARY_NET_ROUNDING = "salary_net_rounding"
const val SALARY_CREATED_AT = "salary_created_at"
const val SALARY_UPDATED_AT = "salary_updated_at"
const val SALARY_MONTH_NUMBER = "salary_month_number"
const val SALARY_YEAR = "salary_year"

@Entity(
    tableName = SALARY_TABLE,
    foreignKeys = [ForeignKey(
        entity = EmployeeEntity::class,
        parentColumns = [EmployeeEntity.EMPLOYEE_NUMBER],
        childColumns = [SALARY_EMPLOYEE_NUMBER],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE,
    )],
    indices = [Index(value = [SALARY_EMPLOYEE_NUMBER])]
)
data class SalaryEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = SALARY_ID)
    val id: Long?,
    @ColumnInfo(name = SALARY_EMPLOYEE_NUMBER) val employeeNumber: Int,
    @ColumnInfo(name = SALARY_BASE_SALARY) val baseSalary: Int,
    @ColumnInfo(name = SALARY_INSURANCE) val insurance: Int,
    @ColumnInfo(name = SALARY_COMPENSATION) val salaryCompensation: Int,
    @ColumnInfo(name = SALARY_ILLNESS_DAYS_OFF_COUNT) val illnessDaysOffCount: Int,
    @ColumnInfo(name = SALARY_UNPAID_DAYS_OFF) val unpaidDaysOff: Int,
    @ColumnInfo(name = SALARY_PUNISHMENT_DAYS_OFF) val punishmentDaysOff: Int,
    @ColumnInfo(name = SALARY_CHILDREN_COUNT) val childrenCount: Int,
    @ColumnInfo(name = SALARY_WIVES_COUNT) val wivesCount: Int,
    @ColumnInfo(name = SALARY_BASIC_ALLOWANCE) val basicAllowance: Int,
    @ColumnInfo(name = SALARY_FAMILY_COMPENSATION) val familyCompensation: Int,
    @ColumnInfo(name = SALARY_MANAGEMENT_BONUS) val managementBonus: Int,
    @ColumnInfo(name = SALARY_RESPONSIBILITY_ALLOWANCE) val responsibilityAllowance: Int,
    @ColumnInfo(name = SALARY_SPECIALIST_BONUS) val specialistBonus: Int,
    @ColumnInfo(name = SALARY_POSITION_ALLOWANCE) val positionAllowance: Int,
    @ColumnInfo(name = SALARY_GENERAL_COMPENSATIONS) val generalCompensations: Int,
    @ColumnInfo(name = SALARY_OVERTIME_PAY) val overtimePay: Int,
    @ColumnInfo(name = SALARY_EXTRA_HOURS_PAY) val extraHoursPay: Int,
    @ColumnInfo(name = SALARY_COMMITTEE_BONUS) val committeeBonus: Int,
    @ColumnInfo(name = SALARY_COMPETENCE_ALLOWANCE) val competenceAllowance: Int,
    @ColumnInfo(name = SALARY_EFFORT_BONUS) val effortBonus: Int,
    @ColumnInfo(name = SALARY_WARMING_ALLOWANCE) val warmingAllowance: Int,
    @ColumnInfo(name = SALARY_ROUNDING_ADJUSTMENT) val salaryRoundingAdjustment: Int,
    @ColumnInfo(name = SALARY_ALLOWANCE_TOTAL) val allowanceProvidedTotal: Int,
    @ColumnInfo(name = SALARY_SOCIAL_INSURANCE) val socialInsurance: Int,
    @ColumnInfo(name = SALARY_SALARY_INSURANCE) val salaryInsurance: Int,
    @ColumnInfo(name = SALARY_FINANCIAL_COMMITMENTS) val financialCommitments: Int,
    @ColumnInfo(name = SALARY_INCOME_TAX) val incomeTax: Int,
    @ColumnInfo(name = SALARY_ENGINEER_ASSOCIATION) val engineerAssociation: Int,
    @ColumnInfo(name = SALARY_FREEDOM_ORGANIZATION) val freedomOrganization: Int,
    @ColumnInfo(name = SALARY_WORKERS_UNION) val workersUnion: Int,
    @ColumnInfo(name = SALARY_CHARITY_BOX) val charityBox: Int,
    @ColumnInfo(name = SALARY_AGRICULTURAL_ASSOCIATION) val agriculturalAssociation: Int,
    @ColumnInfo(name = SALARY_MINISTRY_FUND) val ministryFund: Int,
    @ColumnInfo(name = SALARY_SOLIDARITY_TAX) val solidarityTax: Int,
    @ColumnInfo(name = SALARY_DEDUCTION_TOTAL) val deductionProvidedTotal: Int,
    @ColumnInfo(name = SALARY_NET_TOTAL) val netProvidedTotal: Int,
    @ColumnInfo(name = SALARY_NET_ROUNDED) val netProvidedRounded: Int,
    @ColumnInfo(name = SALARY_NET_ROUNDING) val netProvidedRounding: Int,
    @ColumnInfo(name = SALARY_CREATED_AT) val createdAt: Instant,
    @ColumnInfo(name = SALARY_UPDATED_AT) val updatedAt: Instant,
    @ColumnInfo(name = SALARY_MONTH_NUMBER) val monthNumber: Int,
    @ColumnInfo(name = SALARY_YEAR) val year: Int,
)

fun SalaryEntity.toSalary(): EmployeeSalary = EmployeeSalaryDelegate(
    employeeNumber = employeeNumber,
    createdAt = createdAt,
    updatedAt = updatedAt,
    monthNumber = monthNumber,
    year = year,
    id = id,
    baseSalary = BaseSalaryDelegate(
        baseSalary = baseSalary,
        insurance = insurance,
        salaryCompensation = salaryCompensation,
        illnessDaysOffCount = illnessDaysOffCount,
        unpaidDaysOff = unpaidDaysOff,
        punishmentDaysOff = punishmentDaysOff,
        childrenCount = childrenCount,
        wivesCount = wivesCount
    ),
    allowance = SalaryAllowanceDelegate(
        basicAllowance = basicAllowance,
        familyCompensation = familyCompensation,
        managementBonus = managementBonus,
        responsibilityAllowance = responsibilityAllowance,
        specialistBonus = specialistBonus,
        positionAllowance = positionAllowance,
        generalCompensations = generalCompensations,
        overtimePay = overtimePay,
        extraHoursPay = extraHoursPay,
        committeeBonus = committeeBonus,
        competenceAllowance = competenceAllowance,
        effortBonus = effortBonus,
        warmingAllowance = warmingAllowance,
        salaryRoundingAdjustment = salaryRoundingAdjustment,
        allowanceProvidedTotal = allowanceProvidedTotal
    ),
    deduction = SalaryDeductionDelegate(
        socialInsurance = socialInsurance,
        salaryInsurance = salaryInsurance,
        financialCommitments = financialCommitments,
        incomeTax = incomeTax,
        engineerAssociation = engineerAssociation,
        freedomOrganization = freedomOrganization,
        workersUnion = workersUnion,
        charityBox = charityBox,
        agriculturalAssociation = agriculturalAssociation,
        ministryFund = ministryFund,
        solidarityTax = solidarityTax,
        deductionProvidedTotal = deductionProvidedTotal
    ),
    netSalary = NetSalaryDelegate(
        netProvidedTotal = netProvidedTotal,
        netProvidedRounded = netProvidedRounded,
        netProvidedRounding = netProvidedRounding
    ),
)

fun EmployeeSalary.toSalaryEntity(
    overrideUpdatedAt: Boolean = true,
    overrideUpdatedAtIfNew: Boolean = false,
): SalaryEntity {
    return SalaryEntity(
        id = id,
        employeeNumber = employeeNumber,
        baseSalary = baseSalary,
        insurance = insurance,
        salaryCompensation = salaryCompensation,
        illnessDaysOffCount = illnessDaysOffCount,
        unpaidDaysOff = unpaidDaysOff,
        punishmentDaysOff = punishmentDaysOff,
        childrenCount = childrenCount,
        wivesCount = wivesCount,
        basicAllowance = basicAllowance,
        familyCompensation = familyCompensation,
        managementBonus = managementBonus,
        responsibilityAllowance = responsibilityAllowance,
        specialistBonus = specialistBonus,
        positionAllowance = positionAllowance,
        generalCompensations = generalCompensations,
        overtimePay = overtimePay,
        extraHoursPay = extraHoursPay,
        committeeBonus = committeeBonus,
        competenceAllowance = competenceAllowance,
        effortBonus = effortBonus,
        warmingAllowance = warmingAllowance,
        salaryRoundingAdjustment = salaryRoundingAdjustment,
        allowanceProvidedTotal = allowanceProvidedTotal,
        socialInsurance = socialInsurance,
        salaryInsurance = salaryInsurance,
        financialCommitments = financialCommitments,
        incomeTax = incomeTax,
        engineerAssociation = engineerAssociation,
        freedomOrganization = freedomOrganization,
        workersUnion = workersUnion,
        charityBox = charityBox,
        agriculturalAssociation = agriculturalAssociation,
        ministryFund = ministryFund,
        solidarityTax = solidarityTax,
        deductionProvidedTotal = deductionProvidedTotal,
        netProvidedTotal = netProvidedTotal,
        netProvidedRounded = netProvidedRounded,
        netProvidedRounding = netProvidedRounding,
        createdAt = createdAt,
        updatedAt = updatedAt,
        monthNumber = monthNumber,
        year = year,
    )

}