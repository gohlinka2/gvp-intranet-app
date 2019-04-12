package cz.hlinkapp.gvpintranet.data.repositories

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import cz.hlinkapp.gvpintranet.data.data_sources.room.MainDao
import cz.hlinkapp.gvpintranet.data.data_sources.server.ServerDataSource
import cz.hlinkapp.gvpintranet.data.paging.CommentsBoundaryCallback
import cz.hlinkapp.gvpintranet.model.Article
import cz.hlinkapp.gvpintranet.model.Comment
import javax.inject.Inject
import javax.inject.Singleton

/**
 * A repository for handling data used in the article detail screen, such as comments or the article itself.
 * This is the main class that decides from which data source the data will be loaded and handles the refreshing.
 */
@Singleton
class ArticleDetailRepository @Inject constructor(
    dao: MainDao,
    serverDataSource: ServerDataSource
) {
    private val mMainDao = dao
    private val mServerDataSource = serverDataSource

    val commentsStatus get() = mServerDataSource.commentsStatus
    val postCommentStatus get() = mServerDataSource.postCommentStatus

    /**
     * Retrieves a LiveData of paged comments from the database for the given [articleId] and initiates their refreshing.
     * You can observe the state of the refreshing with [commentsStatus].
     */
    fun getPagedComments(articleId: Int) : LiveData<PagedList<Comment>> {
        mServerDataSource.loadComments(articleId) //refresh comments
        val dataSourceFactory = mMainDao.getPagedComments(articleId)
        return LivePagedListBuilder(dataSourceFactory, PAGE_SIZE)
            .setBoundaryCallback(CommentsBoundaryCallback(articleId,mServerDataSource))
            .build()
    }

    /**
     * Retrieves the article with the given [articleId] from the local db.
     */
    fun getArticle(articleId: Int) : LiveData<Article> {
        return mMainDao.getArticle(articleId)
    }

    /**
     * Attempts to post the given comment to the server.
     * You can observe the status of the upload with [postCommentStatus].
     */
    fun postComment(comment: Comment) = mServerDataSource.postComment(comment)

    companion object {
        private const val PAGE_SIZE = 15
    }

}