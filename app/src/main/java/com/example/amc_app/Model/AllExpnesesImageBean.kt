package com.example.amc_app.Model


import com.google.gson.annotations.SerializedName

data class AllExpnesesImageBean(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("error")
    val error: Boolean, // false
    @SerializedName("msg")
    val msg: String // Dataloaded successfully.
) {
    data class Data(
        @SerializedName("base_uri")
        val baseUri: String, // http://vqq.mqs.mybluehostin.me/api/
        @SerializedName("imgs")
        val imgs: List<Img>
    ) {
        data class Img(
            @SerializedName("expense_id")
            val expenseId: Int, // 98
            @SerializedName("id")
            val id: Int, // 103
            @SerializedName("img_url")
            val imgUrl: String // uploads/3249-img_20220825_125910578.png
        )
    }
}