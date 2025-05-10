package com.dev_bayan_ibrahim.brc_shifting.domain.model.salary

import com.dev_bayan_ibrahim.brc_shifting.domain.model.core.EmployeeRelated
import com.dev_bayan_ibrahim.brc_shifting.domain.model.core.Identified
import com.dev_bayan_ibrahim.brc_shifting.domain.model.core.MonthRelated
import com.dev_bayan_ibrahim.brc_shifting.domain.model.core.Syncable
import kotlinx.datetime.Instant

/**
 * Calculates final net salary using components from other classes
 * @property netProvidedTotal the provided and precalculated total.
 * @property netProvidedRounded the provided and precalculated rounded.
 */
interface NetSalary {
    val netProvidedTotal: Int
    val netProvidedRounded: Int
    val netProvidedRounding: Int

}

data class NetSalaryDelegate(
    override val netProvidedTotal: Int,
    override val netProvidedRounded: Int,
    override val netProvidedRounding: Int,
) : NetSalary

/**
 * net salary with extra metadata:
 * - [EmployeeRelated]
 * - [Syncable]
 * - [MonthRelated]
 * - [Identified]
 */
interface EmployeeNetSalary : NetSalary, EmployeeRelated, Syncable, MonthRelated, Identified

/**
 * delegate of [EmployeeNetSalary]
 */
data class EmployeeNetSalaryDelegate(
    override val employeeNumber: Int,
    override val netProvidedTotal: Int,
    override val netProvidedRounded: Int,
    override val netProvidedRounding: Int,
    override val createdAt: Instant,
    override val updatedAt: Instant,
    override val monthNumber: Int,
    override val year: Int,
    override val id: Long?,
) : EmployeeNetSalary
