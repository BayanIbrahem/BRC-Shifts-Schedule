package com.dev_bayan_ibrahim.brc_shifting.domain.model.remote


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteDayOffs(
    @SerialName("status")
    val status: Int,
    @SerialName("msg")
    val msg: String,
    @SerialName("data")
    val data: List<Data>
) {
    /**
     * @param date formatted like YYYY-MM-DD
     * @param workDate formatted like YYYY-MM-DD used in some types of vacations which require a work in a previous date
     * @param period period of the day-off sometimes it is zero
     * @param type an integer some times it is 0,1, 3, or 4 see
     */
    @Serializable
    data class Data(
        @SerialName("DATE_VAC")
        val date: String,
        @SerialName("PERIOD_VAC")
        val period: String,
        @SerialName("KIND_VAC")
        val type: String,
        @SerialName("DATE_WORK")
        val workDate: String
    )
}