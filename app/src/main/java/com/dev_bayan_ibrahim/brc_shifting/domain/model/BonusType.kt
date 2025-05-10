package com.dev_bayan_ibrahim.brc_shifting.domain.model

import com.dev_bayan_ibrahim.brc_shifting.domain.model.core.Stringifiable

/**
 * Enumerates the different types of bonuses.
 */
enum class BonusType(override val key: String): Stringifiable {
    /**
     * A general reward or bonus.
     *
     * (Arabic: مكافئة)
     */
    Reward("1"),

    /**
     * A bonus related to illness or sick leave.
     *
     * (Arabic: مرضية)
     */
    SickLeaveBonus("3"),

    /**
     * Incentives or performance-based bonuses.
     *
     * (Arabic: حوافز)
     */
    Incentive("11"),

    /**
     * Overtime pay or bonus for extra work.
     *
     * (Arabic: إضافي)
     */
    Overtime("12"),

    /**
     * A grant or gift bonus.
     *
     * (Arabic: منحة)
     */
    Grant("2"),

    Unknown("-1");

    companion object {
        /**
         * Returns the BonusType corresponding to the given KIND_MOK value.
         *
         * @param key The KIND_MOK value as a String.
         * @return The corresponding BonusType, or null if not found.
         */
        fun ofKey(key: String?): BonusType {
            return entries.firstOrNull {
                it.key == key
            } ?: Unknown
        }
    }
}