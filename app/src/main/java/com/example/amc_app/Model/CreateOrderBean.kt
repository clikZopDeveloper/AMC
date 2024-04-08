package com.example.amc_app.Model


import com.google.gson.annotations.SerializedName

data class CreateOrderBean(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("error")
    val error: Boolean, // false
    @SerializedName("msg")
    val msg: String // Order created sucessfully.
) {
    data class Data(
        @SerializedName("order_id")
        val orderId: Int, // 29
        @SerializedName("order_total")
        val orderTotal: Int, // 13500
        @SerializedName("product_count")
        val productCount: Int // 2
    )
}