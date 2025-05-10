package com.dev_bayan_ibrahim.brc_shifting.domain.model

import com.dev_bayan_ibrahim.brc_shifting.data_source.remote.endpoint.EndPointMonthVariance
import com.dev_bayan_ibrahim.brc_shifting.domain.model.core.EmployeeRelated
import com.dev_bayan_ibrahim.brc_shifting.domain.model.core.Identified
import com.dev_bayan_ibrahim.brc_shifting.domain.model.core.MonthRelated
import com.dev_bayan_ibrahim.brc_shifting.domain.model.core.Syncable
import com.dev_bayan_ibrahim.brc_shifting.domain.model.remote.RemoteDeductions
import com.dev_bayan_ibrahim.brc_shifting.util.months
import com.dev_bayan_ibrahim.brc_shifting.util.now
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.minus

/**
 * total deductions of a month for a specific employee
 * @see [Deduction]
 */
data class MonthDeductions(
    override val employeeNumber: Int,
    override val monthNumber: Int,
    override val year: Int,
    val deductions: List<Deduction>,
) : MonthRelated, EmployeeRelated

/**
 * @param name name of the deduction (dialysis name)
 * @param total total amount of the deduction
 * @param monthlyInstallment installment of this deduction this month
 * @param remaining remain value of the deduction
 * @param employRecipeNumber the recipe value of this employ for this deduction
 */

open class Deduction(
    override val createdAt: Instant,
    override val updatedAt: Instant,
    override val id: Long? = null,
    open val name: String,
    open val total: Int,
    open val remaining: Int,
    open val monthlyInstallment: Int,
    open val employRecipeNumber: String,
) : Syncable, Identified {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Deduction) return false

        if (createdAt != other.createdAt) return false
        if (updatedAt != other.updatedAt) return false
        if (id != other.id) return false
        if (name != other.name) return false
        if (total != other.total) return false
        if (remaining != other.remaining) return false
        if (monthlyInstallment != other.monthlyInstallment) return false
        if (employRecipeNumber != other.employRecipeNumber) return false

        return true
    }

    override fun hashCode(): Int {
        var result = createdAt.hashCode()
        result = 31 * result + updatedAt.hashCode()
        result = 31 * result + (id?.hashCode() ?: 0)
        result = 31 * result + name.hashCode()
        result = 31 * result + total
        result = 31 * result + remaining
        result = 31 * result + monthlyInstallment
        result = 31 * result + employRecipeNumber.hashCode()
        return result
    }

    override fun toString(): String {
        return "Deduction(createdAt=$createdAt, updatedAt=$updatedAt, id=$id, name='$name', total=$total, remaining=$remaining, monthlyInstallment=$monthlyInstallment, employRecipeNumber='$employRecipeNumber')"
    }
}

/**
 * a deduction of a specific employee in a specific date
 * @see [Deduction]
 */
data class EmployeeDeduction(
    override val employeeNumber: Int,
    override val monthNumber: Int,
    override val year: Int,
    override val createdAt: Instant,
    override val updatedAt: Instant,
    override val name: String,
    override val total: Int,
    override val remaining: Int,
    override val id: Long? = null,
    override val monthlyInstallment: Int,
    override val employRecipeNumber: String,
) : MonthRelated, EmployeeRelated, Deduction(
    createdAt = createdAt,
    updatedAt = updatedAt,
    name = name,
    total = total,
    remaining = remaining,
    monthlyInstallment = monthlyInstallment,
    employRecipeNumber = employRecipeNumber
)

fun RemoteDeductions.toMonthDeduction(
    employeeNumber: Int,
    monthVariance: EndPointMonthVariance,
    now: LocalDate = LocalDateTime.now().date,
): MonthDeductions = toMonthDeduction(
    employeeNumber = employeeNumber,
    monthNumber = now.let {
        when (monthVariance) {
            EndPointMonthVariance.ThisMonth -> it
            EndPointMonthVariance.PreviousMonth -> it.minus(1.months)
        }
    }.monthNumber,
    year = now.year,
)

fun RemoteDeductions.toMonthDeduction(
    employeeNumber: Int,
    monthNumber: Int,
    year: Int,
): MonthDeductions {
    val now = Clock.System.now()
    return MonthDeductions(
        employeeNumber = employeeNumber,
        monthNumber = monthNumber,
        year = year,
        deductions = data.map {
            Deduction(
                createdAt = now,
                updatedAt = now,
                name = it.name,
                total = it.total.toInt(),
                remaining = it.remaining.toInt(),
                monthlyInstallment = it.monthlyInstallment.toInt(),
                employRecipeNumber = it.employeeNumber,
            )
        }
    )
}
fun MonthDeductions.toEmployeeDeductions(): List<EmployeeDeduction> {
    return deductions.map { deduction->
        EmployeeDeduction(
            employeeNumber = this.employeeNumber,
            monthNumber = this.monthNumber,
            year = this.year,
            createdAt = deduction.createdAt,
            updatedAt = deduction.updatedAt,
            name = deduction.name,
            total = deduction.total,
            remaining = deduction.remaining,
            monthlyInstallment = deduction.monthlyInstallment,
            employRecipeNumber = deduction.employRecipeNumber
        )
    }
}