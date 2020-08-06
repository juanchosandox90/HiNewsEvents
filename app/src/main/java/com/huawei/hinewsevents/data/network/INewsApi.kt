package com.huawei.hinewsevents.data.network

import com.huawei.hinewsevents.data.model.NewsArticle
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface INewsApi {

    @GET("/v1/latest_headlines")
    fun fetchLatestHeadLines(
        @Header("x-rapidapi-host") api_host: String,
        @Header("x-rapidapi-key") api_key: String,
        @Query("topic") topic: String?,          // topics : news, sport, tech, world, finance, politics, business, economics, entertainment
        @Query("lang") lang: String,            // en / tr
        @Query("country") country: String,      // countries ISO 3166-1 alpha-2 codes : TR / US / CN
        @Query("media") media: String           // True / False
    ):Call<NewsArticle>


    @GET("/v1/search")
    fun fetchNewsSearch(
        @Header("x-rapidapi-host") api_host: String,
        @Header("x-rapidapi-key") api_key: String,
        @Query("q") query: String,              // REQUIRED to search. Has to be URL-encoded
        @Query("lang") lang: String,            // en / tr
        @Query("country") country: String,      // countries ISO 3166-1 alpha-2 codes : TR / US / CN
        @Query("sort_by") sort_by: String,      // relevancy / date  / rank
        @Query("ranked_only") ranked_only: String,  // True / False
        @Query("topic") topic: String,          // topics : news, sport, tech, world, finance, politics, business, economics, entertainment
        @Query("media") media: String,          // True / False
        @Query("page") page: String,            // Defaults to 1
        @Query("page_size") page_size: String   // Defaults to 50, max is 100. Not included in Basic plan. Basic Plan defaults is 5
        //@Query("search_in") search_in: String,        // title_summary_en / title  / summary
        //@Query("from") from: String,                  // from date time. default 2 days ago : 2020-07-21 00:00:00 / 2020-07-21
        //@Query("to") to: String,                      // to date time. default NULL : 2020-07-22 00:00:00 / 2020-07-22
        //@Query("sources") sources: String,            // nytimes.com,cnn.com,wsj.com
        //@Query("not_sources") not_source: String,     // nytimes.com,cnn.com,wsj.com
    ): Call<NewsArticle>


    @GET("/v1/search_free")
    fun fetchNewsSearchFree(
        @Header("x-rapidapi-host") api_host: String,
        @Header("x-rapidapi-key") api_key: String,
        @Query("q") query: String,              // REQUIRED to search. Has to be URL-encoded
        @Query("sort_by") sort_by: String,      // relevancy / date  / rank
        @Query("ranked_only") ranked_only: String,  // True / False
        @Query("page_size") page_size: String,  // Defaults to 50, max is 100. Not included in Basic plan. Basic Plan defaults is 5
        @Query("page") page: String,            // Defaults to 1
        @Query("media") media: String,          // True / False
        @Query("lang") lang: String             // en / tr
    ): Call<List<NewsArticle>>


}