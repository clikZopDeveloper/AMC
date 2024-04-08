package com.example.amc_app.Model


import com.google.gson.annotations.SerializedName

data class StartDayBean(
    @SerializedName("data")
    val `data`: List<Any>,
    @SerializedName("error")
    val error: Boolean, // false
    @SerializedName("msg")
    val msg: String // Day started successfully. Good Luck.
)