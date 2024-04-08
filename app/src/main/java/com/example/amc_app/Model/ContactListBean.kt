package com.example.amc_app.Model


import com.google.gson.annotations.SerializedName

data class ContactListBean(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("error")
    val error: Boolean, // false
    @SerializedName("msg")
    val msg: String // Data loaded successfully.
) {
    data class Data(
        @SerializedName("created_at")
        val createdAt: String, // 2023-04-2412:50:46
        @SerializedName("designation")
        val designation: String, // Manager
        @SerializedName("email")
        val email: String, // adesh@clikzopinnovations.com
        @SerializedName("id")
        val id: Int, // 1
        @SerializedName("name")
        val name: String, // AdeshManocha
        @SerializedName("phone")
        val phone: String // 8146653301
    )
}