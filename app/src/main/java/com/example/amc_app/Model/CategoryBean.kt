package com.example.amc_app.Model


import com.google.gson.annotations.SerializedName

data class CategoryBean(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("error")
    val error: Boolean, // false
    @SerializedName("msg")
    val msg: String // Data loaded successfully.
) {
    data class Data(
        @SerializedName("id")
        val id: Int, // 1
        @SerializedName("is_active")
        val isActive: Int, // 1
        @SerializedName("name")
        val name: String // Bathing
    )
}