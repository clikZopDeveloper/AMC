package com.example.amc_app.Model


import com.google.gson.annotations.SerializedName

data class CustomerDetailBean(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("error")
    val error: Boolean, // false
    @SerializedName("msg")
    val msg: String
) {
    data class Data(
        @SerializedName("customer_interested_category")
        val customerInterestedCategory: List<CustomerInterestedCategory>,
        @SerializedName("customer_purchased_category")
        val customerPurchasedCategory: List<CustomerPurchasedCategory>,
        @SerializedName("customers")
        val customers: Customers,
        @SerializedName("lead_comments")
        val leadComments: List<LeadComment>,
        @SerializedName("lead_history")
        val leadHistory: List<LeadHistory>
    ) {
        data class CustomerInterestedCategory(
            @SerializedName("category_id")
            val categoryId: Int, // 1
            @SerializedName("category_name")
            val categoryName: String, // AC
            @SerializedName("interested_status")
            val interestedStatus: Int // 1
        )

        data class CustomerPurchasedCategory(
            @SerializedName("category_id")
            val categoryId: Int, // 1
            @SerializedName("category_name")
            val categoryName: String, // AC
            @SerializedName("purchased_status")
            val purchasedStatus: Int // 0
        )

        data class Customers(
            @SerializedName("address")
            val address: String, // sfnef
            @SerializedName("allocated_date")
            val allocatedDate: Any, // null
            @SerializedName("city")
            val city: String, // Barpeta
            @SerializedName("converted")
            val converted: Int, // 0
            @SerializedName("converted_date")
            val convertedDate: String, // 0000-00-00
            @SerializedName("created_at")
            val createdAt: String, // 2024-02-21 11:15:47
            @SerializedName("customer_type")
            val customerType: String, // visitor
            @SerializedName("doa")
            val doa: String, // 2024-02-25
            @SerializedName("dob")
            val dob: String, // 2024-02-21
            @SerializedName("email")
            val email: String, // yash@gmail.com
            @SerializedName("id")
            val id: Int, // 3
            @SerializedName("is_allocated")
            val isAllocated: Int, // 0
            @SerializedName("name")
            val name: String, // yash
            @SerializedName("phone")
            val phone: String, // 8982652582
            @SerializedName("remarks")
            val remarks: String,
            @SerializedName("remind_date")
            val remindDate: Any, // null
            @SerializedName("remind_time")
            val remindTime: Any, // null
            @SerializedName("source")
            val source: String, // WhatsApp
            @SerializedName("staff_id")
            val staffId: Int, // 3
            @SerializedName("state")
            val state: String, // Assam
            @SerializedName("status")
            val status: Int, // 0
            @SerializedName("updated_at")
            val updatedAt: String, // 2024-02-21 11:44:32
            @SerializedName("user_id")
            val userId: Int, // 3
            @SerializedName("whatsapp")
            val whatsapp: String // 5894591595
        )

        data class LeadComment(
            @SerializedName("created_at")
            val createdAt: String, // 2024-02-21 11:15:47
            @SerializedName("id")
            val id: Int, // 3
            @SerializedName("lead_id")
            val leadId: Int, // 3
            @SerializedName("remarks")
            val remarks: String, // New Lead
            @SerializedName("remind_date")
            val remindDate: String, // 0000-00-00
            @SerializedName("remind_time")
            val remindTime: String, // 00:00:00
            @SerializedName("status")
            val status: String, // New Lead
            @SerializedName("updated_at")
            val updatedAt: Any, // null
            @SerializedName("user_id")
            val userId: Int // 3
        )

        data class LeadHistory(
            @SerializedName("interested_category")
            val interestedCategory: String, // AC
            @SerializedName("store_name")
            val storeName: String // Panchkula
        )
    }
}