package com.huawei.hinewsevents.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class NewsArticle(

    @SerializedName("status") val status : String,
    @SerializedName("total_hits") val total_hits : Int,
    @SerializedName("page") val page : Int,
    @SerializedName("total_pages") val total_pages : Int,
    @SerializedName("page_size") val page_size : Int,

    @SerializedName("articles") val articles : List<Article>,
    @SerializedName("user_input") val user_input : UserInput

)

