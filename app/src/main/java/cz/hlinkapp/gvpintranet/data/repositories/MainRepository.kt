package cz.hlinkapp.gvpintranet.data.repositories

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import cz.hlinkapp.gvpintranet.data.data_sources.room.MainDao
import cz.hlinkapp.gvpintranet.data.data_sources.server.ServerDataSource
import cz.hlinkapp.gvpintranet.data.paging.ArticleBoundaryCallback
import cz.hlinkapp.gvpintranet.data.utils.event_handling.RequestInfo
import cz.hlinkapp.gvpintranet.model.Article
import cz.hlinkapp.gvpintranet.model.PieceOfNews
import javax.inject.Inject
import javax.inject.Singleton

/**
 * A repository for handling main data - articles and events/news.
 * This is the main class that decides from which data source the data will be loaded and handles the refreshing.
 */
@Singleton
class MainRepository @Inject constructor(
    dao: MainDao,
    serverDataSource: ServerDataSource,
    articleBoundaryCallback: ArticleBoundaryCallback
) {
    private val mMainDao = dao
    private val mServerDataSource = serverDataSource
    private val mArticleBoundaryCallback = articleBoundaryCallback

    val articleStatus : LiveData<RequestInfo> get() = mServerDataSource.articleStatus
    val newsStatus: LiveData<RequestInfo> get() = mServerDataSource.newsStatus

    /**
     * Returns a LiveData with paged articles from the db. Also refreshes the data if the right conditions are met.
     * You can observe the status of the refreshing using [articleStatus].
     */
    fun getPagedArticles() : LiveData<PagedList<Article>> {
        return getPagedArticlesInternal(false)
    }

    /**
     * Forces a refresh of the articles. This initiates a refresh even if the data is not old.
     * You can observe the status of the refreshing using [articleStatus].
     */
    fun forceRefreshArticles() : LiveData<PagedList<Article>> {
        return getPagedArticlesInternal(true)
    }

    /**
     * Internal method for getting articles.
     * [forceRefresh] whether to force the refresh even if the data is not old or not.
     */
    private fun getPagedArticlesInternal(forceRefresh: Boolean): LiveData<PagedList<Article>> {
        mServerDataSource.refreshArticleData(forceRefresh) //refresh articles
        val dataSourceFactory = mMainDao.getPagedArticles()
        return LivePagedListBuilder(dataSourceFactory, PagedList.Config.Builder().setPageSize(ARTICLES_PAGE_SIZE).setEnablePlaceholders(true).build())
            .setBoundaryCallback(mArticleBoundaryCallback)
            .build()
    }

    /**
     * Returns a LiveData with news/events from the db. Also refreshes the data if the right conditions are met.
     * You can observe the status of the refreshing using [newsStatus].
     */
    fun getNews() : LiveData<List<PieceOfNews>> {
        return getNewsInternal(false)
    }

    /**
     * Forces a refresh of news/events. This initiates a refresh even if the data is not old.
     * You can observe the status of the refreshing using [newsStatus].
     */
    fun forceRefreshNews() : LiveData<List<PieceOfNews>> {
        return getNewsInternal(true)
    }

    /**
     * Internal method for getting events/news.
     * [forceRefresh] whether to force the refresh even if the data is not old or not.
     */
    private fun getNewsInternal(forceRefresh: Boolean) : LiveData<List<PieceOfNews>> {
        mServerDataSource.refreshNewsData(forceRefresh)
        return mMainDao.getNews()
    }

    companion object {
        private const val ARTICLES_PAGE_SIZE = 15
    }

}