package com.example.amc_app.Model


import com.google.gson.annotations.SerializedName

data class AllContactListBean(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("error")
    val error: Boolean, // false
    @SerializedName("msg")
    val msg: String // Data loadedsuccessfully.
) {
    data class Data(
        @SerializedName("area_id")
        val areaId: Int, // 1
        @SerializedName("created_at")
        val createdAt: String, // 2022-08-12 13:15:10
        @SerializedName("designation")
        val designation: String, // Executive Assistant
        @SerializedName("email")
        val email: String, // sales@kktechecoproducts.com
        @SerializedName("id")
        val id: Int, // 1
        @SerializedName("name")
        val name: String, // ShivaniSingh
        @SerializedName("phone")
        val phone: String, // 9216755418
        @SerializedName("updated_at")
        val updatedAt: String // 2024-02-09 13:08:03
    )
}