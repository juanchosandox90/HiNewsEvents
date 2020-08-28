package com.huawei.hinewsevents.data.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.huawei.hinewsevents.data.model.Article
import com.huawei.hinewsevents.data.network.NewsApiService
import com.huawei.hinewsevents.data.network.pagination.NewsDataSource
import com.huawei.hinewsevents.data.network.pagination.NewsDataSourceFactory
import io.reactivex.disposables.CompositeDisposable

class TechNewsViewModel : ViewModel() {

    val TAG: String = TechNewsViewModel::class.simpleName.toString()

    var newsList: LiveData<PagedList<Article>>
    private var compositeDisposable = CompositeDisposable()
    private var newsDataSourceFactory: NewsDataSourceFactory

    init {
        Log.i(TAG, "init params : pageSize : ${NewsApiService.PAGE_SIZE} - initialLoadSizeHint : ${NewsApiService.PAGE_SIZE*2}")
        compositeDisposable = CompositeDisposable()
        // TODO : Language and country preferences can be change with parametric, SharedPreferences
        newsDataSourceFactory = NewsDataSourceFactory(compositeDisposable, NewsApiService.TOPIC_TECH, NewsApiService.LANG_EN, NewsApiService.COUNTRY_EN)
        // TODO : PagedList can be change with Api properties
        val config = PagedList.Config.Builder()
            .setPageSize(NewsApiService.PAGE_SIZE)
            .setInitialLoadSizeHint(NewsApiService.PAGE_SIZE * 2)
            .setEnablePlaceholders(false)
            .build()
        newsList = LivePagedListBuilder(newsDataSourceFactory, config).build()
    }

    fun getState(): LiveData<Boolean> = Transformations.switchMap(
        newsDataSourceFactory.newsDataSourceLiveData,
        NewsDataSource::newsDataLoadingState
    )

    fun getErrorMsg(): LiveData<String> = Transformations.switchMap(
        newsDataSourceFactory.newsDataSourceLiveData,
        NewsDataSource::newsDataErrorMessage
    )

    fun retry() {
        newsDataSourceFactory.newsDataSourceLiveData.value?.retry()
    }

    fun listIsEmpty(): Boolean {
        return newsList.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}