package cz.hlinkapp.gvpintranet.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import cz.hlinkapp.gvpintranet.data.repositories.MainRepository
import cz.hlinkapp.gvpintranet.data.utils.event_handling.RequestInfo
import cz.hlinkapp.gvpintranet.model.Article
import cz.hlinkapp.gvpintranet.model.PieceOfNews
import javax.inject.Inject

/**
 * Main ViewModel for the main screens - handles articles and news/events lists.
 */
class MainViewModel @Inject constructor(repository: MainRepository) : ViewModel() {

    private val mMainRepository: MainRepository = repository

    private var mNews : LiveData<List<PieceOfNews>>? = null
    private var mPagedArticles : LiveData<PagedList<Article>>? = null

    val news : LiveData<List<PieceOfNews>>? get() = mNews
    val pagedArticles : LiveData<PagedList<Article>>? get() = mPagedArticles

    val articleStatus : LiveData<RequestInfo> get() = mMainRepository.articleStatus
    val newsStatus: LiveData<RequestInfo> get() = mMainRepository.newsStatus

    /**
     * Initializes the news/events-related data.
     * First loads the data from a local db, then attempts to refresh the data if the right conditions are met. To force refresh the data, use [forceRefreshNews].
     */
    fun initNews() {
        if (mNews == null) mNews = mMainRepository.getNews() //only trigger loading data if no data is loaded
    }

    /**
     * Initializes the articles-related data.
     * First loads the data from a local db, then attempts to refresh the data if the right conditions are met. To force refresh the data, use [forceRefreshArticles].
     */
    fun initArticles() {
        if (mPagedArticles == null) mPagedArticles = mMainRepository.getPagedArticles() //only trigger loading data if no data is loaded
    }

    /**
     * Attempts to force refresh the articles, even if the data is not old.
     */
    fun forceRefreshArticles() {
        mPagedArticles = mMainRepository.forceRefreshArticles()
    }

    /**
     * Attempts to force refresh the events/news, even if the data is not old.
     */
    fun forceRefreshNews() {
        mNews = mMainRepository.forceRefreshNews()
    }

}