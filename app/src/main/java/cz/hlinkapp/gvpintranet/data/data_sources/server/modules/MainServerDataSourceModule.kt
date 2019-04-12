package cz.hlinkapp.gvpintranet.data.data_sources.server.modules

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cz.hlinkapp.gvpintranet.data.data_sources.room.MainDao
import cz.hlinkapp.gvpintranet.data.data_sources.server.IntranetService
import cz.hlinkapp.gvpintranet.data.utils.event_handling.RequestInfo
import cz.hlinkapp.gvpintranet.model.Article
import cz.hlinkapp.gvpintranet.model.PieceOfNews
import cz.hlinkapp.gvpintranet.utils.ConnectivityChecker
import cz.hlinkapp.gvpintranet.utils.SharedPrefUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executor
import javax.inject.Inject
import javax.inject.Singleton

/**
 * A [cz.hlinkapp.gvpintranet.data.data_sources.server.ServerDataSource] module that handles downloading of the main content - articles and news/events.
 */
@Singleton
class MainServerDataSourceModule @Inject constructor(
    executor: Executor,
    dao: MainDao,
    sharedPrefUtil: SharedPrefUtil,
    connectivityChecker: ConnectivityChecker,
    intranetService: IntranetService
){

    private val mExecutor = executor
    private val mDao = dao
    private val mSharedPrefUtil = sharedPrefUtil
    private val mConnectivityChecker = connectivityChecker
    private val mIntranetService = intranetService

    private val mArticleStatus = MutableLiveData<RequestInfo>(RequestInfo.notStarted())
    private val mNewsStatus = MutableLiveData<RequestInfo>(RequestInfo.notStarted())
    private val mLoadMoreArticlesStatus = MutableLiveData<RequestInfo>(RequestInfo.notStarted())

    val articleStatus: LiveData<RequestInfo> get() = mArticleStatus
    val loadMoreArticlesStatus : LiveData<RequestInfo> get() = mLoadMoreArticlesStatus

    val newsStatus: LiveData<RequestInfo> get() = mNewsStatus

    /**
     * Downloads the most recent articles from the server.
     * @param forceRefresh If false, the data will be refreshed only if it's old, true to force refresh it.
     */
    fun refreshArticleData(forceRefresh : Boolean) {
        val status = mArticleStatus
        if ((forceRefresh || mSharedPrefUtil.isArticleDataOld()) && status.value?.isProcessing() == false) if (mConnectivityChecker.isConnected()) {
            status.postValue(RequestInfo.processing())
            mIntranetService.getLatestArticles().enqueue(object : Callback<List<Article>> {
                override fun onFailure(call: Call<List<Article>>, t: Throwable) = status.postValue(
                    RequestInfo.done(
                        RequestInfo.RequestResult.FAILED))
                override fun onResponse(call: Call<List<Article>>, response: Response<List<Article>>) =
                    if (response.isSuccessful && response.body() != null) {
                        mExecutor.execute {
                            mDao.saveArticles(response.body()!!)
                            mSharedPrefUtil.updateArticlesLastFetchedTime()
                            status.postValue(RequestInfo.done(RequestInfo.RequestResult.OK))
                        } } else status.postValue(RequestInfo.done(RequestInfo.RequestResult.FAILED)) })
        } else status.postValue(RequestInfo.done(RequestInfo.RequestResult.NO_INTERNET))
    }

    /**
     * Loads another chunk of articles, starting from the ID provided. Use for paging data.
     */
    fun loadMoreArticles(lastArticleId: Int) {
        val status = mLoadMoreArticlesStatus
        if (mArticleStatus.value?.isProcessing() == false && status.value?.isProcessing() == false) if (mConnectivityChecker.isConnected()) {
            status.postValue(RequestInfo.processing())
            mIntranetService.getMoreArticles(lastArticleId).enqueue(object : Callback<List<Article>> {
                override fun onFailure(call: Call<List<Article>>, t: Throwable) = status.postValue(
                    RequestInfo.done(
                        RequestInfo.RequestResult.FAILED))
                override fun onResponse(call: Call<List<Article>>, response: Response<List<Article>>) =
                    if (response.isSuccessful && response.body() != null) {
                        mExecutor.execute {
                            mDao.saveArticles(response.body()!!)
                            status.postValue(RequestInfo.done(RequestInfo.RequestResult.OK))
                        } } else status.postValue(RequestInfo.done(RequestInfo.RequestResult.FAILED)) })
        } else status.postValue(RequestInfo.done(RequestInfo.RequestResult.NO_INTERNET))
    }


    /**
     * Downloads the most recent news from the server.
     * @param forceRefresh If false, the data will be refreshed only if it's old, true to force refresh it.
     */
    fun refreshNewsData(forceRefresh: Boolean) {
        val status = mNewsStatus
        if ((forceRefresh || mSharedPrefUtil.isNewsDataOld()) && status.value?.isProcessing() == false) if (mConnectivityChecker.isConnected()) {
            status.postValue(RequestInfo.processing())
            mIntranetService.getLatestNews().enqueue(object : Callback<List<PieceOfNews>> {
                override fun onFailure(call: Call<List<PieceOfNews>>, t: Throwable) = status.postValue(RequestInfo.done(RequestInfo.RequestResult.FAILED))
                override fun onResponse(call: Call<List<PieceOfNews>>, response: Response<List<PieceOfNews>>) =
                    if (response.isSuccessful && response.body() != null) {
                        mExecutor.execute {
                            mDao.saveNews(response.body()!!)
                            mSharedPrefUtil.updateNewsLastFetchedTime()
                            status.postValue(RequestInfo.done(RequestInfo.RequestResult.OK))
                        } } else status.postValue(RequestInfo.done(RequestInfo.RequestResult.FAILED)) })
        } else status.postValue(RequestInfo.done(RequestInfo.RequestResult.NO_INTERNET))
    }
}