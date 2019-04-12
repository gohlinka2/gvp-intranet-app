package cz.hlinkapp.gvpintranet.data.data_sources.server

import androidx.lifecycle.LiveData
import cz.hlinkapp.gvpintranet.data.data_sources.server.modules.ArticleDetailServerDataSourceModule
import cz.hlinkapp.gvpintranet.data.data_sources.server.modules.ContentAddServerDataSourceModule
import cz.hlinkapp.gvpintranet.data.data_sources.server.modules.MainServerDataSourceModule
import cz.hlinkapp.gvpintranet.data.utils.event_handling.RequestInfo
import cz.hlinkapp.gvpintranet.model.Article
import cz.hlinkapp.gvpintranet.model.Comment
import cz.hlinkapp.gvpintranet.model.PieceOfNews
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Main class for communicating with the server. This is the class that makes network calls and handles the persistence of data to the database.
 * The class's work is divided into individual modules, which are injected in the constructor.
 */
@Singleton
class ServerDataSource @Inject constructor(
    mainServerDataSourceModule: MainServerDataSourceModule,
    articleDetailServerDataSourceModule: ArticleDetailServerDataSourceModule,
    contentAddServerDataSourceModule: ContentAddServerDataSourceModule
    ){

    private val mContentAddServerDataSourceModule = contentAddServerDataSourceModule
    private val mMainServerDataSourceModule = mainServerDataSourceModule
    private val mArticleDetailServerDataSourceModule = articleDetailServerDataSourceModule
    
    val commentsStatus : LiveData<RequestInfo> get() = mArticleDetailServerDataSourceModule.commentsStatus
    val loadMoreCommentsStatus : LiveData<RequestInfo> get() = mArticleDetailServerDataSourceModule.loadMoreCommentsStatus
    val postCommentStatus : LiveData<RequestInfo> get() = mArticleDetailServerDataSourceModule.postCommentStatus

    val articleStatus: LiveData<RequestInfo> get() = mMainServerDataSourceModule.articleStatus
    val loadMoreArticlesStatus : LiveData<RequestInfo> get() = mMainServerDataSourceModule.loadMoreArticlesStatus

    val newsStatus: LiveData<RequestInfo> get() = mMainServerDataSourceModule.newsStatus
    val postArticleStatus get() = mContentAddServerDataSourceModule.postArticleStatus
    val postEventStatus get() = mContentAddServerDataSourceModule.postEventStatus

    /**
     * Downloads the most recent articles from the server.
     * @param forceRefresh If false, the data will be refreshed only if it's old, true to force refresh it.
     */
    fun refreshArticleData(forceRefresh : Boolean) = mMainServerDataSourceModule.refreshArticleData(forceRefresh)

    /**
     * Loads another chunk of articles, starting from the ID provided. Use for paging data.
     */
    fun loadMoreArticles(lastArticleId: Int) = mMainServerDataSourceModule.loadMoreArticles(lastArticleId)

    /**
     * Downloads the most recent news from the server.
     * @param forceRefresh If false, the data will be refreshed only if it's old, true to force refresh it.
     */
    fun refreshNewsData(forceRefresh: Boolean) = mMainServerDataSourceModule.refreshNewsData(forceRefresh)

    /**
     * Downloads the most recent comments for the given [articleId].
     */
    fun loadComments(articleId: Int) = mArticleDetailServerDataSourceModule.loadComments(articleId)

    /**
     * Loads another chunk of comments for the given [articleId], starting from the [lastCommentId] provided. Use for paging data.
     */
    fun loadMoreComments(articleId : Int, lastCommentId: Int) = mArticleDetailServerDataSourceModule.loadMoreComments(articleId,lastCommentId)

    /**
     * Posts the given [comment] to the server.
     * The [comment] object must have the following fields set: [Comment.articleId], [Comment.text], [Comment.email], [Comment.author];
     */
    fun postComment(comment: Comment) = mArticleDetailServerDataSourceModule.postComment(comment)

    /**
     * Posts the given [article] to the server.
     * The [article] object must have the following fields set: [Article.title], [Article.author], [Article.email] and at least one of [Article.description] or [Article.content].
     */
    fun postArticle(article: Article) = mContentAddServerDataSourceModule.postArticle(article)

    /**
     * Posts the given event/[pieceOfNews] to the server.
     * The [pieceOfNews] object must have the following fields set: [PieceOfNews.title], [PieceOfNews.description].
     */
    fun postEvent(pieceOfNews: PieceOfNews) = mContentAddServerDataSourceModule.postEvent(pieceOfNews)

}