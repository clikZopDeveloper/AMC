package com.example.amc_app.Model


import com.google.gson.annotations.SerializedName

data class ProjectDetailBean(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("error")
    val error: Boolean, // false
    @SerializedName("msg")
    val msg: String // DataSuccessfully Loaded
) {
    data class Data(
        @SerializedName("amc_cost")
        val amcCost: String, // 7507.00
        @SerializedName("amc_date")
        val amcDate: String, // 2025-01-28
        @SerializedName("amc_percentage")
        val amcPercentage: String, // 11.00
        @SerializedName("amc_type")
        val amcType: String, // product
        @SerializedName("created_at")
        val createdAt: String, // 2024-02-1315:57:27
        @SerializedName("customer_id")
        val customerId: Int, // 2
        @SerializedName("delivery_date")
        val deliveryDate: String, // 2023-01-28
        @SerializedName("description")
        val description: String, // AllSet
        @SerializedName("discount")
        val discount: String, // 0.00
        @SerializedName("domain")
        val domain: Int, // 1
        @SerializedName("domain_price")
        val domainPrice: String, // 0.00
        @SerializedName("foc_time")
        val focTime: Int, // 1
        @SerializedName("hosting_price")
        val hostingPrice: String, // 0.00
        @SerializedName("id")
        val id: Int, // 7
        @SerializedName("mail_template_id")
        val mailTemplateId: Any, // null
        @SerializedName("payable_amt")
        val payableAmt: String, // 0.00
        @SerializedName("payment")
        val payment: Any, // null
        @SerializedName("payment_status")
        val paymentStatus: String, // Pending
        @SerializedName("project_amt")
        val projectAmt: String, // 0.00
        @SerializedName("project_name")
        val projectName: String, // GoHeights
        @SerializedName("project_percentage")
        val projectPercentage: String, // 0.00
        @SerializedName("project_service")
        val projectService: List<ProjectService>,
        @SerializedName("project_sub_id")
        val projectSubId: Int, // 4
        @SerializedName("project_sub_name")
        val projectSubName: String, // Dynamic with Backendpanel
        @SerializedName("project_tech_id")
        val projectTechId: Int, // 2
        @SerializedName("project_technology")
        val projectTechnology: String, // Wordpress
        @SerializedName("project_type")
        val projectType: String, // WebsiteAMC
        @SerializedName("project_type_id")
        val projectTypeId: Int, // 1
        @SerializedName("project_url")
        val projectUrl: String, // https://goheightsrealty.com/
        @SerializedName("server")
        val server: String, // wp.clikzop@gmail.com
        @SerializedName("server_id")
        val serverId: Int, // 3
        @SerializedName("service_id")
        val serviceId: String, // 2,3, 4, 7
        @SerializedName("source")
        val source: String, // Reference
        @SerializedName("status")
        val status: String, // Active
        @SerializedName("updated_at")
        val updatedAt: Any, // null
        @SerializedName("verified")
        val verified: Any // null
    ) {
        data class ProjectService(
            @SerializedName("amc_id")
            val amcId: Int, // 7
            @SerializedName("created_at")
            val createdAt: String, // 2024-02-0313:00:34
            @SerializedName("id")
            val id: Int, // 27
            @SerializedName("service_id")
            val serviceId: Int, // 2
            @SerializedName("service_name")
            val serviceName: String, // Hosting
            @SerializedName("service_price")
            val servicePrice: String, // 2500.00
            @SerializedName("updated_at")
            val updatedAt: Any // null
        )
    }
}