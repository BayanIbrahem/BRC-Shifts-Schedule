package com.dev_bayan_ibrahim.brc_shifting.domain.model

import androidx.annotation.StringRes
import com.dev_bayan_ibrahim.brc_shifting.R
import com.dev_bayan_ibrahim.brc_shifting.domain.model.core.Stringifiable

/**
 * Enumerates the different types of bonuses.
 */
enum class BonusType(
    override val key: String,
    @StringRes val nameRes: Int,
) : Stringifiable {
    /**
     * A general reward or bonus.
     *
     * (Arabic: مكافئة)
     */
    Reward("1", R.string.bonus_reward),

    /**
     * A bonus related to illness or sick leave.
     *
     * (Arabic: مرضية)
     */
    SickLeaveBonus("3", R.string.bonus_sick_leave),

    /**
     * Incentives or performance-based bonuses.
     *
     * (Arabic: حوافز)
     */
    Incentive("11", R.string.bonus_Incentive),

    /**
     * Overtime pay or bonus for extra work.
     *
     * (Arabic: إضافي)
     */
    Overtime("12", R.string.bonus_overtime),

    /**
     * A grant or gift bonus.
     *
     * (Arabic: منحة)
     */
    Grant("2", R.string.bonus_grant),

    Unknown("-1", R.string.bonus_unknown);

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