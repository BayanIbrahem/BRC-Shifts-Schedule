package com.dev_bayan_ibrahim.brc_shifting.domain.model.remote


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteBonus(
    @SerialName("status")
    val status: Int,
    @SerialName("msg")
    val msg: String,
    @SerialName("data")
    val data: List<Data>,
) {
    @Serializable
    data class Data(
        @SerialName("AMOUNT_MOK")
        val total: String,
        @SerialName("START_DATE")
        val date: String,
        @SerialName("SAFI_MOK")
        val netValue: String,
        @SerialName("KIND_MOK")
        val type: String,
    )
}