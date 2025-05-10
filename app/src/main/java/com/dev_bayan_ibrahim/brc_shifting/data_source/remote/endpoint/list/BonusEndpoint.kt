package com.dev_bayan_ibrahim.brc_shifting.data_source.remote.endpoint.list

import com.dev_bayan_ibrahim.brc_shifting.data_source.remote.endpoint.Endpoint
import com.dev_bayan_ibrahim.brc_shifting.data_source.remote.uri.UriDirector
import com.dev_bayan_ibrahim.brc_shifting.domain.model.remote.RemoteBonus
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import kotlinx.serialization.json.Json

data class BonusEndpoint(
    override val client: HttpClient,
    override val uriDirector: UriDirector = com.dev_bayan_ibrahim.brc_shifting.data_source.remote.uri.BRCUriDirector,
    val json: Json = Json,
) : Endpoint<Unit, RemoteBonus, Unit>(
    path = "user_mokafa.php",
    client = client,
    uriDirector = uriDirector,
) {
    override suspend fun onParsePostResult(httpResponse: HttpResponse): RemoteBonus {
        return json.decodeFromString(
            RemoteBonus.serializer(),
            httpResponse.body<String>()
        )
    }
}