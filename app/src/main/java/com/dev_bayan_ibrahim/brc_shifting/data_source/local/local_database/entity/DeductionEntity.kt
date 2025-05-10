package com.dev_bayan_ibrahim.brc_shifting.data_source.local.local_database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.dev_bayan_ibrahim.brc_shifting.domain.model.EmployeeDeduction
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

@Entity(
    tableName = DeductionEntity.DEDUCTION_TABLE,
    foreignKeys = [ForeignKey(
        entity = EmployeeEntity::class,
        parentColumns = [EmployeeEntity.EMPLOYEE_NUMBER],
        childColumns = [DeductionEntity.DEDUCTION_EMPLOYEE_NUMBER],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = [DeductionEntity.DEDUCTION_EMPLOYEE_NUMBER])]
)
data class DeductionEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = DEDUCTION_ID)
    val id: Long? = null,
    @ColumnInfo(name = DEDUCTION_EMPLOYEE_NUMBER)
    val employeeNumber: Int,
    @ColumnInfo(name = DEDUCTION_MONTH)
    val monthNumber: Int,
    @ColumnInfo(name = DEDUCTION_YEAR)
    val year: Int,
    @ColumnInfo(name = DEDUCTION_NAME)
    val name: String,
    @ColumnInfo(name = DEDUCTION_TOTAL)
    val total: Int,
    @ColumnInfo(name = DEDUCTION_REMAINING)
    val remaining: Int,
    @ColumnInfo(name = DEDUCTION_MONTHLY_INSTALLMENT)
    val monthlyInstallment: Int,
    @ColumnInfo(name = DEDUCTION_RECIPE_NUMBER)
    val employRecipeNumber: String,
    @ColumnInfo(name = DEDUCTION_CREATED_AT)
    val createdAt: Instant,
    @ColumnInfo(name = DEDUCTION_UPDATED_AT)
    val updatedAt: Instant,
) {
    companion object {
        const val DEDUCTION_TABLE = "employee_deduction"
        const val DEDUCTION_ID = "deduction_id"
        const val DEDUCTION_EMPLOYEE_NUMBER = "deduction_employee_number"
        const val DEDUCTION_MONTH = "deduction_month"
        const val DEDUCTION_YEAR = "deduction_year"
        const val DEDUCTION_NAME = "deduction_name"
        const val DEDUCTION_TOTAL = "deduction_total"
        const val DEDUCTION_REMAINING = "deduction_remaining"
        const val DEDUCTION_MONTHLY_INSTALLMENT = "deduction_monthly_installment"
        const val DEDUCTION_RECIPE_NUMBER = "deduction_recipe_number"
        const val DEDUCTION_CREATED_AT = "deduction_created_at"
        const val DEDUCTION_UPDATED_AT = "deduction_updated_at"
    }
}

// Mappers
fun DeductionEntity.toEmployeeDeduction() = EmployeeDeduction(
    id = id,
    employeeNumber = employeeNumber,
    monthNumber = monthNumber,
    year = year,
    createdAt = createdAt,
    updatedAt = updatedAt,
    name = name,
    total = total,
    remaining = remaining,
    monthlyInstallment = monthlyInstallment,
    employRecipeNumber = employRecipeNumber
)

fun EmployeeDeduction.toEntity(overrideUpdatedAt: Boolean = true, overrideUpdatedAtIfNew: Boolean = false) = DeductionEntity(
    id = id,
    employeeNumber = employeeNumber,
    monthNumber = monthNumber,
    year = year,
    name = name,
    total = total,
    remaining = remaining,
    monthlyInstallment = monthlyInstallment,
    employRecipeNumber = employRecipeNumber,
    createdAt = createdAt,
    updatedAt = if (overrideUpdatedAt && (overrideUpdatedAtIfNew || id != null)) Clock.System.now() else updatedAt
)