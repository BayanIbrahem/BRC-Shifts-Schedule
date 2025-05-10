package com.dev_bayan_ibrahim.brc_shifting.domain.model.remote


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteDeductions(
    @SerialName("status")
    val status: Int,
    @SerialName("msg")
    val msg: String,
    @SerialName("data")
    val data: List<Data>
) {
    @Serializable
    data class Data(
        @SerialName("TOTAL_AMOUNT")
        val total: String,
        @SerialName("REMAINING_AMOUNT")
        val remaining: String,
        @SerialName("MONTHLY_AMMOUNT")
        val monthlyInstallment: String,
        @SerialName("DEDUCTION_NAME")
        val name: String,
        @SerialName("EMPLOYEE_DEDUCTION_NUMBER")
        val employeeNumber: String,
    )
}