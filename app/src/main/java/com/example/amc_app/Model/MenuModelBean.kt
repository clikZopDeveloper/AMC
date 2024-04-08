package com.example.amc_app.Model


import com.google.gson.annotations.SerializedName

data class MenuModelBean(

    @SerializedName("indexId")
    val indexId: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("subTitle")
    val subTitle: String,

    val drawableId: Int
) {

}