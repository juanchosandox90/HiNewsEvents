package com.huawei.hinewsevents.data.network

import android.util.Log
import com.huawei.hinewsevents.data.model.NewsArticle
import com.huawei.hinewsevents.data.network.NewsApiService.Companion.COUNTRY_EN
import com.huawei.hinewsevents.data.network.NewsApiService.Companion.LANG_EN
import com.huawei.hinewsevents.data.network.NewsApiService.Companion.TOPIC_NEWS
import com.huawei.hinewsevents.data.network.NewsApiService.Companion.getAPIInstance
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Header
import retrofit2.http.Query

class NewsApiService {


    companion object{
        val TAG = "NewsApiService"

        val BASE_URL: String = "https://newscatcher.p.rapidapi.com"
        val API_HOST: String = "newscatcher.p.rapidapi.com"
        val API_KEY: String = "2da5747c4dmshbb447d1b2763079p16db24jsnb3b4de20e958" // BuildConfig.API_KEY

        //region "someApiParams"
        val LANG_TR = "tr"
        val LANG_EN = "en"

        val COUNTRY_TR = "TR"
        val COUNTRY_EN = "US"

        val QUERY_HUAWEI = "huawei"

        val TOPIC_NEWS = "news"
        val TOPIC_TECH = "tech"
        val TOPIC_WORLD = "world"
        val TOPIC_FIN = "finance"
        val TOPIC_BUS = "business"
        val TOPIC_ECO = "economics"
        val TOPIC_ENT = "entertainment"

        val SORT_RELEVANCY = "relevancy"
        val SORT_DATE = "date"
        val SORT_RANK = "rank"

        val TRUE_STR = "True"
        val FALSE_STR = "False"

        val PAGE_SIZE = 5

        //endregion

        /**
         * query was value is set to static with NewsApiService.QUERY_HUAWEI, it can be changeable
         * topics : news, sport, tech, world, finance, politics, business, economics, entertainment
         * lang :  en / tr
         * country :  countries ISO 3166-1 alpha-2 codes : TR / US / CN
         * sort : // relevancy / date  / rank
         * page : current page
         * page_size : default 5. much more are not support for free access
         * media : True / False
         * ranked_only : True / False
         */
        fun getApiCallSearchNewsSingle(topic: String, page: Int, page_size: Int, lang: String, country: String): Single<NewsArticle> {
            return getAPIInstance().fetchNewsSearchSingle(
                API_HOST,
                API_KEY,
                QUERY_HUAWEI,
                topic,
                page.toString(),
                page_size.toString(),
                lang,
                country,
                TRUE_STR,
                SORT_RELEVANCY,
                TRUE_STR
            )
        }

        fun getAPIInstance(): INewsApi {
            Log.i(TAG, "getAPIInstance")
            return getRetrofitInstance().create(INewsApi::class.java)
        }

        private fun getRetrofitInstance(): Retrofit {
            Log.i(TAG, "getRetrofitInstance")
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        }

        /**
         * topics : news, sport, tech, world, finance, politics, business, economics, entertainment
         * lang :  en / tr
         * country :  countries ISO 3166-1 alpha-2 codes : TR / US / CN
         */
        fun getApiCallLatestHeadLines(): Call<NewsArticle> {
            return getAPIInstance().fetchLatestHeadLines(
                API_HOST,
                API_KEY,
                TOPIC_NEWS,
                LANG_EN,
                COUNTRY_EN,
                TRUE_STR
            )
        }


        /**
         * topics : news, sport, tech, world, finance, politics, business, economics, entertainment
         * lang :  en / tr
         * country :  countries ISO 3166-1 alpha-2 codes : TR / US / CN
         * sort : // relevancy / date  / rank
         * page : current page
         * page_size : default 5. much more are not support for free access
         * media : True / False
         * ranked_only : True / False
         */
        fun getApiCallSearchNews(query: String, topic: String, page: Int, page_size: Int, media: String, ranked_only: String): Call<NewsArticle> {
            return getAPIInstance().fetchNewsSearch(
                    API_HOST,
                    API_KEY,
                    query,
                    LANG_EN,
                    COUNTRY_EN,
                    SORT_RELEVANCY,
                    ranked_only,
                    topic,
                    media,
                    page.toString(),
                    page_size.toString()
            )
        }



        // for try
        private var createdRetrofit: INewsApi = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(INewsApi::class.java)


    }


}

