package com.example.amc_app.Model


import com.google.gson.annotations.SerializedName

data class DashboardBean(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("error")
    val error: Boolean, // false
    @SerializedName("msg")
    val msg: String // Dataloaded successfully.
) {
    data class Data(
        @SerializedName("approved_order")
        val approvedOrder: String, // 1
        @SerializedName("area_report")
        val areaReport: String, // null
        @SerializedName("delivered_order")
        val deliveredOrder: String, // 3
        @SerializedName("dispatched_order")
        val dispatchedOrder: String, // 0
        @SerializedName("last_month_expense")
        val lastMonthExpense: String, // 0
        @SerializedName("pending_order")
        val pendingOrder: String, // 2
        @SerializedName("rejected_order")
        val rejectedOrder: String, // 1
        @SerializedName("this_month_expense")
        val thisMonthExpense: String // 250
    )
}