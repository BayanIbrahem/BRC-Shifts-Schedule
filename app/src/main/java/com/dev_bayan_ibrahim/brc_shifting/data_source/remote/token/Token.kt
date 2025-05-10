package com.dev_bayan_ibrahim.brc_shifting.data_source.remote.token

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.json.Json
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@Serializable(QTokenSerializer::class)
data class Token(
    val access: String,
    val refresh: String,
) {
    @OptIn(ExperimentalEncodingApi::class)
    fun <T> decode(deserializer: DeserializationStrategy<T>): T? {
        val data = access.split('.').getOrNull(1) ?: return null
        val decodeData = Base64.decode(data).decodeToString()
        return Json.decodeFromString(deserializer, decodeData)
    }

    fun decode() = decode(QTokenDataSerializer)
}

object QTokenSerializer : KSerializer<Token> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor(
        "QTokenSerializer"
    ) {
        element<String>("access")
        element<String>("refresh")
    }

    override fun deserialize(decoder: Decoder): Token {
        var access = ""
        var refresh = ""
        decoder.decodeStructure(descriptor) {
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> access = decodeStringElement(descriptor, index)
                    1 -> refresh = decodeStringElement(descriptor, index)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("unexpected index $index")
                }
            }
        }
        return Token(access, refresh)
    }

    override fun serialize(encoder: Encoder, value: Token) {}
}

object QTokenDataSerializer : KSerializer<Long> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor(
        "QTokenDataSerializer"
    ) {
        element<Long>("user_id")
    }

    override fun deserialize(decoder: Decoder): Long {
        var id: Long? = null
        decoder.decodeStructure(descriptor) {
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> id = decodeLongElement(descriptor, index)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("unexpected index $index")
                }
            }
        }
        return id ?: error("invalid data")
    }

    override fun serialize(encoder: Encoder, value: Long) {}
}