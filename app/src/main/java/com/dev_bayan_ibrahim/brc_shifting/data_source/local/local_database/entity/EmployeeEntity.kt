package com.dev_bayan_ibrahim.brc_shifting.data_source.local.local_database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dev_bayan_ibrahim.brc_shifting.domain.model.Employee
import com.dev_bayan_ibrahim.brc_shifting.util.WorkGroup
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

@Entity(tableName = EmployeeEntity.EMPLOYEE_TABLE)
data class EmployeeEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = EMPLOYEE_NUMBER)
    val employeeNumber: Int,
    @ColumnInfo(name = EMPLOYEE_NAME)
    val name: String,
    @ColumnInfo(name = EMPLOYEE_REMOTE_ID)
    val remoteId: String,
    @ColumnInfo(name = EMPLOYEE_GROUP)
    val group: WorkGroup?,
    @ColumnInfo(name = EMPLOYEE_CREATED_AT)
    val createdAt: Instant,
    @ColumnInfo(name = EMPLOYEE_UPDATED_AT)
    val updatedAt: Instant,
) {
    companion object {
        const val EMPLOYEE_TABLE = "employee"
        const val EMPLOYEE_NUMBER = "employee_number"
        const val EMPLOYEE_NAME = "employee_name"
        const val EMPLOYEE_REMOTE_ID = "employee_remote_id"
        const val EMPLOYEE_GROUP = "employee_group"
        const val EMPLOYEE_CREATED_AT = "employee_created_at"
        const val EMPLOYEE_UPDATED_AT = "employee_updated_at"
    }
}

// Mappers
fun EmployeeEntity.toEmployee() = Employee(
    name = name,
    remoteId = remoteId,
    employeeNumber = employeeNumber,
    group = group,
    createdAt = createdAt,
    updatedAt = updatedAt,
)

fun Employee.toEntity(overrideUpdatedAt: Boolean = true) = EmployeeEntity(
    employeeNumber = employeeNumber,
    name = name,
    remoteId = remoteId,
    group = group,
    createdAt = createdAt,
    updatedAt = if (overrideUpdatedAt) Clock.System.now() else updatedAt,
)