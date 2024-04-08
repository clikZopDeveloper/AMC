package com.example.amc_app.Model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CustmerTelecallerBean(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("error")
    val error: Boolean, // false
    @SerializedName("msg")
    val msg: String
): Serializable {
    data class Data(
        @SerializedName("address")
        val address: String, // asdasd
        @SerializedName("allocated_date")
        val allocatedDate: String, // 2024-02-19
        @SerializedName("city")
        val city: String, // Chirang
        @SerializedName("converted")
        val converted: Int, // 0
        @SerializedName("converted_date")
        val convertedDate: String, // 0000-00-00
        @SerializedName("created_at")
        val createdAt: String, // 2024-02-17 13:20:45
        @SerializedName("customer_type")
        val customerType: String, // visitor
        @SerializedName("doa")
        val doa: String, // 2024-02-24
        @SerializedName("dob")
        val dob: String, // 2024-02-23
        @SerializedName("email")
        val email: String, // asdsad
        @SerializedName("id")
        val id: Int, // 10
        @SerializedName("is_allocated")
        val isAllocated: Int, // 1
        @SerializedName("name")
        val name: String, // dgdfg
        @SerializedName("phone")
        val phone: String, // 3453159158
        @SerializedName("remarks")
        val remarks: String, // kjl
        @SerializedName("remind_date")
        val remindDate: String, // 0000-00-00
        @SerializedName("remind_time")
        val remindTime: String, // 00:00:00
        @SerializedName("source")
        val source: String, // facebook
        @SerializedName("staff_id")
        val staffId: Int, // 3
        @SerializedName("state")
        val state: String, // Assam
        @SerializedName("status")
        val status: Int, // 1
        @SerializedName("updated_at")
        val updatedAt: String, // 2024-02-19 16:24:37
        @SerializedName("user_id")
        val userId: Int, // 2
        @SerializedName("whatsapp")
        val whatsapp: String // 4545******
    ): Serializable
}