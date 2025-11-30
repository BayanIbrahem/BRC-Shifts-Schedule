package com.dev_bayan_ibrahim.brc_shifting.ui.screen.salary.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dev_bayan_ibrahim.brc_shifting.R
import com.dev_bayan_ibrahim.brc_shifting.domain.model.Deduction
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
    deductions: List<Deduction>,
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
        ) // not expandable
        NetSalaryCard(
            net = salary,
            modifier = Modifier.fillMaxWidth(),
        ) // expandable, has a trailing head main value with netProvidedRounded
        BaseSalaryCard(
            baseSalary = salary,
            modifier = Modifier.fillMaxWidth(),
        ) // expandable, has a trailing main value of baseSalary
        SalaryAllowanceCard(
            allowance = salary,
            modifier = Modifier.fillMaxWidth(),
        ) // expandable, has a trailing head main value with allowanceProvidedTotal
        SalaryDeductionCard(
            deduction = salary,
            modifier = Modifier.fillMaxWidth(),
        ) // expandable, has a trailing head main value with deductionProvidedTotal
        // add a card of deductions which has a main value of deductions sum (of monthly installment), and each value line is the deduction monthly installment
    }
}

@Composable
private fun SalaryEmployeeCard(
    employee: Employee,
    salaryDate: LocalDate,

    modifier: Modifier = Modifier,
) {
    SalaryContainer(
        title = stringResource(R.string.employee),
        modifier = modifier,
    ) {
        Text(stringResource(R.string.name_x, employee.name), style = MaterialTheme.typography.bodyMedium)
        Text(stringResource(R.string.number_x, employee.employeeNumber), style = MaterialTheme.typography.bodyMedium)
        Text(stringResource(R.string.salary_x, salaryDate.monthYearFormat()), style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun BaseSalaryCard(
    baseSalary: BaseSalary,
    modifier: Modifier = Modifier,
) {
    SalaryContainer(
        title = stringResource(R.string.base_salary),
        modifier = modifier,
    ) {
        NormalEntry(stringResource(R.string.base), baseSalary.baseSalary)
        NormalEntry(stringResource(R.string.insurance), baseSalary.insurance)
        NormalEntry(stringResource(R.string.salary_compensation), baseSalary.salaryCompensation)
        NormalEntry(stringResource(R.string.illness_days_off), baseSalary.illnessDaysOffCount)
        NormalEntry(stringResource(R.string.punishment_days_off), baseSalary.punishmentDaysOff)
        NormalEntry(stringResource(R.string.children), baseSalary.childrenCount)
        NormalEntry(stringResource(R.string.wives), baseSalary.wivesCount)
    }
}

@Composable
fun SalaryAllowanceCard(
    allowance: SalaryAllowance,
    modifier: Modifier = Modifier,
) {
    SalaryContainer(
        title = stringResource(R.string.salary_allowances),
        modifier = modifier,
    ) {
        NormalEntry(stringResource(R.string.basic_allowance), allowance.basicAllowance)
        NormalEntry(stringResource(R.string.family_compensation), allowance.familyCompensation)
        NormalEntry(stringResource(R.string.management_bonus), allowance.managementBonus)
        NormalEntry(stringResource(R.string.responsibility_allowance), allowance.responsibilityAllowance)
        NormalEntry(stringResource(R.string.specialist_bonus), allowance.specialistBonus)
        NormalEntry(stringResource(R.string.position_allowance), allowance.positionAllowance)
        NormalEntry(stringResource(R.string.general_compensations), allowance.generalCompensations)
        NormalEntry(stringResource(R.string.overtime_pay), allowance.overtimePay)
        NormalEntry(stringResource(R.string.extra_hours_pay), allowance.extraHoursPay)
        NormalEntry(stringResource(R.string.committee_bonus), allowance.committeeBonus)
        NormalEntry(stringResource(R.string.competence_allowance), allowance.competenceAllowance)
        NormalEntry(stringResource(R.string.effort_bonus), allowance.effortBonus)
        NormalEntry(stringResource(R.string.warming_allowance), allowance.warmingAllowance)
        NormalEntry(stringResource(R.string.rounding_adjustment), allowance.salaryRoundingAdjustment)
        HorizontalDivider()
        ImportantEntry(stringResource(R.string.provided_total), allowance.allowanceProvidedTotal)
        ImportantEntry(stringResource(R.string.expected_total), allowance.allowanceExpectedSum)
    }
}

@Composable
fun SalaryDeductionCard(
    deduction: SalaryDeduction,
    modifier: Modifier = Modifier,
) {
    SalaryContainer(
        title = stringResource(R.string.salary_deductions),
        modifier = modifier,
    ) {
        NormalEntry(stringResource(R.string.social_insurance), deduction.socialInsurance)
        NormalEntry(stringResource(R.string.salary_insurance), deduction.salaryInsurance)
        NormalEntry(stringResource(R.string.financial_commitments), deduction.financialCommitments)
        NormalEntry(stringResource(R.string.income_tax), deduction.incomeTax)
        NormalEntry(stringResource(R.string.engineer_association), deduction.engineerAssociation)
        NormalEntry(stringResource(R.string.freedom_organization), deduction.freedomOrganization)
        NormalEntry(stringResource(R.string.workers_union), deduction.workersUnion)
        NormalEntry(stringResource(R.string.charity_box), deduction.charityBox)
        NormalEntry(stringResource(R.string.agricultural_association), deduction.agriculturalAssociation)
        NormalEntry(stringResource(R.string.ministry_fund), deduction.ministryFund)
        NormalEntry(stringResource(R.string.solidarity_tax), deduction.solidarityTax)
        HorizontalDivider()
        ImportantEntry(stringResource(R.string.provided_total), deduction.deductionProvidedTotal, valueColor = MaterialTheme.colorScheme.error)
        ImportantEntry(stringResource(R.string.expected_total), deduction.deductionExpectedSum, valueColor = MaterialTheme.colorScheme.error)
    }
}

@Composable
fun NetSalaryCard(
    net: NetSalary,
    modifier: Modifier = Modifier,
) {
    SalaryContainer(
        title = stringResource(R.string.net_salary),
        modifier = modifier,
    ) {
        NormalEntry(stringResource(R.string.total), net.netProvidedTotal)
        NormalEntry(stringResource(R.string.rounded_total), net.netProvidedRounded)
        NormalEntry(stringResource(R.string.rounding_adjustment), net.netProvidedRounding)
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
