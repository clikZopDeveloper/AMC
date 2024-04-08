package com.example.amc_app.Model


import com.google.gson.annotations.SerializedName

data class CustomerListBean(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("error")
    val error: Boolean, // false
    @SerializedName("msg")
    val msg: String // Data loaded sucessfully.
) {
    data class Data(
        @SerializedName("anniversary")
        val anniversary: String,
        @SerializedName("area")
        val area: String, // Zirakpur
        @SerializedName("billing_address")
        val billingAddress: String, // Global BusinessPark
        @SerializedName("created_date")
        val createdDate: String, // 2023-04-1516:19:17
        @SerializedName("dob")
        val dob: String, // 1991-01-15
        @SerializedName("email")
        val email: String, // test2@gmail.com
        @SerializedName("gst_no")
        val gstNo: String, // YYYYYYYYYYYY
        @SerializedName("id")
        val id: Int, // 3
        @SerializedName("is_active")
        val isActive: Int, // 1
        @SerializedName("mobile")
        val mobile: String, // 9090909090
        @SerializedName("name")
        val name: String, // XYZ
        @SerializedName("shipping_address")
        val shippingAddress: String // Global BusinessPark
    )
}