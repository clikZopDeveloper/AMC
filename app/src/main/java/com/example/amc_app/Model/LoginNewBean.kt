package com.example.amc_app.Model


import com.google.gson.annotations.SerializedName

data class LoginNewBean(
    @SerializedName("d")
    val d: D
) {
    data class D(
        @SerializedName("Data")
        val `data`: String,
        @SerializedName("Message")
        val message: String,
        @SerializedName("Status")
        val status: Int,
        @SerializedName("__type")
        val type: String
    )
}