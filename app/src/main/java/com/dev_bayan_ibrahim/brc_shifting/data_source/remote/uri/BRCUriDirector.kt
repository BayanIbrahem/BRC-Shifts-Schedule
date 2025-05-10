package com.dev_bayan_ibrahim.brc_shifting.data_source.remote.uri

import io.ktor.http.URLBuilder
import io.ktor.http.set

const val scheme = "https"
const val brcHost = "brc.sy"
const val ip = "192.168.43.44"
const val port = "8080"
const val baseUrlPath = "test"

object BRCUriDirector : UriDirector {
    override fun URLBuilder.director(): URLBuilder = apply {
        set(
            scheme = scheme,
            host = brcHost,
            path = baseUrlPath
        )
    }
}
