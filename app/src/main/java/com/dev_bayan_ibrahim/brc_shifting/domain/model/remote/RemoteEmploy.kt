package com.dev_bayan_ibrahim.brc_shifting.domain.model.remote


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteEmploy(
    @SerialName("status")
    val status: Int,
    @SerialName("msg")
    val message: String,
    @SerialName("data")
    val data: Data?
) {
    @Serializable
    data class Data(
        @SerialName("User_id")
        val id: String,
        @SerialName("user_Name")
        val userName: String,
        @SerialName("password")
        val password: String,
        @SerialName("Full_name")
        val fullName: String,
        @SerialName("User_Num")
        val userNumber: String,
        @SerialName("phone")
        val phone: String,
        @SerialName("Verion")
        val apiVersion: String
    )
}