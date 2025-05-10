package com.dev_bayan_ibrahim.brc_shifting.domain.model.salary

import com.dev_bayan_ibrahim.brc_shifting.domain.model.remote.RemoteSalary
import kotlinx.datetime.Clock

fun RemoteSalary.toSalary(
    employeeNumber: Int,
): Salary {
    val now = Clock.System.now()
    val baseSalary = toBaseSalary(employeeNumber)
    val allowances = toSalaryAllowance(employeeNumber)
    val deductions = toSalaryDeduction(employeeNumber)
    val netSalary = toNetSalary(employeeNumber)
    return EmployeeSalaryDelegate(
        baseSalary = baseSalary,
        allowance = allowances,
        deduction = deductions,
        netSalary = netSalary,
        employeeNumber = employeeNumber,
        createdAt = now,
        updatedAt = now,
        monthNumber = data.month.toInt(),
        year = data.year.toInt(),
        id = null,
    )
}

private fun RemoteSalary.toNetSalary(
    employeeNumber: Int,
): EmployeeNetSalaryDelegate {
    val now = Clock.System.now()
    return EmployeeNetSalaryDelegate(
        employeeNumber = employeeNumber,
        createdAt = now,
        updatedAt = now,
        monthNumber = data.year.toInt(),
        year = data.month.toInt(),
        id = null,
        netProvidedTotal = data.totalNetSalary.toInt(),
        netProvidedRounded = data.roundedNetSalary.toInt(),
        netProvidedRounding = data.newRoundedAmount.toInt(),
    )
}

private fun RemoteSalary.toSalaryDeduction(employeeNumber: Int): EmployeeSalaryDeductionDelegate {
    val now = Clock.System.now()
    return EmployeeSalaryDeductionDelegate(
        employeeNumber = employeeNumber,
        createdAt = now,
        updatedAt = now,
        socialInsurance = data.societyInsurance.toInt(),
        salaryInsurance = data.salaryAndInsurance.toInt(),
        financialCommitments = data.commitments.toInt(),
        incomeTax = data.salaryTax.toInt(),
        engineerAssociation = data.associationEngineer.toInt(),
        freedomOrganization = data.organizationOfFreedom.toInt(),
        workersUnion = data.associationOfWorkers.toInt(),
        charityBox = data.helpBox.toInt(),
        agriculturalAssociation = data.associationAgricultural.toInt(),
        ministryFund = data.ministerBoxValue.toInt(),
        solidarityTax = data.societySolidarity.toInt(),
        deductionProvidedTotal = data.totalTwo.toInt(),
        monthNumber = data.year.toInt(),
        year = data.month.toInt(),
        id = null,
    )
}

private fun RemoteSalary.toSalaryAllowance(employeeNumber: Int): EmployeeSalaryAllowanceDelegate {
    val now = Clock.System.now()
    return EmployeeSalaryAllowanceDelegate(
        employeeNumber = employeeNumber,
        createdAt = now,
        updatedAt = now,
        basicAllowance = data.alaawa.toInt(),
        familyCompensation = data.familyCompensation.toInt(),
        managementBonus = data.goodManagementCompensation.toInt(),
        responsibilityAllowance = data.responsibilityCompensation.toInt(),
        specialistBonus = data.specialistCompensation.toInt(),
        positionAllowance = data.jobFormCompensation.toInt(),
        generalCompensations = data.generalCompensations.toInt(),
        overtimePay = data.extraWork.toInt(),
        extraHoursPay = data.extraHours.toInt(),
        committeeBonus = data.committee.toInt(),
        competenceAllowance = data.competence.toInt(),
        effortBonus = data.additionalStrive.toInt(),
        warmingAllowance = data.warming.toInt(),
        salaryRoundingAdjustment = data.prevSalaryRoundingAmount.toInt(),
        allowanceProvidedTotal = data.totalOne.toInt(),
        monthNumber = data.year.toInt(),
        year = data.month.toInt(),
        id = null,
    )
}

private fun RemoteSalary.toBaseSalary(employeeNumber: Int): EmployeeBaseSalaryDelegate {
    val now = Clock.System.now()
    return EmployeeBaseSalaryDelegate(
        employeeNumber = employeeNumber,
        createdAt = now,
        updatedAt = now,
        baseSalary = data.baseSalary.toInt(),
        insurance = data.insurance.toInt(),
        salaryCompensation = data.salaryCompensation.toInt(),
        illnessDaysOffCount = data.illnessDaysOffCount.toInt(),
        unpaidDaysOff = data.unpaidDaysOff.toInt(),
        punishmentDaysOff = data.punishmentDaysOff.toInt(),
        childrenCount = data.childrenCount.toInt(),
        wivesCount = data.wivesCount.toInt(),
        monthNumber = data.year.toInt(),
        year = data.month.toInt(),
        id = null,
    )
}