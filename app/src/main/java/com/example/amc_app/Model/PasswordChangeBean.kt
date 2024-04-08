package com.example.amc_app.Model


import com.google.gson.annotations.SerializedName

data class PasswordChangeBean(
    @SerializedName("data")
    val `data`: List<Any>,
    @SerializedName("error")
    val error: Boolean, // false
    @SerializedName("msg")
    val msg: String // Password changed successfully.
)