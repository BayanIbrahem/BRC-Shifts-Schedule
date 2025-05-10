package com.dev_bayan_ibrahim.brc_shifting.data_source.remote.uri

import io.ktor.http.URLBuilder


interface UriDirector {
    /**
     * director for uri
     */
    fun URLBuilder.director(): URLBuilder
}
