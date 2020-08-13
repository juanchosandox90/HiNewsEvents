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

class TechNewsViewModel : ViewModel() {

    val TAG: String = TechNewsViewModel::class.simpleName.toString()

    var techNewsListData: MutableLiveData<NewsArticle> = MutableLiveData<NewsArticle>()
    var techNewsListLoadError = MutableLiveData<Boolean>()
    var techNewsListLoading = MutableLiveData<Boolean>()

    var newsData: MutableLiveData<Article> = MutableLiveData<Article>()
    var newsDataLoadError = MutableLiveData<Boolean>()
    var newsDataLoading = MutableLiveData<Boolean>()

    fun refreshData(page: Int) {
        getDataFromAPI(page)
    }

    private fun getDataFromAPI(page: Int) {

        Log.i(TAG, "getDataFromAPI")

        techNewsListLoading.value = true

        val apiCall = NewsApiService.getApiCallSearchNews(
            NewsApiService.QUERY_HUAWEI,
            NewsApiService.TOPIC_TECH,
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
                    techNewsListData.value = response.body()
                    techNewsListLoadError.value = false
                    techNewsListLoading.value = false

                    //var articleResponse: NewsArticle = response.body()!!
                    //// page 1
                    //if( page == 1 ){
                    //    Log.i(TAG, "onResponse firstCall page ${articleResponse.page} = user_input ${articleResponse.user_input.page} :  articleSize : ${articleResponse.articles.size}")

                    //    //var twiceList: ArrayList<Article> = articleResponse.articles as ArrayList<Article>
                    //    //(articleResponse.articles as ArrayList<Article>).addAll( twiceList )

                    //    techNewsListData.value = articleResponse
                    //    techNewsListLoadError.value = false
                    //    techNewsListLoading.value = false
                    //}else{
                    //    Log.i(TAG, "onResponse pagedCalls page ${articleResponse.page} != user_input ${articleResponse.user_input.page}")
                    //    var oldList: ArrayList<Article> = techNewsListData.value?.articles as ArrayList<Article>
                    //    Log.i(TAG, "onResponse pagedCalls oldList${oldList.size}")
                    //    var newList: ArrayList<Article> = articleResponse.articles as ArrayList<Article>
                    //    oldList.addAll(newList)
                    //    Log.i(TAG, "onResponse pagedCalls oldList + newList${oldList.size}")
                    //    Log.i(TAG, "onResponse pagedCalls clear articleResponse.articles")
                    //    (articleResponse.articles as ArrayList<Article>).clear()
                    //    Log.i(TAG, "onResponse add all lists to articleResponse.articles")
                    //    (articleResponse.articles as ArrayList<Article>).addAll( oldList )
                    //    Log.i(TAG, "onResponse pagedCalls new articleSize : ${articleResponse.articles.size}")

                    //    techNewsListData.value = articleResponse
                    //    techNewsListLoadError.value = false
                    //    techNewsListLoading.value = false
                    //}

                } else {
                    techNewsListLoadError.value = true
                    techNewsListLoading.value = false
                }

            }

            override fun onFailure(call: Call<NewsArticle>, t: Throwable) {
                Log.i(TAG, "apiCall.enqueue.onFailure error  : $t")
                Log.i(TAG, "apiCall.enqueue.onFailure error message : ${t.message.toString()}")
                techNewsListData.value = null
                techNewsListLoadError.value = true
                techNewsListLoading.value = false
            }
        })

    }

}