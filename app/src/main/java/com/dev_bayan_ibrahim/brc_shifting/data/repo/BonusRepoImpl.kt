package com.dev_bayan_ibrahim.brc_shifting.data.repo

import com.dev_bayan_ibrahim.brc_shifting.data_source.local.LocalDataSource
import com.dev_bayan_ibrahim.brc_shifting.data_source.remote.RemoteDataSource
import com.dev_bayan_ibrahim.brc_shifting.domain.model.Bonus
import com.dev_bayan_ibrahim.brc_shifting.domain.model.Employee
import com.dev_bayan_ibrahim.brc_shifting.domain.model.util.asResult
import com.dev_bayan_ibrahim.brc_shifting.domain.repo.BonusRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class BonusRepoImpl(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
) : BonusRepo {
    override suspend fun getEmployee(employeeNumber: Int): Employee? {
        return localDataSource.getEmployee(employeeNumber).asResult().firstOrNull()?.getOrNull()
    }

    override fun getEmployeeBonus(employeeNumber: Int): Flow<List<Bonus>> {
        return localDataSource.getEmployeeBonus(employeeNumber)
    }

    override suspend fun getEmployeeRemoteBonus(employeeNumber: Int): Result<List<Bonus>> {
        val result = remoteDataSource.getBonus(employeeNumber)
        result.fold(
            onFailure = {
                return result
            },
            onSuccess = { bonuses ->
                val now = Clock.System.now()
                val newBonuses = bonuses.map {
                    it.copy(createdAt = now, updatedAt = now)
                }
                val newIds = localDataSource.insertBonusList(bonuses)
                val newBonusesWithIds = newBonuses.mapIndexed { i, it ->
                    it.copy(id = newIds[i])
                }
                return Result.success(newBonusesWithIds)
            }
        )
    }

    override suspend fun deleteBonus(id: Long): Result<Boolean> {
        return localDataSource.deleteBonus(id).map { true }
    }

    override suspend fun lastBonusFetch(employeeNumber: Int): Result<Instant?> {
        return localDataSource.getEmployeeBonusLastFetch(employeeNumber).asResult().firstOrNull() ?: Result.success(null)
    }

    override suspend fun deleteBonusList(bonusList: Set<Long>): Result<Unit> {
        return localDataSource.deleteBonus(bonusList)
    }

    override suspend fun deleteEmployeeBonus(employeeNumber: Int): Result<Unit> {
        return localDataSource.deleteBonus(employeeNumber)
    }
}