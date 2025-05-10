package com.dev_bayan_ibrahim.brc_shifting.domain.model

import com.dev_bayan_ibrahim.brc_shifting.domain.model.core.DateRelated
import com.dev_bayan_ibrahim.brc_shifting.domain.model.core.EmployeeRelated
import com.dev_bayan_ibrahim.brc_shifting.domain.model.core.Identified
import com.dev_bayan_ibrahim.brc_shifting.domain.model.core.Syncable
import com.dev_bayan_ibrahim.brc_shifting.domain.model.remote.RemoteBonus
import com.dev_bayan_ibrahim.brc_shifting.domain.model.util.SimpleDate
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

/**
 * specific bonus or مكافأة
 * @param employeeNumber for which employee this is
 * @param date in which date
 * @param type type of the bonus
 * @param total total amount of the bonus (before taxes)
 * @param net net value of the bonus
 * @property tax estimated value of tax according to total and net ([total] - [net])
 */
data class Bonus(
    override val employeeNumber: Int,
    override val createdAt: Instant,
    override val updatedAt: Instant,
    override val date: SimpleDate,
    override val id: Long? = null,
    val type: BonusType,
    val total: Int,
    val net: Int,
) : EmployeeRelated, DateRelated, Syncable, Identified {
    val tax: Int
        get() = total - net
}

fun RemoteBonus.toBonuses(
    employeeNumber: Int,
): List<Bonus> = data.map {
    val now = Clock.System.now()
    Bonus(
        employeeNumber = employeeNumber,
        date = SimpleDate(it.date),
        type = BonusType.ofKey(it.type),
        total = it.total.toInt(),
        net = it.netValue.toInt(),
        createdAt = now,
        updatedAt = now,
    )
}