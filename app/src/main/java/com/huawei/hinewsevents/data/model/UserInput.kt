package com.huawei.hinewsevents.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserInput (
    @Expose @SerializedName("q") val q : String,
    @Expose @SerializedName("search_in") val search_in : String,
    @Expose @SerializedName("lang") val lang : String,
    @Expose @SerializedName("country") val country : String,
    @Expose @SerializedName("from") val from : String,
    @Expose @SerializedName("to") val to : String,
    @Expose @SerializedName("ranked_only") val ranked_only : Boolean,
    @Expose @SerializedName("from_rank") val from_rank : String,
    @Expose @SerializedName("to_rank") val to_rank : String,
    @Expose @SerializedName("sort_by") val sort_by : String,
    @Expose @SerializedName("page") val page : Int,
    @Expose @SerializedName("size") val size : Int,
    @Expose @SerializedName("sources") val sources : String,
    @Expose @SerializedName("not_sources") val not_sources : String,
    @Expose @SerializedName("topic") val topic : String,
    @Expose @SerializedName("media") val media : Boolean
)