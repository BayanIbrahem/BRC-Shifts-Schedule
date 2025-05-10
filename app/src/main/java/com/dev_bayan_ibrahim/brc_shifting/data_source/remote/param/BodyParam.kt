package com.dev_bayan_ibrahim.brc_shifting.data_source.remote.param

sealed interface BodyParam {
    val key: String

    data class Primitive(
        override val key: String,
        val value: String,
    ) : BodyParam
}

infix fun String.bodyKeyOf(value: String) = BodyParam.Primitive(this, value)