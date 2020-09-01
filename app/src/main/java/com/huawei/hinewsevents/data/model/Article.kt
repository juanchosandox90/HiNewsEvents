package com.huawei.hinewsevents.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Article (
    @Expose @SerializedName("_id") val id : String,
    @Expose @SerializedName("_score") val _score : String,
    @Expose @SerializedName("title") val title : String,
    @Expose @SerializedName("summary") val summary : String,
    @Expose @SerializedName("link") val link : String,
    @Expose @SerializedName("media") val media : String,
    @Expose @SerializedName("media_content") val media_content : String,
    @Expose @SerializedName("topic") val topic : String,
    @Expose @SerializedName("clean_url") val clean_url : String,
    @Expose @SerializedName("published_date") val published_date : String,
    @Expose @SerializedName("language") val language : String,
    @Expose @SerializedName("country") val country : String,
    @Expose @SerializedName("rank") val rank : Int,
    @Expose @SerializedName("author") val author : String,
    @Expose @SerializedName("rights") val rights : String
) : Serializable