package cz.hlinkapp.gvpintranet.data.data_sources.server.modules

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cz.hlinkapp.gvpintranet.data.data_sources.room.MainDao
import cz.hlinkapp.gvpintranet.data.data_sources.server.IntranetService
import cz.hlinkapp.gvpintranet.data.utils.event_handling.RequestInfo
import cz.hlinkapp.gvpintranet.model.Comment
import cz.hlinkapp.gvpintranet.utils.ConnectivityChecker
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executor
import javax.inject.Inject
import javax.inject.Singleton

/**
 * A [cz.hlinkapp.gvpintranet.data.data_sources.server.ServerDataSource] module that handles data used in the article detail screen, such as comments.
 */
@Singleton
class ArticleDetailServerDataSourceModule @Inject constructor(
    executor: Executor,
    dao: MainDao,
    connectivityChecker: ConnectivityChecker,
    intranetService: IntranetService
){

    private val mExecutor = executor
    private val mDao = dao
    private val mConnectivityChecker = connectivityChecker
    private val mIntranetService = intranetService

    private val mCommentsStatus = MutableLiveData<RequestInfo>(RequestInfo.notStarted())
    private val mLoadMoreCommentsStatus = MutableLiveData<RequestInfo>(RequestInfo.notStarted())
    private val mPostCommentStatus = MutableLiveData<RequestInfo>(RequestInfo.notStarted())

    val commentsStatus : LiveData<RequestInfo> get() = mCommentsStatus
    val loadMoreCommentsStatus : LiveData<RequestInfo> get() = mLoadMoreCommentsStatus
    val postCommentStatus : LiveData<RequestInfo> get() = mPostCommentStatus

    /**
     * Downloads the most recent comments for the given [articleId].
     */
    fun loadComments(articleId: Int) {
        val status = mCommentsStatus
        if (status.value?.isProcessing() == false) if (mConnectivityChecker.isConnected()) {
            status.postValue(RequestInfo.processing())
            mIntranetService.getComments(articleId).enqueue(object : Callback<List<Comment>> {
                override fun onFailure(call: Call<List<Comment>>, t: Throwable) = status.postValue(
                    RequestInfo.done(
                        RequestInfo.RequestResult.FAILED))
                override fun onResponse(call: Call<List<Comment>>, response: Response<List<Comment>>) =
                    if (response.isSuccessful && response.body() != null) {
                        mExecutor.execute {
                            mDao.saveComments(response.body()!!)
                            status.postValue(RequestInfo.done(RequestInfo.RequestResult.OK))
                        } } else status.postValue(RequestInfo.done(RequestInfo.RequestResult.FAILED)) })
        } else status.postValue(RequestInfo.done(RequestInfo.RequestResult.NO_INTERNET))
    }

    /**
     * Loads another chunk of comments for the given [articleId], starting from the [lastCommentId] provided. Use for paging data.
     */
    fun loadMoreComments(articleId : Int, lastCommentId: Int) {
        val status = mLoadMoreCommentsStatus
        if (mCommentsStatus.value?.isProcessing() == false && status.value?.isProcessing() == false) if (mConnectivityChecker.isConnected()) {
            status.postValue(RequestInfo.processing())
            mIntranetService.getMoreComments(articleId, lastCommentId).enqueue(object : Callback<List<Comment>> {
                override fun onFailure(call: Call<List<Comment>>, t: Throwable) = status.postValue(
                    RequestInfo.done(
                        RequestInfo.RequestResult.FAILED))
                override fun onResponse(call: Call<List<Comment>>, response: Response<List<Comment>>) =
                    if (response.isSuccessful && response.body() != null) {
                        mExecutor.execute {
                            mDao.saveComments(response.body()!!)
                            status.postValue(RequestInfo.done(RequestInfo.RequestResult.OK))
                        } } else status.postValue(RequestInfo.done(RequestInfo.RequestResult.FAILED)) })
        } else status.postValue(RequestInfo.done(RequestInfo.RequestResult.NO_INTERNET))
    }

    /**
     * Posts the given [comment] to the server.
     * The [comment] object must have the following fields set: [Comment.articleId], [Comment.text], [Comment.email], [Comment.author];
     */
    fun postComment(comment: Comment) {
        val status = mPostCommentStatus
        if (status.value?.isProcessing() == false) if (mConnectivityChecker.isConnected()) {
            status.postValue(RequestInfo.processing())
            mIntranetService.postComment(comment).enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) = status.postValue(RequestInfo.done(RequestInfo.RequestResult.FAILED))
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) =
                    if (response.isSuccessful) status.postValue(RequestInfo.done(RequestInfo.RequestResult.OK))
                    else status.postValue(RequestInfo.done(RequestInfo.RequestResult.FAILED)) })
        } else status.postValue(RequestInfo.done(RequestInfo.RequestResult.NO_INTERNET))
    }
}