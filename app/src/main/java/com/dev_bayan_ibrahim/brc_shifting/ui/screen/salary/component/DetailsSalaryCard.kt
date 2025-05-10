package com.dev_bayan_ibrahim.brc_shifting.ui.screen.salary.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dev_bayan_ibrahim.brc_shifting.domain.model.Employee
import com.dev_bayan_ibrahim.brc_shifting.domain.model.salary.BaseSalary
import com.dev_bayan_ibrahim.brc_shifting.domain.model.salary.EmployeeSalary
import com.dev_bayan_ibrahim.brc_shifting.domain.model.salary.NetSalary
import com.dev_bayan_ibrahim.brc_shifting.domain.model.salary.SalaryAllowance
import com.dev_bayan_ibrahim.brc_shifting.domain.model.salary.SalaryDeduction
import com.dev_bayan_ibrahim.brc_shifting.util.monthYearFormat
import kotlinx.datetime.LocalDate

@Composable
fun DetailsSalaryCard(
    employee: Employee,
    salary: EmployeeSalary,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SalaryEmployeeCard(
            employee = employee,
            salaryDate = salary.atStartOfMonth,
            modifier = Modifier.fillMaxWidth(),
        )
        BaseSalaryCard(
            baseSalary = salary,
            modifier = Modifier.fillMaxWidth(),
        )
        NetSalaryCard(
            net = salary,
            modifier = Modifier.fillMaxWidth(),
        )
        SalaryAllowanceCard(
            allowance = salary,
            modifier = Modifier.fillMaxWidth(),
        )
        SalaryDeductionCard(
            deduction = salary,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun SalaryEmployeeCard(
    employee: Employee,
    salaryDate: LocalDate,

    modifier: Modifier = Modifier,
) {
    // TODO, string res
    SalaryContainer(
        title = "Employee",
        modifier = modifier,
    ) {
        Text("Name ${employee.name}", style = MaterialTheme.typography.bodyMedium)
        Text("Number ${employee.employeeNumber}", style = MaterialTheme.typography.bodyMedium)
        Text("Salary ${salaryDate.monthYearFormat()}", style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun BaseSalaryCard(
    baseSalary: BaseSalary,
    modifier: Modifier = Modifier,
) {
    // TODO, string res
    SalaryContainer(
        title = "Base Salary",
        modifier = modifier,
    ) {
        NormalEntry("Base", baseSalary.baseSalary)
        NormalEntry("Insurance", baseSalary.insurance)
        NormalEntry("Salary Compensation", baseSalary.salaryCompensation)
        NormalEntry("Illness Days Off", baseSalary.illnessDaysOffCount)
        NormalEntry("Punishment Days off", baseSalary.punishmentDaysOff)
        NormalEntry("Children", baseSalary.childrenCount)
        NormalEntry("Wives", baseSalary.wivesCount)
    }
}

@Composable
fun SalaryAllowanceCard(
    allowance: SalaryAllowance,
    modifier: Modifier = Modifier,
) {
    SalaryContainer(
        title = "Salary Allowances",
        modifier = modifier,
    ) {
        NormalEntry("Basic Allowance", allowance.basicAllowance)
        NormalEntry("Family Compensation", allowance.familyCompensation)
        NormalEntry("Management Bonus", allowance.managementBonus)
        NormalEntry("Responsibility Allowance", allowance.responsibilityAllowance)
        NormalEntry("Specialist Bonus", allowance.specialistBonus)
        NormalEntry("Position Allowance", allowance.positionAllowance)
        NormalEntry("General Compensations", allowance.generalCompensations)
        NormalEntry("Overtime Pay", allowance.overtimePay)
        NormalEntry("Extra Hours Pay", allowance.extraHoursPay)
        NormalEntry("Committee Bonus", allowance.committeeBonus)
        NormalEntry("Competence Allowance", allowance.competenceAllowance)
        NormalEntry("Effort Bonus", allowance.effortBonus)
        NormalEntry("Warming Allowance", allowance.warmingAllowance)
        NormalEntry("Rounding Adjustment", allowance.salaryRoundingAdjustment)
        HorizontalDivider()
        ImportantEntry("Provided Total", allowance.allowanceProvidedTotal)
        ImportantEntry("Expected Total", allowance.allowanceExpectedSum)
    }
}

@Composable
fun SalaryDeductionCard(
    deduction: SalaryDeduction,
    modifier: Modifier = Modifier,
) {
    SalaryContainer(
        title = "Salary Deductions",
        modifier = modifier,
    ) {
        NormalEntry("Social Insurance", deduction.socialInsurance)
        NormalEntry("Salary Insurance", deduction.salaryInsurance)
        NormalEntry("Financial Commitments", deduction.financialCommitments)
        NormalEntry("Income Tax", deduction.incomeTax)
        NormalEntry("Engineer Association", deduction.engineerAssociation)
        NormalEntry("Freedom Organization", deduction.freedomOrganization)
        NormalEntry("Workers Union", deduction.workersUnion)
        NormalEntry("Charity Box", deduction.charityBox)
        NormalEntry("Agricultural Association", deduction.agriculturalAssociation)
        NormalEntry("Ministry Fund", deduction.ministryFund)
        NormalEntry("Solidarity Tax", deduction.solidarityTax)
        HorizontalDivider()
        ImportantEntry("Provided Total", deduction.deductionProvidedTotal, valueColor = MaterialTheme.colorScheme.error)
        ImportantEntry("Expected Total", deduction.deductionExpectedSum, valueColor = MaterialTheme.colorScheme.error)
    }
}

@Composable
fun NetSalaryCard(
    net: NetSalary,
    modifier: Modifier = Modifier,
) {
    SalaryContainer(
        title = "Net Salary",
        modifier = modifier,
    ) {
        NormalEntry("Total", net.netProvidedTotal)
        NormalEntry("Rounded Total", net.netProvidedRounded)
        NormalEntry("Rounding Adjustment", net.netProvidedRounding)
    }
}

@Composable
private fun SalaryContainer(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Card(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 12.dp),
            )
            Column(content = content)
        }
    }
}

@Composable
private fun NormalEntry(
    label: String,
    value: Int,
    modifier: Modifier = Modifier,
    labelColor: Color = MaterialTheme.colorScheme.onSurface,
    valueColor: Color = MaterialTheme.colorScheme.onSurface,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = labelColor,
        )
        Text(
            modifier = Modifier,
            text = value.toString(),
            style = MaterialTheme.typography.bodyMedium,
            color = valueColor,
        )
    }
}

@Composable
private fun ImportantEntry(
    label: String,
    value: Int,
    modifier: Modifier = Modifier,
    labelColor: Color = MaterialTheme.colorScheme.onSurface,
    valueColor: Color = MaterialTheme.colorScheme.primary,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = labelColor,
        )
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = value.toString(),
            style = MaterialTheme.typography.titleMedium,
            color = valueColor,
        )
    }
}
