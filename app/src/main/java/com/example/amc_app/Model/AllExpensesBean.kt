package com.example.amc_app.Model


import com.google.gson.annotations.SerializedName

data class AllExpensesBean(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("error")
    val error: Boolean, // false
    @SerializedName("msg")
    val msg: String // Data loaded successfully.
) {
    data class Data(
        @SerializedName("amount")
        val amount: Int, // 20
        @SerializedName("category")
        val category: String, // DA
        @SerializedName("comments")
        val comments: String, // 8 k.m office to Sec 21panchkula
        @SerializedName("employee_id")
        val employeeId: Int, // 19
        @SerializedName("exp_date")
        val expDate: String, // 2022-08-25
        @SerializedName("id")
        val id: Int, // 98
        @SerializedName("title")
        val title: String // Baick 
    )
}