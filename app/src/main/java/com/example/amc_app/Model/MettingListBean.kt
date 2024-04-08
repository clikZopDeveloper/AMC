package com.example.amc_app.Model


import com.google.gson.annotations.SerializedName

data class MettingListBean(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("error")
    val error: Boolean, // false
    @SerializedName("msg")
    val msg: String // Data loaded successfully.
) {
    data class Data(
        @SerializedName("client_address")
        val clientAddress: String, // Zirakpur
        @SerializedName("client_contact")
        val clientContact: String, // 9638527410
        @SerializedName("client_name")
        val clientName: String, // ABC
        @SerializedName("contact_person")
        val contactPerson: String, // Abhishek
        @SerializedName("id")
        val id: Int, // 1
        @SerializedName("remarks")
        val remarks: String, // Successful
        @SerializedName("start_location")
        val startLocation: String, // 30.6382566,76.8250528
        @SerializedName("start_time")
        val startTime: String, // 2023-04-1516:28:50
        @SerializedName("stop_location")
        val stopLocation: String, // 30.6382682,76.8250733
        @SerializedName("stop_time")
        val stopTime: String, // 2023-04-2113:01:52
        @SerializedName("user_id")
        val userId: Int // 2
    )
}