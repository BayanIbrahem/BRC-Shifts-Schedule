package com.dev_bayan_ibrahim.brc_shifting.data_source.remote.endpoint.list

import com.dev_bayan_ibrahim.brc_shifting.data_source.remote.endpoint.EndPointMonthVariance
import com.dev_bayan_ibrahim.brc_shifting.data_source.remote.endpoint.Endpoint
import com.dev_bayan_ibrahim.brc_shifting.data_source.remote.uri.UriDirector
import com.dev_bayan_ibrahim.brc_shifting.domain.model.remote.RemoteSalary
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import kotlinx.serialization.json.Json

data class SalaryEndpoint(
    override val client: HttpClient,
    override val uriDirector: UriDirector,
    val json: Json = Json,
    val variance: EndPointMonthVariance,
) : Endpoint<Unit, RemoteSalary, Unit>(
    client = client,
    uriDirector = uriDirector,
    path = when (variance) {
        EndPointMonthVariance.ThisMonth -> "user_salary.php"
        EndPointMonthVariance.PreviousMonth -> "user_salary_last.php"
    }
) {
    override suspend fun onParsePostResult(httpResponse: HttpResponse): RemoteSalary {
        return json.decodeFromString(
            RemoteSalary.serializer(),
            httpResponse.body<String>()
        )
    }
}
