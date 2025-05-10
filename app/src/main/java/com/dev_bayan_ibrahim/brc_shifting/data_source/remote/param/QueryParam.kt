package com.dev_bayan_ibrahim.brc_shifting.data_source.remote.param

data class QueryParam(val key: String, val value: String)

infix fun String.queryKeyOf(value: String) = QueryParam(this, value)