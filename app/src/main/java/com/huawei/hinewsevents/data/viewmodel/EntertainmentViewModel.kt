package com.huawei.hinewsevents.data.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.huawei.hinewsevents.data.model.Article
import com.huawei.hinewsevents.data.model.NewsArticle
import com.huawei.hinewsevents.data.network.NewsApiService
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class EntertainmentViewModel : ViewModel() {

    val TAG: String = EntertainmentViewModel::class.simpleName.toString()

    var entertainmentNewsListData: MutableLiveData<NewsArticle> = MutableLiveData<NewsArticle>()
    var entertainmentNewsListLoadError = MutableLiveData<Boolean>()
    var entertainmentNewsListLoading = MutableLiveData<Boolean>()

    var newsData: MutableLiveData<Article> = MutableLiveData<Article>()
    var newsDataLoadError = MutableLiveData<Boolean>()
    var newsDataLoading = MutableLiveData<Boolean>()

    fun refreshData(page: Int) {
        getDataFromAPI(page)
    }

    private fun getDataFromAPI(page: Int) {

        Log.i(TAG, "getDataFromAPI")

        entertainmentNewsListLoading.value = true

        val apiCall = NewsApiService.getApiCallSearchNews(
            NewsApiService.QUERY_HUAWEI,
            NewsApiService.TOPIC_ENT,
            page,
            NewsApiService.PAGE_SIZE,
            NewsApiService.TRUE_STR,
            NewsApiService.TRUE_STR
        )

        Log.i(TAG, "apiCall.url : ${apiCall.request().url()}")
        Log.i(TAG, "apiCall.headers : ${apiCall.request().headers()}")

        apiCall.enqueue(object : Callback, retrofit2.Callback<NewsArticle> {

            override fun onResponse(call: Call<NewsArticle>, response: Response<NewsArticle>) {
                //Log.i(TAG, "apiCall.onResponse body : ${response.body().toString()}")
                if (response.code() == 200) {
                    entertainmentNewsListData.value = response.body()
                    entertainmentNewsListLoadError.value = false
                    entertainmentNewsListLoading.value = false
                } else {
                    entertainmentNewsListLoadError.value = true
                    entertainmentNewsListLoading.value = false
                }

            }

            override fun onFailure(call: Call<NewsArticle>, t: Throwable) {
                Log.i(TAG, "apiCall.enqueue.onFailure error  : $t")
                Log.i(TAG, "apiCall.enqueue.onFailure error message : ${t.message.toString()}")
                entertainmentNewsListData.value = null
                entertainmentNewsListLoadError.value = true
                entertainmentNewsListLoading.value = false
            }
        })

    }

}