package com.dev_bayan_ibrahim.brc_shifting.domain.repo

import com.dev_bayan_ibrahim.brc_shifting.domain.model.Bonus
import com.dev_bayan_ibrahim.brc_shifting.domain.model.Employee
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

interface BonusRepo {
    suspend fun getEmployee(employeeNumber: Int): Employee?
    fun getEmployeeBonus(employeeNumber: Int): Flow<List<Bonus>>
    suspend fun getEmployeeRemoteBonus(employeeNumber: Int): Result<List<Bonus>>
    suspend fun deleteBonus(id: Long): Result<Boolean>
    suspend fun lastBonusFetch(employeeNumber: Int): Result<Instant?>
    suspend fun deleteBonusList(bonusList: Set<Long>): Result<Unit>
    suspend fun deleteEmployeeBonus(employeeNumber: Int): Result<Unit>
}