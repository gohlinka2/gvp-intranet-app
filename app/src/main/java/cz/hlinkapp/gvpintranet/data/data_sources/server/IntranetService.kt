package cz.hlinkapp.gvpintranet.data.data_sources.server

import cz.hlinkapp.gvpintranet.contracts.ServerContract
import cz.hlinkapp.gvpintranet.model.Article
import cz.hlinkapp.gvpintranet.model.Comment
import cz.hlinkapp.gvpintranet.model.PieceOfNews
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * A Retrofit service for communicating with the app's server API.
 */
interface IntranetService {

    /**
     * Will retrieve the most recent articles.
     */
    @GET(ServerContract.ARTICLES)
    fun getLatestArticles() : Call<List<Article>>

    /**
     * Will retrieve a chunk of articles starting from the [lastArticleId] and older.
     */
    @GET(ServerContract.ARTICLES)
    fun getMoreArticles(@Query(ServerContract.QUERY_PARAM_LAST_ARTICLE_ID) lastArticleId : Int) : Call<List<Article>>

    /**
     * Will retrieve the most recent comments for an article.
     */
    @GET(ServerContract.COMMENTS)
    fun getComments(@Query(ServerContract.QUERY_PARAM_ARTICLE_ID) articleId: Int) : Call<List<Comment>>

    /**
     * Will retrieve a chunk if comments starting from the [lastCommentId] and older.
     */
    @GET(ServerContract.COMMENTS)
    fun getMoreComments(@Query(ServerContract.QUERY_PARAM_ARTICLE_ID) articleId: Int, @Query(ServerContract.QUERY_PARAM_LAST_COMMENT_ID) lastCommentId: Int) : Call<List<Comment>>

    /**
     * Will retrieve the most recent news.
     */
    @GET(ServerContract.NEWS)
    fun getLatestNews() : Call<List<PieceOfNews>>

    /**
     * Posts the provided comment to the server.
     */
    @POST(ServerContract.POST_COMMENT)
    fun postComment(@Body comment: Comment) : Call<ResponseBody>

    /**
     * Posts the provided article to the server.
     */
    @POST(ServerContract.POST_ARTICLE)
    fun postArticle(@Body article: Article) : Call<ResponseBody>

    /**
     * Posts the provided event/PieceOfNews to the server.
     */
    @POST(ServerContract.POST_EVENT)
    fun postEvent(@Body pieceOfNews: PieceOfNews) : Call<ResponseBody>
}