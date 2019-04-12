package cz.hlinkapp.gvpintranet.data.data_sources.server.modules

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cz.hlinkapp.gvpintranet.data.data_sources.room.MainDao
import cz.hlinkapp.gvpintranet.data.data_sources.server.IntranetService
import cz.hlinkapp.gvpintranet.data.utils.event_handling.RequestInfo
import cz.hlinkapp.gvpintranet.model.Article
import cz.hlinkapp.gvpintranet.model.PieceOfNews
import cz.hlinkapp.gvpintranet.utils.ConnectivityChecker
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executor
import javax.inject.Inject
import javax.inject.Singleton

/**
 * A [cz.hlinkapp.gvpintranet.data.data_sources.server.ServerDataSource] module that handles posting articles and events to the server.
 */
@Singleton
class ContentAddServerDataSourceModule @Inject constructor(
    executor: Executor,
    dao: MainDao,
    connectivityChecker: ConnectivityChecker,
    intranetService: IntranetService
){

    private val mExecutor = executor
    private val mConnectivityChecker = connectivityChecker
    private val mIntranetService = intranetService

    private val mPostArticleStatus = MutableLiveData<RequestInfo>(RequestInfo.notStarted())
    private val mPostEventStatus = MutableLiveData<RequestInfo>(RequestInfo.notStarted())

    val postArticleStatus : LiveData<RequestInfo> get() = mPostArticleStatus
    val postEventStatus : LiveData<RequestInfo> get() = mPostEventStatus

    /**
     * Posts the given [article] to the server.
     * The [article] object must have the following fields set: [Article.title], [Article.author], [Article.email] and at least one of [Article.description] or [Article.content].
     */
    fun postArticle(article: Article) {
        val status = mPostArticleStatus
        if (status.value?.isProcessing() == false) if (mConnectivityChecker.isConnected()) {
            status.postValue(RequestInfo.processing())
            mIntranetService.postArticle(article).enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) = status.postValue(RequestInfo.done(RequestInfo.RequestResult.FAILED))
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) =
                    if (response.isSuccessful) status.postValue(RequestInfo.done(RequestInfo.RequestResult.OK))
                    else status.postValue(RequestInfo.done(RequestInfo.RequestResult.FAILED)) })
        } else status.postValue(RequestInfo.done(RequestInfo.RequestResult.NO_INTERNET))
    }

    /**
     * Posts the given event/[pieceOfNews] to the server.
     * The [pieceOfNews] object must have the following fields set: [PieceOfNews.title], [PieceOfNews.description].
     */
    fun postEvent(pieceOfNews: PieceOfNews) {
        val status = mPostEventStatus
        if (status.value?.isProcessing() == false) if (mConnectivityChecker.isConnected()) {
            status.postValue(RequestInfo.processing())
            mIntranetService.postEvent(pieceOfNews).enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) = status.postValue(RequestInfo.done(RequestInfo.RequestResult.FAILED))
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) =
                    if (response.isSuccessful) status.postValue(RequestInfo.done(RequestInfo.RequestResult.OK))
                    else status.postValue(RequestInfo.done(RequestInfo.RequestResult.FAILED)) })
        } else status.postValue(RequestInfo.done(RequestInfo.RequestResult.NO_INTERNET))
    }
}