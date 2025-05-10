package com.dev_bayan_ibrahim.brc_shifting.domain.model

import com.dev_bayan_ibrahim.brc_shifting.domain.model.core.EmployeeRelated
import com.dev_bayan_ibrahim.brc_shifting.domain.model.core.Identified
import com.dev_bayan_ibrahim.brc_shifting.domain.model.core.Syncable
import com.dev_bayan_ibrahim.brc_shifting.domain.model.remote.RemoteEmploy
import com.dev_bayan_ibrahim.brc_shifting.util.WorkGroup
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

/**
 * @param employeeNumber unique id of the employ
 * @param remoteId remote id
 * @param name full name (display name)
 * @param group which work group this employ belong to
 */
data class Employee(
    override val employeeNumber: Int,
    override val createdAt: Instant,
    override val updatedAt: Instant,
    val name: String,
    val remoteId: String,
    val group: WorkGroup?,
) : EmployeeRelated, Syncable, Identified {
    override val id: Long get() = employeeNumber.toLong()
}

@Throws(IllegalArgumentException::class)
fun RemoteEmploy.toEmployee(
    group: WorkGroup? = null,
): Employee {
    val now = Clock.System.now()
    return Employee(
        employeeNumber = this.data?.userNumber?.toInt() ?: throw IllegalArgumentException("null remote employ data"),
        createdAt = now,
        updatedAt = now,
        name = this.data.fullName,
        remoteId = this.data.id,
        group = group,
    )
}