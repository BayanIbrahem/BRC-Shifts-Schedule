package com.dev_bayan_ibrahim.brc_shifting.domain.model

import com.dev_bayan_ibrahim.brc_shifting.domain.model.DayOff.CompensatoryRestDay
import com.dev_bayan_ibrahim.brc_shifting.domain.model.DayOff.HolidayCompensationDay
import com.dev_bayan_ibrahim.brc_shifting.domain.model.DayOff.HourlyLeave
import com.dev_bayan_ibrahim.brc_shifting.domain.model.DayOff.Standard
import com.dev_bayan_ibrahim.brc_shifting.domain.model.core.DateRelated
import com.dev_bayan_ibrahim.brc_shifting.domain.model.core.EmployeeRelated
import com.dev_bayan_ibrahim.brc_shifting.domain.model.core.Identified
import com.dev_bayan_ibrahim.brc_shifting.domain.model.core.Syncable
import com.dev_bayan_ibrahim.brc_shifting.domain.model.remote.RemoteDayOffs
import com.dev_bayan_ibrahim.brc_shifting.domain.model.util.SimpleDate
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

/**
 * day off of a specific employee
 * @property employeeNumber number of employee
 * @property date date of day off
 * @property workDate **previous extra work day** some day offs are instead of extra work day, this value is
 * not null for [CompensatoryRestDay] and [HolidayCompensationDay]
 * @property type type of this day-off see [DayOffType]
 * @property period the amount (in days) of this day-off, values are 0.25 (for [HourlyLeave]), 2 or 3 for non [Standard]
 * and 1 for [Standard], [HolidayCompensationDay], [CompensatoryRestDay]
 */
sealed interface DayOff : EmployeeRelated, DateRelated, Syncable, Identified {
    override val employeeNumber: Int
    override val date: SimpleDate
    override val createdAt: Instant
    override val updatedAt: Instant
    override val id: Long?
    val workDate: SimpleDate?
    val type: DayOffType
    val period: Float

    /**
     * Standard, scheduled leave for general purposes.
     * This represents a typical day off, planned and approved in advance.
     */
    data class Standard(
        override val employeeNumber: Int,
        override val date: SimpleDate,
        override val createdAt: Instant,
        override val updatedAt: Instant,
        override val id: Long? = null,
        val days: Int,
    ) : DayOff {
        override val period: Float
            get() = days.toFloat()
        override val workDate: SimpleDate?
            get() = null
        override val type: DayOffType
            get() = DayOffType.Standard
    }

    /**
     * Compensatory rest day granted in lieu of extra work performed on a regular work day or personal day off (e.g., working on a Friday).
     */
    data class CompensatoryRestDay(
        override val employeeNumber: Int,
        override val date: SimpleDate,
        override val createdAt: Instant,
        override val updatedAt: Instant,
        override val workDate: SimpleDate,
        override val id: Long? = null,
        val days: Int,
    ) : DayOff {
        override val period: Float
            get() = days.toFloat()
        override val type: DayOffType
            get() = DayOffType.CompensatoryRestDay
    }

    /**
     * Compensatory day off granted for work performed on an internationally recognized holiday (e.g., Christmas).
     */
    data class HolidayCompensationDay(
        override val employeeNumber: Int,
        override val date: SimpleDate,
        override val createdAt: Instant,
        override val updatedAt: Instant,
        override val workDate: SimpleDate,
        override val id: Long? = null,
        val days: Int,
    ) : DayOff {
        override val period: Float
            get() = days.toFloat()
        override val type: DayOffType
            get() = DayOffType.HolidayCompensationDay
    }

    /**
     * Short leave granted for a few hours, typically for personal appointments or errands.
     */
    data class HourlyLeave(
        override val employeeNumber: Int,
        override val createdAt: Instant,
        override val updatedAt: Instant,
        override val date: SimpleDate,
        override val id: Long? = null,
    ) : DayOff {
        override val period: Float
            get() = 0.25f
        override val workDate: SimpleDate?
            get() = null
        override val type: DayOffType
            get() = DayOffType.HourlyLeave
    }
}

fun RemoteDayOffs.toDayOffs(
    employeeNumber: Int,
): List<DayOff> {
    return data.map { dayOff ->
        val type = DayOffType.ofKey(dayOff.type)
        val now = Clock.System.now()
        when (type) {
            DayOffType.CompensatoryRestDay -> DayOff.CompensatoryRestDay(
                employeeNumber = employeeNumber,
                date = SimpleDate(dayOff.date),
                createdAt = now,
                updatedAt = now,
                workDate = SimpleDate(dayOff.workDate),
                days = dayOff.period.toInt(),
            )

            DayOffType.HolidayCompensationDay -> DayOff.HolidayCompensationDay(
                employeeNumber = employeeNumber,
                date = SimpleDate(dayOff.date),
                createdAt = now,
                updatedAt = now,
                workDate = SimpleDate(dayOff.workDate),
                days = dayOff.period.toInt(),
            )

            DayOffType.HourlyLeave -> DayOff.HourlyLeave(
                employeeNumber = employeeNumber,
                createdAt = now,
                updatedAt = now,
                date = SimpleDate(dayOff.date),
            )

            DayOffType.Standard, null -> DayOff.Standard(
                employeeNumber = employeeNumber,
                createdAt = now,
                updatedAt = now,
                date = SimpleDate(dayOff.date),
                days = dayOff.period.toInt(),
            )
        }
    }
}

