package com.huawei.hinewsevents.data.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback
import com.huawei.hinewsevents.data.model.Article
import com.huawei.hinewsevents.data.model.NewsArticle
import com.huawei.hinewsevents.data.network.NewsApiService

class LatestHeadLinesViewModel : ViewModel() {

    val TAG: String = LatestHeadLinesViewModel::class.simpleName.toString()

    var latestNewsListData: MutableLiveData<NewsArticle> = MutableLiveData<NewsArticle>()
    var latestNewsListLoadError = MutableLiveData<Boolean>()
    var latestNewsListLoading = MutableLiveData<Boolean>()

    var newsData: MutableLiveData<Article> = MutableLiveData<Article>()
    var newsDataLoadError = MutableLiveData<Boolean>()
    var newsDataLoading = MutableLiveData<Boolean>()


    fun refreshData() {
        getDataFromAPI()
    }

    private fun getDataFromAPI() {

        Log.i(TAG, "getDataFromAPI")

        latestNewsListLoading.value = true

        val apiCall = NewsApiService.getApiCallLatestHeadLines()

        Log.i(TAG, "apiCall.url : ${apiCall.request().url()}")
        Log.i(TAG, "apiCall.headers : ${apiCall.request().headers()}")

        apiCall.enqueue(object : Callback, retrofit2.Callback<NewsArticle> {

            override fun onResponse(call: Call<NewsArticle>, response: Response<NewsArticle>) {
                //Log.i(TAG, "apiCall.onResponse body : ${response.body().toString()}")
                if (response.code() == 200) {
                    latestNewsListData.value = response.body()
                    latestNewsListLoadError.value = false
                    latestNewsListLoading.value = false
                } else {
                    latestNewsListLoadError.value = true
                    latestNewsListLoading.value = false
                }

            }

            override fun onFailure(call: Call<NewsArticle>, t: Throwable) {
                Log.i(TAG, "apiCall.enqueue.onFailure error  : $t")
                Log.i(TAG, "apiCall.enqueue.onFailure error message : ${t.message.toString()}")
                latestNewsListData.value = null
                latestNewsListLoadError.value = true
                latestNewsListLoading.value = false
            }
        })

    }


}