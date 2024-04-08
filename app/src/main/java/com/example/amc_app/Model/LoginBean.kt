package com.example.amc_app.Model


import com.google.gson.annotations.SerializedName

data class LoginBean(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("error")
    val error: Boolean, // false
    @SerializedName("msg")
    val msg: String // User logged in successfully.
) {
    data class Data(
        @SerializedName("anniversary")
        val anniversary: String,
        @SerializedName("created_date")
        val createdDate: String, // 2023-04-1516:18:07
        @SerializedName("current_office_status")
        val currentOfficeStatus: String,
        @SerializedName("customers")
        val customers: String, // 4,3
        @SerializedName("dob")
        val dob: String, // 1993-07-15
        @SerializedName("email")
        val email: String, // priyanka@gmail.com
        @SerializedName("id")
        val id: Int, // 2
        @SerializedName("is_active")
        val isActive: Int, // 1
        @SerializedName("last_location")
        val lastLocation: String, // 45.45654,52.465456
        @SerializedName("last_login")
        val lastLogin: String, // 2024-02-22 18:20:40
        @SerializedName("location_time")
        val locationTime: String, // 2024-02-2218:07:20
        @SerializedName("mobile")
        val mobile: String, // 6666666666
        @SerializedName("name")
        val name: String, // Test
        @SerializedName("password")
        val password: String, // 123456
        @SerializedName("token")
        val token: String, // 5XaKu6MuL37f
        @SerializedName("user_type")
        val userType: String // salesman
    )
}