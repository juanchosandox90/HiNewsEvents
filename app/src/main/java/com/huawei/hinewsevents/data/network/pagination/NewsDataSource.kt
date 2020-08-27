package com.huawei.hinewsevents.data.network.pagination

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.huawei.hinewsevents.data.model.Article
import com.huawei.hinewsevents.data.model.NewsArticle
import com.huawei.hinewsevents.data.network.INewsApi
import com.huawei.hinewsevents.data.network.NewsApiService
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers

class NewsDataSource(
    private val compositeDisposable: CompositeDisposable,
    topicDataSource: String,
    languageDataSource: String,
    countryDataSource: String
) : PageKeyedDataSource<Int, Article>() {

    val TAG: String = NewsDataSource::class.simpleName.toString()

    var newsDataLoadingState = MutableLiveData<Boolean>()
    private var retryCompletable: Completable? = null

    private val pageFirst = 1
    private val pageSecond = 2

    private var topicData = topicDataSource
    private var languageData  = languageDataSource
    private var countryData = countryDataSource

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Article>
    ) {
        Log.i(TAG, "loadInitial params : topicData :$topicData - languageData :$languageData - countryData :$countryData -  ")

        newsDataLoadingState.postValue(true)
        compositeDisposable.add(
            NewsApiService.getApiCallSearchNewsSingle(
                topicData,
                pageFirst,
                NewsApiService.PAGE_SIZE,
                languageData,
                countryData
            )
                .subscribe(
                    { response ->
                        newsDataLoadingState.postValue(false)
                        callback.onResult(
                            response.articles,
                            null,
                            pageSecond
                        )
                    },
                    {
                        newsDataLoadingState.postValue(false)
                        Log.i(TAG, "loadInitial subscribe throwable : ${it.message.toString()}")
                        setRetry(Action { loadInitial(params, callback) })
                    }
                )
        )

    }


    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Article>) {
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Article>) {
        Log.i(TAG, "loadAfter params.key : ${params.key}")
        Log.i(TAG, "loadAfter params.requestedLoadSize : ${params.requestedLoadSize}")

        newsDataLoadingState.postValue(true)
        compositeDisposable.add(
            NewsApiService.getApiCallSearchNewsSingle(
                topicData,
                params.key,
                NewsApiService.PAGE_SIZE,
                languageData,
                countryData
            )
                .subscribe(
                    { response ->
                        newsDataLoadingState.postValue(false)
                        callback.onResult(
                            response.articles,
                            params.key + 1
                        )
                    },
                    {
                        newsDataLoadingState.postValue(false)
                        setRetry( Action { loadAfter(params, callback) } )
                    }
                )
        )
    }

    fun retry() {
        Log.i(TAG, "retry")
        if (retryCompletable != null) {
            compositeDisposable.add(retryCompletable!!
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe())
        }
    }

    private fun setRetry(action: Action?) {
        Log.i(TAG, "setRetryCompletable")
        retryCompletable = if (action == null) null else Completable.fromAction(action)
    }


}