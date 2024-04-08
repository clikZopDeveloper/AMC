package com.example.amc_app.Model


import com.google.gson.annotations.SerializedName

data class CreateMeetingBean(
    @SerializedName("data")
    val `data`: Int, // 16
    @SerializedName("error")
    val error: Boolean, // false
    @SerializedName("msg")
    val msg: String // Meeting started successfully.
)