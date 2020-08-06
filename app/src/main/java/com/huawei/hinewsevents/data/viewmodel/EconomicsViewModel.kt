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

class EconomicsViewModel : ViewModel() {

    val TAG: String = EconomicsViewModel::class.simpleName.toString()

    var economicsNewsListData: MutableLiveData<NewsArticle> = MutableLiveData<NewsArticle>()
    var economicsNewsListLoadError = MutableLiveData<Boolean>()
    var economicsNewsListLoading = MutableLiveData<Boolean>()

    var newsData: MutableLiveData<Article> = MutableLiveData<Article>()
    var newsDataLoadError = MutableLiveData<Boolean>()
    var newsDataLoading = MutableLiveData<Boolean>()

    fun refreshData(page: Int) {
        getDataFromAPI(page)
    }

    private fun getDataFromAPI(page: Int) {

        Log.i(TAG, "getDataFromAPI")

        economicsNewsListLoading.value = true

        val apiCall = NewsApiService.getApiCallSearchNews(
            NewsApiService.QUERY_HUAWEI,
            NewsApiService.TOPIC_ECO,
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
                    economicsNewsListData.value = response.body()
                    economicsNewsListLoadError.value = false
                    economicsNewsListLoading.value = false
                } else {
                    economicsNewsListLoadError.value = true
                    economicsNewsListLoading.value = false
                }

            }

            override fun onFailure(call: Call<NewsArticle>, t: Throwable) {
                Log.i(TAG, "apiCall.enqueue.onFailure error  : $t")
                Log.i(TAG, "apiCall.enqueue.onFailure error message : ${t.message.toString()}")
                economicsNewsListData.value = null
                economicsNewsListLoadError.value = true
                economicsNewsListLoading.value = false
            }
        })

    }

}