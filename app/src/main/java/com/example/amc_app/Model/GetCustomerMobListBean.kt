package com.example.amc_app.Model


import com.google.gson.annotations.SerializedName

data class GetCustomerMobListBean(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("error")
    val error: Boolean, // false
    @SerializedName("msg")
    val msg: String
) {
    data class Data(
        @SerializedName("id")
        val id: Int, // 1
        @SerializedName("name")
        val name: String, // vaibhav
        @SerializedName("phone")
        val phone: String // 9675539233
    )
}