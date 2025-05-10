package com.dev_bayan_ibrahim.brc_shifting.domain.model

import com.dev_bayan_ibrahim.brc_shifting.domain.model.core.Stringifiable

/**
 * Enumerates the different types of day-off or leave available to employees.
 */
enum class DayOffType(override val key: String) : Stringifiable {
    /**
     * Standard, scheduled leave for general purposes.
     * This represents a typical day off, planned and approved in advance.
     *
     * Arabic: إجازة إدارية
     */
    Standard("0"),

    /**
     * Compensatory rest day granted in lieu of extra work performed on a regular work day or personal day off (e.g., working on a Friday).
     *
     * Arabic: بدل راحة
     */
    CompensatoryRestDay("1"),

    /**
     * Compensatory day off granted for work performed on an internationally recognized holiday (e.g., Christmas).
     *
     * Arabic: بدل تعطيل
     */
    HolidayCompensationDay("3"),

    /**
     * Short leave granted for a few hours, typically for personal appointments or errands.
     *
     * Arabic: إجازة ساعية
     */
    HourlyLeave("4");

    companion object Companion {
        fun ofKey(key: String?) = entries.firstOrNull { it.key == key }
    }
}
