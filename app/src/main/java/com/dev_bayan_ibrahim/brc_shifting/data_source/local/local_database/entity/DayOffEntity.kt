package com.dev_bayan_ibrahim.brc_shifting.data_source.local.local_database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.dev_bayan_ibrahim.brc_shifting.domain.model.DayOff
import com.dev_bayan_ibrahim.brc_shifting.domain.model.DayOffType
import com.dev_bayan_ibrahim.brc_shifting.domain.model.util.SimpleDate
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

@Entity(
    tableName = DayOffEntity.DAY_OFF_TABLE,
    foreignKeys = [ForeignKey(
        entity = EmployeeEntity::class,
        parentColumns = [EmployeeEntity.EMPLOYEE_NUMBER],
        childColumns = [DayOffEntity.DAY_OFF_EMPLOYEE_NUMBER],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = [DayOffEntity.DAY_OFF_EMPLOYEE_NUMBER])]
)
data class DayOffEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = DAY_OFF_ID)
    val id: Long? = null,
    @ColumnInfo(name = DAY_OFF_EMPLOYEE_NUMBER)
    val employeeNumber: Int,
    @ColumnInfo(name = DAY_OFF_DATE)
    val date: SimpleDate,
    @ColumnInfo(name = DAY_OFF_TYPE)
    val type: DayOffType,
    @ColumnInfo(name = DAY_OFF_WORK_DATE)
    val workDate: SimpleDate?,
    @ColumnInfo(name = DAY_OFF_DAYS)
    val days: Int?,
    @ColumnInfo(name = DAY_OFF_PERIOD)
    val period: Float,
    @ColumnInfo(name = DAY_OFF_CREATED_AT)
    val createdAt: Instant,
    @ColumnInfo(name = DAY_OFF_UPDATED_AT)
    val updatedAt: Instant,
) {
    companion object {
        const val DAY_OFF_TABLE = "day_off"
        const val DAY_OFF_ID = "day_off_id"
        const val DAY_OFF_EMPLOYEE_NUMBER = "day_off_employee_number"
        const val DAY_OFF_DATE = "day_off_date"
        const val DAY_OFF_TYPE = "day_off_type"
        const val DAY_OFF_WORK_DATE = "day_off_work_date"
        const val DAY_OFF_DAYS = "day_off_days"
        const val DAY_OFF_PERIOD = "day_off_period"
        const val DAY_OFF_CREATED_AT = "day_off_created_at"
        const val DAY_OFF_UPDATED_AT = "day_off_updated_at"
    }
}

// Mappers
fun DayOffEntity.toDayOff() = when (type) {
    DayOffType.Standard -> DayOff.Standard(
        employeeNumber = employeeNumber,
        date = date,
        days = days ?: 1,
        id = id,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

    DayOffType.CompensatoryRestDay -> DayOff.CompensatoryRestDay(
        employeeNumber = employeeNumber,
        date = date,
        workDate = workDate ?: throw IllegalArgumentException("Missing workDate for CompensatoryRestDay"),
        days = days ?: 1,
        id = id,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

    DayOffType.HolidayCompensationDay -> DayOff.HolidayCompensationDay(
        employeeNumber = employeeNumber,
        date = date,
        workDate = workDate ?: throw IllegalArgumentException("Missing workDate for HolidayCompensationDay"),
        days = days ?: 1,
        createdAt = createdAt,
        updatedAt = updatedAt,
        id = id,
    )

    DayOffType.HourlyLeave -> DayOff.HourlyLeave(
        employeeNumber = employeeNumber,
        date = date,
        createdAt = createdAt,
        updatedAt = updatedAt,
        id = id,
    )
}

fun DayOff.toEntity(overrideUpdatedAt: Boolean = true, overrideUpdatedAtIfNew: Boolean = false) = DayOffEntity(
    id = this.id,
    employeeNumber = employeeNumber,
    date = date,
    type = type,
    workDate = when (this) {
        is DayOff.CompensatoryRestDay -> workDate
        is DayOff.HolidayCompensationDay -> workDate
        else -> null
    },
    days = when (this) {
        is DayOff.Standard -> days
        is DayOff.CompensatoryRestDay -> days
        is DayOff.HolidayCompensationDay -> days
        else -> null
    },
    period = period,
    createdAt = createdAt,
    updatedAt = if (overrideUpdatedAt && (overrideUpdatedAtIfNew || id != null)) Clock.System.now() else updatedAt
)