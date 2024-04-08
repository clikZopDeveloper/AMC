package com.example.amc_app.Model


import com.google.gson.annotations.SerializedName

data class CustomerWiseReportBean(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("error")
    val error: Boolean, // false
    @SerializedName("msg")
    val msg: String // Dataloaded sucessfully.
) {
    data class Data(
        @SerializedName("approved_amt")
        val approvedAmt: String, // 0
        @SerializedName("approved_count")
        val approvedCount: String, // 0
        @SerializedName("delivered_amt")
        val deliveredAmt: String, // 0
        @SerializedName("delivered_count")
        val deliveredCount: String, // 0
        @SerializedName("dispatched_amt")
        val dispatchedAmt: String, // 0
        @SerializedName("dispatched_count")
        val dispatchedCount: String, // 0
        @SerializedName("pending_amt")
        val pendingAmt: String, // 0
        @SerializedName("pending_count")
        val pendingCount: String, // 0
        @SerializedName("rejected_amt")
        val rejectedAmt: String, // 0
        @SerializedName("rejected_count")
        val rejectedCount: String // 0
    )
}