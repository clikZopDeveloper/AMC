package com.example.amc_app.Model


import com.google.gson.annotations.SerializedName

data class GetAllStatusBean(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("error")
    val error: Boolean, // false
    @SerializedName("msg")
    val msg: String
) {
    data class Data(
        @SerializedName("careated_at")
        val careatedAt: String, // 2024-02-06 10:59:48
        @SerializedName("id")
        val id: Int, // 1
        @SerializedName("seq")
        val seq: Int, // 1
        @SerializedName("status")
        val status: String // New Lead
    )
}