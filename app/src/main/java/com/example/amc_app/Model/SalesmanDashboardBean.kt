package com.example.amc_app.Model


import com.google.gson.annotations.SerializedName

data class SalesmanDashboardBean(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("error")
    val error: Boolean, // false
    @SerializedName("msg")
    val msg: String
) {
    data class Data(
        @SerializedName("customer_data")
        val customerData: List<CustomerData>,
        @SerializedName("weekly_data")
        val weeklyData: List<WeeklyData>
    ) {
        data class CustomerData(
            @SerializedName("total_customer")
            val totalCustomer: Int, // 0
            @SerializedName("total_visitor")
            val totalVisitor: Int // 3
        )

        data class WeeklyData(
            @SerializedName("date")
            val date: String, // 2024-02-21
            @SerializedName("total_customer")
            val totalCustomer: Int, // 0
            @SerializedName("total_visitor")
            val totalVisitor: Int // 3
        )
    }
}