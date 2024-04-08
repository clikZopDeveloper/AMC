package com.example.amc_app.Model


import com.google.gson.annotations.SerializedName

data class ProfileBean(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("error")
    val error: Boolean, // false
    @SerializedName("msg")
    val msg: String // Data loaded successfully.
) {
    data class Data(
        @SerializedName("area_id")
        val areaId: Int, // 1
        @SerializedName("area_name")
        val areaName: String, // NORTHINDIA
        @SerializedName("created_at")
        val createdAt: String, // 2023-12-0517:00:36
        @SerializedName("email")
        val email: String, // service@kktechecoproducts.com
        @SerializedName("fcm_token")
        val fcmToken: Any, // null
        @SerializedName("id")
        val id: Int, // 19
        @SerializedName("is_active")
        val isActive: Int, // 1
        @SerializedName("last_ip")
        val lastIp: Any, // null
        @SerializedName("last_location")
        val lastLocation: String, // 41./654465
        @SerializedName("last_login")
        val lastLogin: String, // 2024-02-0911:24:25
        @SerializedName("location_time")
        val locationTime: String, // 2024-02-08 13:34:00
        @SerializedName("name")
        val name: String, // Avinay Sharma
        @SerializedName("parent_id")
        val parentId: Int, // 2
        @SerializedName("password")
        val password: String, // 123458
        @SerializedName("phone")
        val phone: String, // 7743008977
        @SerializedName("token")
        val token: String, // 1VkYnipUsPNL
        @SerializedName("updated_at")
        val updatedAt: String, // 2024-02-09 11:24:25
        @SerializedName("user_type")
        val userType: String, // service man
        @SerializedName("username")
        val username: String // avinay
    )
}