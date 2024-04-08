package com.example.amc_app.Model


import com.google.gson.annotations.SerializedName

data class AllComplaintsBean(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("error")
    val error: Boolean, // false
    @SerializedName("msg")
    val msg: String // Data loaded successfully.
) {
    data class Data(
        @SerializedName("address")
        val address: String, // #2141, Phase-10, Mohali punjab160055
        @SerializedName("co_id")
        val coId: Int, // 1
        @SerializedName("comment")
        val comment: String, // null
        @SerializedName("complain_description")
        val complainDescription: String, // BRADFOR WHITEHEAT PUMP , GEYSER, SUBMERSIBLE PUMP WITH PANEL, RETURN PUMP WITH PANEL, VOROSCH STRAINER AND KAYLXX CONDITIONERINSTALLTION ANDCOMMISSIONING
        @SerializedName("created_date")
        val createdDate: String, // 2022-08-19 10:30:58
        @SerializedName("cust_id")
        val custId: Int, // 7
        @SerializedName("customer_name")
        val customerName: String, // ROCKPECKERPRIVATE LIMITED
        @SerializedName("description")
        val description: Any, // null
        @SerializedName("id")
        val id: Int, // 14
        @SerializedName("installation_date")
        val installationDate: String, // 2022-08-1900:00:00
        @SerializedName("inv_id")
        val invId: Int, // 25
        @SerializedName("last_comment")
        val lastComment: String, // maintenance requiredon time
        @SerializedName("last_updated")
        val lastUpdated: String, // 2024-02-09 13:45:43
        @SerializedName("last_work_status")
        val lastWorkStatus: String, // completed
        @SerializedName("mobile")
        val mobile: String, // 9464638054
        @SerializedName("otp")
        val otp: Any, // null
        @SerializedName("paid_amount")
        val paidAmount: String, // 0.00
        @SerializedName("part_change")
        val partChange: Any, // null
        @SerializedName("part_select")
        val partSelect: Any, // null
        @SerializedName("payable_amt")
        val payableAmt: String, // 0.00
        @SerializedName("payment_type")
        val paymentType: String, // free
        @SerializedName("products")
        val products: String, // VORDOSCH KRISTALL KLAR STRAINER VALUE 1,OVERHEAD TANK AUTOMATION PANEL, RECIRCULATION LINE AUTOMATION PANEL, AQUA 1 HP, CDXM/A90/10 CENTRIFUGAL WATER PUMP,BWHP300C, 300 LITER CAPACITY, INTEGRATED, M-II-80R6DS 4.5KW/240/1PH-80 GALLON, IPSKXRG1 BLUE1
        @SerializedName("recommendation")
        val recommendation: String, // please provide propershed
        @SerializedName("secondary_mobile")
        val secondaryMobile: String, // 9464638054
        @SerializedName("service_type")
        val serviceType: String, // installation
        @SerializedName("sm_id")
        val smId: Int, // 19
        @SerializedName("status")
        val status: String, // pending
        @SerializedName("suggestion")
        val suggestion: String
    )
}