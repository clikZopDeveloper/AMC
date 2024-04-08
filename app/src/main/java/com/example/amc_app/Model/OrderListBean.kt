package com.example.amc_app.Model


import com.google.gson.annotations.SerializedName

data class OrderListBean(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("error")
    val error: Boolean, // false
    @SerializedName("msg")
    val msg: String // Data loaded successfully.
) {
    data class Data(
        @SerializedName("customer_name")
        val customerName: String, // XYZ
        @SerializedName("id")
        val id: Int, // 30
        @SerializedName("order_date")
        val orderDate: String, // 2024-02-2618:55:13
        @SerializedName("order_value")
        val orderValue: Int // 0
    )
}