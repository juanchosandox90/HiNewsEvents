package com.huawei.hinewsevents.data.network.pagination

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.huawei.hinewsevents.data.model.Article
import io.reactivex.disposables.CompositeDisposable


class NewsDataSourceFactory(
    private val compositeDisposable: CompositeDisposable,
    topicDataSource: String,
    languageDataSource: String,
    countryDataSource: String
) : DataSource.Factory<Int, Article>() {

    val TAG: String = NewsDataSourceFactory::class.simpleName.toString()

    val newsDataSourceLiveData = MutableLiveData<NewsDataSource>()

    private var newsDataSource: NewsDataSource? = null

    private var topicData = topicDataSource
    private var languageData  = languageDataSource
    private var countryData = countryDataSource

    override fun create(): DataSource<Int, Article> {
        Log.i(TAG, "DataSource params : topicData :$topicData - languageData :$languageData - countryData :$countryData -  ")
        // Getting our Data source object
        newsDataSource = NewsDataSource(compositeDisposable, topicData, languageData, countryData)

        // Posting the Data source to get the values
        newsDataSourceLiveData.postValue(newsDataSource)

        return newsDataSource as NewsDataSource
    }


}