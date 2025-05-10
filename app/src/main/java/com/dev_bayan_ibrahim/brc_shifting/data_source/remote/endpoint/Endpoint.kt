package com.dev_bayan_ibrahim.brc_shifting.data_source.remote.endpoint

import android.util.Log
import com.dev_bayan_ibrahim.brc_shifting.data_source.remote.REMOTE_TAG
import com.dev_bayan_ibrahim.brc_shifting.data_source.remote.param.BodyParam
import com.dev_bayan_ibrahim.brc_shifting.data_source.remote.param.QueryParam
import com.dev_bayan_ibrahim.brc_shifting.data_source.remote.token.Token
import com.dev_bayan_ibrahim.brc_shifting.data_source.remote.uri.UriDirector
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.URLBuilder
import io.ktor.http.Url
import io.ktor.http.appendPathSegments
import io.ktor.http.contentType
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject

const val REFRESHABLE_REQUEST_TAG = "refreshable_request"

abstract class Endpoint<Get, Post, Delete>(
    protected open val client: HttpClient,
    protected open val uriDirector: UriDirector,
    private val path: String,
    private val needAuthentication: Boolean = false,
    private val refreshOn: List<Int> = listOf(401),
    private val useMultipart: Boolean = true,
    private val getToken: suspend () -> Token? = { null },
    private val onRequestRefreshToken: suspend (Token?) -> Result<Boolean> = { Result.failure(NotImplementedError()) },
) {
    private suspend fun Result<HttpResponse>.log(): Result<HttpResponse> = also {
        fold(
            onFailure = { exception ->
                Log.d(
                    REMOTE_TAG,
                    "Request failed with exception: ${exception.message.orEmpty()}",
                    exception
                )
            },
            onSuccess = { response ->
                Log.d(
                    REMOTE_TAG,
                    "Request to ${response.call.request.url} succeeded with status: ${response.status}, body: ${response.body<String>()}"
                )
            }
        )
    }

    private suspend fun refreshableRequest(
        buildRequest: suspend () -> HttpResponse,
    ): HttpResponse {
        if (!needAuthentication) {
            return buildRequest()
        }
        Log.d(REFRESHABLE_REQUEST_TAG, "start..")
        return try {
            buildRequest().also {
                Log.d(REFRESHABLE_REQUEST_TAG, "success without refresh")
            }
        } catch (response: ResponseException) {
            val status = response.response.status.value
            Log.d(REFRESHABLE_REQUEST_TAG, "response exception status: $status")
            if (status in refreshOn) {
                Log.d(REFRESHABLE_REQUEST_TAG, "response exception status need refresh")
                val refreshResult = onRequestRefreshToken(getToken())
                Log.d(REFRESHABLE_REQUEST_TAG, "response exception status refreshed? $refreshResult")
                val isRefreshed = refreshResult.getOrThrow()
                if (isRefreshed) {
                    buildRequest()
                } else {
                    throw response
                }
            } else {
                throw response
            }
        } catch (e: Exception) {
            Log.d(REFRESHABLE_REQUEST_TAG, "general exception $e")
            throw e
        }
    }

    private suspend fun HttpRequestBuilder.setTokens() {
        val accessToken = getToken()?.access ?: return
        header(
            "Authorization",
            "Bearer $accessToken"
        )
    }

    private suspend fun HttpRequestBuilder.requestBody(params: Collection<BodyParam>) {
        setTokens()
        if (params.isNotEmpty()) {
            if (useMultipart) {
                setBody(buildMultiPartData(params))
            } else {
                contentType(ContentType.Application.Json)
                setBody(buildJsonData(params))
            }
        }
    }

    private fun buildMultiPartData(
        params: Collection<BodyParam>,
    ): MultiPartFormDataContent = MultiPartFormDataContent(
        formData {
            params.forEach { param ->
                when (param) {
//                    is QubitParam.Body.File -> {
//                        append(
//                            key = param.key,
//                            value = param.array!!,
//                            headers = Headers.build {
//                                append(
//                                    name = HttpHeaders.ContentType,
//                                    value = param.type
//                                )
//                                append(
//                                    name = HttpHeaders.ContentDisposition,
//                                    value = "filename=\"${param.name}.${param.type}\""
//                                )
//                            }
//                        )
//                    }

                    is BodyParam.Primitive -> {
                        append(
                            key = param.key,
                            value = param.value
                        )
                    }
                }
            }
        }
    )

    private fun buildJsonData(
        params: Collection<BodyParam>,
    ) = buildJsonObject {
        params.forEach { param ->
            when (param) {
                is BodyParam.Primitive -> {
                    this.put(param.key, JsonPrimitive(param.value))
                }
            }
        }
    }

    private fun prepareUri(
        params: Collection<QueryParam> = emptyList(),
        body: URLBuilder.() -> URLBuilder = { this },
    ): Url {
        return URLBuilder().apply {
            with(uriDirector) {
                director()
            }
            appendPathSegments(path)
            params.forEach { (key, value) ->
                this.parameters.append(key, value)
            }
            body()
        }.build()
    }

    private suspend fun onGetRequest(
        queryParams: Collection<QueryParam>,
        bodyParams: Collection<BodyParam>,
    ): Result<HttpResponse> {
        val uri = prepareUri(queryParams)
        return Result.runCatching {
            refreshableRequest {
                client.get(uri) {
                    requestBody(bodyParams)
                }
            }
        }
    }

    private suspend fun onPostRequest(
        queryParams: Collection<QueryParam>,
        bodyParams: Collection<BodyParam>,
    ): Result<HttpResponse> {
        val uri = prepareUri(queryParams)
        return Result.runCatching {
            refreshableRequest {
                client.post(uri) {
                    requestBody(bodyParams)
                }
            }
        }
    }

    private suspend fun onDeleteRequest(
        queryParams: Collection<QueryParam>,
        bodyParams: Collection<BodyParam>,
    ): Result<HttpResponse> {
        val uri = prepareUri(queryParams)
        return Result.runCatching {
            refreshableRequest {
                client.delete(uri) {
                    requestBody(bodyParams)
                }
            }
        }
    }

    /** exceptions are caught */
    open suspend fun onParseGetResult(httpResponse: HttpResponse): Get {
        throw NotImplementedError()
    }

    /** exceptions are caught */
    open suspend fun onParsePostResult(httpResponse: HttpResponse): Post {
        throw NotImplementedError()
    }

    /** exceptions are caught */
    open suspend fun onParseDeleteResult(httpResponse: HttpResponse): Delete {
        throw NotImplementedError()
    }

    suspend fun getRequest(
        queryParams: Collection<QueryParam> = emptyList(),
        bodyParams: Collection<BodyParam> = emptyList(),
        /** exceptions are caught */
        parseResult: (suspend (HttpResponse) -> Get)? = null,
    ): Result<Get> {
        return onGetRequest(queryParams, bodyParams)
            .log()
            .mapCatching {
                parseResult?.invoke(it) ?: onParseGetResult(it)
            }
    }

    suspend fun postRequest(
        queryParams: Collection<QueryParam> = emptyList(),
        bodyParams: Collection<BodyParam> = emptyList(),
        /** exceptions are caught */
        parseResult: (suspend (HttpResponse) -> Post)? = null,
    ): Result<Post> {
        return onPostRequest(queryParams, bodyParams)
            .log()
            .mapCatching {
                parseResult?.invoke(it) ?: onParsePostResult(it)
            }
    }

    suspend fun deleteRequest(
        queryParams: Collection<QueryParam> = emptyList(),
        bodyParams: Collection<BodyParam> = emptyList(),
        /** exceptions are caught */
        parseResult: (suspend (HttpResponse) -> Delete)? = null,
    ): Result<Delete> {
        return onDeleteRequest(queryParams, bodyParams)
            .log()
            .mapCatching {
                parseResult?.invoke(it) ?: onParseDeleteResult(it)
            }
    }
}

