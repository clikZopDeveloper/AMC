package com.example.amc_app.Model


import com.google.gson.annotations.SerializedName

data class ProductBean(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("error")
    val error: Boolean, // false
    @SerializedName("msg")
    val msg: String // Dataloaded successfully.
) {
    data class Data(
        @SerializedName("category_name")
        val categoryName: String, // Bathing
        @SerializedName("color")
        val color: String, // White
        @SerializedName("current_stock")
        val currentStock: Int, // 74
        @SerializedName("id")
        val id: Int, // 1
        @SerializedName("name")
        val name: String, // SENSOR FAUCET FOR WASH BASIN box(White)
        @SerializedName("price")
        val price: String, // 500.00
        @SerializedName("qty")
        val qty: Int, // 50
        @SerializedName("type")
        val type: String // box
    )
}