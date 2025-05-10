package com.dev_bayan_ibrahim.brc_shifting.domain.model.remote


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteSalary(
    @SerialName("status")
    val status: Int,
    @SerialName("msg")
    val msg: String,
    @SerialName("data")
    val data: Data,
) {
    /**
     * Represents an employee's salary breakdown with calculation logic:
     *
     * 1. **Base Salary Calculation**
     *    Starts with [baseSalary] (SALARY_ASASY) and adjusts for:
     *    - [insurance] (SALARY_INSURENCE) - Employee insurance contribution (negative)
     *    - [illnessDaysOffCount]/[unpaidDaysOff]/[punishmentDaysOff] - Unpaid leave deductions (negative)
     *    - [salaryCompensation] - Additional compensation (positive)
     *    - Result stored in [salaryNow]
     *
     * 2. **Allowances & Bonuses**
     *  Summed into [totalOne]:
     *  - [alaawa]
     *  - [familyCompensation]
     *  - [goodManagementCompensation]
     *  - [responsibilityCompensation]
     *  - [specialistCompensation]
     *  - [jobFormCompensation]
     *  - [generalCompensations]
     *  - [extraWork]
     *  - [extraHours]
     *  - [committee]
     *  - [competence]
     *  - [additionalStrive]
     *  - [warming]
     *  - [prevSalaryRoundingAmount]
     *
     * 3. **Deductions Phase**
     *  Total deductions in [totalTwo]:
     *  - [societyInsurance]
     *  - [salaryAndInsurance]
     *  - [commitments]
     *  - [salaryTax]
     *  - [associationEngineer]
     *  - [organizationOfFreedom]
     *  - [associationOfWorkers]
     *  - [helpBox]
     *  - [associationAgricultural]
     *  - [ministerBoxValue]
     *  - [societySolidarity]
     *
     * 4. **Final Calculation**
     *  [totalNetSalary] = [totalOne] - [totalTwo]
     *  [roundedNetSalary] = [totalNetSalary] - [currentSalaryCalculatedRoundingAmount] (rounding adjustment)
     *
     *  finally [newRoundedAmount] is the value would be passed as []
     *
     * @param baseSalary Base salary before adjustments (SALARY_ASASY) - Positive
     * @param insurance Employee's insurance contribution (SALARY_INSURENCE) - Negative
     * @param salaryCompensation Additional compensation/benefits (SALARY_COMPENSATION) - Positive
     * @param specialistCompensation Specialist position allowance (SPECIALIST) - Positive
     * @param jobFormCompensation Job form/position allowance (JOB_FORM) - Positive
     * @param totalOne Total after base salary + all allowances (TOTAL_ONE)
     * @param societyInsurance Social insurance deduction (SOCIETY_INSURANCE) - Negative
     * @param salaryTax Income tax deduction (SALARY_TAX) - Negative
     * @param associationOfWorkers Worker's union fees (ASSOCIATION_OF_WORKERS) - Negative
     * @param societySolidarity Solidarity tax (SOCIETY_SOLIDARITY) - Negative
     * @param totalTwo Sum of all deductions (TOTAL_TOW) - Negative
     * @param totalNetSalary Intermediate total after deductions (TOW_ONE)
     * @param currentSalaryCalculatedRoundingAmount Final rounding adjustment (TOTAL_DISCOUNT) - Usually negative
     * @param roundedNetSalary Final take-home pay (THE_NET)
     * @param prevSalaryRoundingAmount First rounding adjustment (RECYCLE_SALARY) - ±
     * @param newRoundedAmount Secondary rounding (NEW_MODAWAR) - Handles fractions
     * @param childrenCount Number of children (potential allowance trigger)
     * @param wivesCount Number of spouses (potential allowance trigger)
     * @param illnessDaysOffCount Unpaid sick days (negative)
     * @param unpaidDaysOff Other unpaid leave (negative)
     * @param punishmentDaysOff Disciplinary deductions (negative)
     *
     * Note: Fields containing "ASSOCIATION" represent various union/organization fees
     */
    @Serializable
    data class Data(
        @SerialName("EMPLOYE_NUMBER")
        val employNumber: String,
        @SerialName("THE_MONTH")
        val month: String,
        @SerialName("THE_YEAR")
        val year: String,
        @SerialName("SALARY_INSURENCE")
        val insurance: String,
        @SerialName("SALARY_ASASY")
        val baseSalary: String,
        @SerialName("SALARY_BEFORE")
        val salaryBefore: String,
        @SerialName("SALARY_COMPENSATION")
        val salaryCompensation: String,
        @SerialName("CHILDREN_TOTAL")
        val childrenCount: String,
        @SerialName("WIFE")
        val wivesCount: String,
        @SerialName("ASSOCIATION_CASE")
        val associationState: String,
        @SerialName("ILLNESS_VACANCY_DAYS")
        val illnessDaysOffCount: String,
        @SerialName("NOTSALARY_VACANCY_DAYS")
        val unpaidDaysOff: String,
        @SerialName("PUNISHMENT_DAYS")
        val punishmentDaysOff: String,
        @SerialName("SALARY_NOW")
        val salaryNow: String,
        @SerialName("ALAAWA")
        val alaawa: String,
        @SerialName("FAMILY_ALL")
        val familyCompensation: String,
        @SerialName("GOOD_MANAG")
        val goodManagementCompensation: String,
        @SerialName("RESPONSIBILITY")
        val responsibilityCompensation: String,
        @SerialName("SPECIALIST")
        val specialistCompensation: String,
        @SerialName("JOB_FORM")
        val jobFormCompensation: String,
        @SerialName("HAWAFEZ")
        val generalCompensations: String,
        @SerialName("EXTRA_WORK")
        val extraWork: String,
        @SerialName("EXTRA_HOURS")
        val extraHours: String,
        @SerialName("COMMITTEE")
        val committee: String,
        @SerialName("COMPETENCE")
        val competence: String,
        @SerialName("ADDITIONAL_STRIVE")
        val additionalStrive: String,
        @SerialName("WARMING")
        val warming: String,
        @SerialName("RECYCLE_SALARY")
        val prevSalaryRoundingAmount: String,
        @SerialName("TOTAL_ONE")
        val totalOne: String,
        @SerialName("SOCIETY_INSURANCE")
        val societyInsurance: String,
        @SerialName("SAL_AND_INSURANCE")
        val salaryAndInsurance: String,
        @SerialName("COMMITMENTS")
        val commitments: String,
        @SerialName("SALARY_TAX")
        val salaryTax: String,
        @SerialName("ASSOCIATION_ENGINEER")
        val associationEngineer: String,
        @SerialName("ORGANIZATION_OF_FREEDOM")
        val organizationOfFreedom: String,
        @SerialName("ASSOCIATION_OF_WORKERS")
        val associationOfWorkers: String,
        @SerialName("HELP_BOX")
        val helpBox: String,
        @SerialName("ASSOCIATION_AGRICULTURAL")
        val associationAgricultural: String,
        @SerialName("SOCIETY_SOLIDARITY")
        val societySolidarity: String,
        @SerialName("MINISTER_BOX_VALUE")
        val ministerBoxValue: String,
        @SerialName("TOTAL_TOW")
        val totalTwo: String,
        @SerialName("TOW_ONE")
        val totalNetSalary: String,
        @SerialName("TOTAL_DISCOUNT")
        val currentSalaryCalculatedRoundingAmount: String,
        @SerialName("THE_NET")
        val roundedNetSalary: String,
        @SerialName("SITUATION")
        val situation: String,
        @SerialName("NEW_MODAWAR")
        val newRoundedAmount: String,
    )
}