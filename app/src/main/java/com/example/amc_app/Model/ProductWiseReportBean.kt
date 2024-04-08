package com.example.amc_app.Model


import com.google.gson.annotations.SerializedName

data class ProductWiseReportBean(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("error")
    val error: Boolean, // false
    @SerializedName("msg")
    val msg: String // Data loaded successfully.
) {
    data class Data(
        @SerializedName("amount")
        val amount: String, // 20000.00
        @SerializedName("category_name")
        val categoryName: String, // Bathing
        @SerializedName("color")
        val color: String, // White
        @SerializedName("product_name")
        val productName: String, // SENSOR FAUCET FOR WASH BASIN
        @SerializedName("qty")
        val qty: String, // 40
        @SerializedName("type")
        val type: String // box
    )
}