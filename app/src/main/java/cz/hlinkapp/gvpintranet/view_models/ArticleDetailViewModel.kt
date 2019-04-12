package cz.hlinkapp.gvpintranet.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import cz.hlinkapp.gvpintranet.data.repositories.ArticleDetailRepository
import cz.hlinkapp.gvpintranet.data.utils.event_handling.RequestInfo
import cz.hlinkapp.gvpintranet.model.Article
import cz.hlinkapp.gvpintranet.model.Comment
import javax.inject.Inject

/**
 * A ViewModel for the article-detail screen.
 */
class ArticleDetailViewModel @Inject constructor(articleDetailRepository: ArticleDetailRepository): ViewModel() {

    private val mRepository = articleDetailRepository

    private lateinit var mArticle : LiveData<Article>
    private lateinit var mComments : LiveData<PagedList<Comment>>

    val article : LiveData<Article> get() = mArticle
    val comments : LiveData<PagedList<Comment>> get() = mComments
    val commentsStatus : LiveData<RequestInfo> get() = mRepository.commentsStatus
    val postCommentStatus : LiveData<RequestInfo> get() = mRepository.postCommentStatus

    /**
     * Initializes the view model's data.
     * Loads both the comments and the article.
     * The article is loaded only from the local db, while the comments are first loaded from the db and then an attempt to refresh them is made.
     * You can observe the status of comments refreshing via [commentsStatus].
     */
    fun init(articleId: Int) {
        mArticle = mRepository.getArticle(articleId)
        mComments = mRepository.getPagedComments(articleId)
    }

    /**
     * Posts the given comment to the server.
     * You can observe the operation status using [postCommentStatus].
     */
    fun postComment(comment: Comment) {
        mRepository.postComment(comment)
    }
}