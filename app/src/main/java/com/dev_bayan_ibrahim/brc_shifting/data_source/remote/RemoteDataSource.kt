package com.dev_bayan_ibrahim.brc_shifting.data_source.remote

import android.util.Log
import com.dev_bayan_ibrahim.brc_shifting.data_source.remote.endpoint.EndPointMonthVariance
import com.dev_bayan_ibrahim.brc_shifting.data_source.remote.endpoint.list.BonusEndpoint
import com.dev_bayan_ibrahim.brc_shifting.data_source.remote.endpoint.list.DayOffEndpoint
import com.dev_bayan_ibrahim.brc_shifting.data_source.remote.endpoint.list.DeductionEndpoint
import com.dev_bayan_ibrahim.brc_shifting.data_source.remote.endpoint.list.EmployEndpoint
import com.dev_bayan_ibrahim.brc_shifting.data_source.remote.endpoint.list.SalaryEndpoint
import com.dev_bayan_ibrahim.brc_shifting.data_source.remote.param.bodyKeyOf
import com.dev_bayan_ibrahim.brc_shifting.data_source.remote.param.queryKeyOf
import com.dev_bayan_ibrahim.brc_shifting.data_source.remote.uri.BRCUriDirector
import com.dev_bayan_ibrahim.brc_shifting.data_source.remote.uri.UriDirector
import com.dev_bayan_ibrahim.brc_shifting.domain.model.Bonus
import com.dev_bayan_ibrahim.brc_shifting.domain.model.DayOff
import com.dev_bayan_ibrahim.brc_shifting.domain.model.Employee
import com.dev_bayan_ibrahim.brc_shifting.domain.model.MonthDeductions
import com.dev_bayan_ibrahim.brc_shifting.domain.model.exception.UnAccessibleDataException
import com.dev_bayan_ibrahim.brc_shifting.domain.model.remote.RemoteEmploy
import com.dev_bayan_ibrahim.brc_shifting.domain.model.salary.Salary
import com.dev_bayan_ibrahim.brc_shifting.domain.model.salary.toSalary
import com.dev_bayan_ibrahim.brc_shifting.domain.model.toBonuses
import com.dev_bayan_ibrahim.brc_shifting.domain.model.toDayOffs
import com.dev_bayan_ibrahim.brc_shifting.domain.model.toEmployee
import com.dev_bayan_ibrahim.brc_shifting.domain.model.toMonthDeduction
import com.dev_bayan_ibrahim.brc_shifting.util.now
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.json.Json

const val REMOTE_TAG = "REMOTE"

class RemoteDataSource(
    private val client: HttpClient,
    private val director: UriDirector = BRCUriDirector,
    private val json: Json,
) {
    suspend fun getEmploy(employNumber: Int, password: String): Result<Employee> {
        return EmployEndpoint(client, director, json).postRequest(
            bodyParams = listOf(
                "username" bodyKeyOf employNumber.toString(),
                "password" bodyKeyOf password
            ),
            queryParams = listOf(
                "type" queryKeyOf "login"
            ),
            parseResult = {
                val body = it.body<String>()
                val lastJsonClose = body.lastIndexOf('}')
                val trimmedBody = if (lastJsonClose > 0) {
                    body.removeRange(lastJsonClose.inc()..body.length.dec())
                } else {
                    body
                }
                Log.d(REMOTE_TAG, "employee(num: $employNumber, pw; $password) => $body")
                json.decodeFromString(
                    deserializer = RemoteEmploy.serializer(),
                    string = trimmedBody,
                )
            }
        ).mapCatching {
            it.toEmployee()
        }.also {
            Log.d(REMOTE_TAG, "employee(num: $employNumber, pw; $password) => $it")
        }
    }

    /**
     * @throws UnAccessibleDataException if the [monthNumber] and [year] are not valid variance in [EndPointMonthVariance]
     */
    suspend fun getSalary(employNumber: Int, monthNumber: Int, year: Int): Result<Salary> {
        val variance = getMonthVarianceOrNull(monthNumber, year)
        return if (variance != null) {
            getSalary(
                employNumber = employNumber,
                variance = variance,
            )
        } else {
            Result.failure(UnAccessibleDataException("Invalid required date (month number: $monthNumber, year: $year"))
        }.also {
            Log.d(REMOTE_TAG, "salary(num: $employNumber, month: $monthNumber, year: $year) => $it")
        }
    }

    suspend fun getSalary(
        employNumber: Int,
        variance: EndPointMonthVariance,
    ) = SalaryEndpoint(
        client = client,
        uriDirector = director,
        json = json,
        variance = variance
    ).postRequest(
        bodyParams = listOf(
            "user_id" bodyKeyOf employNumber.toString(),
        )
    ).mapCatching {
        it.toSalary(employNumber)
    }

    /**
     * @throws UnAccessibleDataException if the [monthNumber] and [year] are not valid variance in [EndPointMonthVariance]
     */
    suspend fun getDeductions(employNumber: Int, monthNumber: Int, year: Int): Result<MonthDeductions> {
        val variance = getMonthVarianceOrNull(monthNumber, year)
        return if (variance != null) {
            DeductionEndpoint(
                client = client,
                uriDirector = director,
                json = json,
                variance = variance
            ).postRequest(
                bodyParams = listOf(
                    "user_id" bodyKeyOf employNumber.toString(),
                )
            ).mapCatching {
                it.toMonthDeduction(employeeNumber = employNumber, monthVariance = variance)
            }
        } else {
            Result.failure(UnAccessibleDataException("Invalid required date (month number: $monthNumber, year: $year"))
        }.also {
            Log.d(REMOTE_TAG, "deductions(num: $employNumber, month: $monthNumber, year: $year) => $it")
        }
    }

    private fun getMonthVarianceOrNull(monthNumber: Int, year: Int): EndPointMonthVariance? {
        val now = LocalDateTime.now().date
        val requiredMonthIndex = year.times(12).plus(monthNumber)
        val nowMonthIndex = now.year.times(12).plus(now.monthNumber)
        return when (nowMonthIndex - requiredMonthIndex) {
            0 -> EndPointMonthVariance.ThisMonth
            1 -> EndPointMonthVariance.PreviousMonth
            else -> null
        }
    }

    suspend fun getDayOffs(employNumber: Int): Result<List<DayOff>> {
        return DayOffEndpoint(
            client = client,
            uriDirector = director,
            json = json,
        ).postRequest(
            bodyParams = listOf(
                "user_id" bodyKeyOf employNumber.toString(),
            )
        ).mapCatching {
            it.toDayOffs(employeeNumber = employNumber)
        }.also {
            Log.d(REMOTE_TAG, "dayOffs(num: $employNumber) => $it")
        }
    }

    suspend fun getBonus(
        employNumber: Int,
    ): Result<List<Bonus>> {
        return BonusEndpoint(
            client = client,
            uriDirector = director,
            json = json,
        ).postRequest(
            bodyParams = listOf(
                "user_id" bodyKeyOf employNumber.toString(),
            )
        ).mapCatching {
            it.toBonuses(employeeNumber = employNumber)
        }.also {
            Log.d(REMOTE_TAG, "bonus(num: $employNumber) => $it")
        }
    }
}