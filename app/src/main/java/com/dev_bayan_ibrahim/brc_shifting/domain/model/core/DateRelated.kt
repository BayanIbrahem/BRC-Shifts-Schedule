package com.dev_bayan_ibrahim.brc_shifting.domain.model.core

import com.dev_bayan_ibrahim.brc_shifting.domain.model.util.SimpleDate


/**
 * related to a specific date
 * @property date simple date see [SimpleDate]
 */
interface DateRelated : MonthRelated {
    val date: SimpleDate
    override val monthNumber: Int
        get() = date.monthNumber
    override val year: Int
        get() = date.year
}