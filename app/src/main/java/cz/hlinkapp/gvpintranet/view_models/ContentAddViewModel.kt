package cz.hlinkapp.gvpintranet.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import cz.hlinkapp.gvpintranet.data.repositories.ContentAddRepository
import cz.hlinkapp.gvpintranet.data.utils.event_handling.RequestInfo
import cz.hlinkapp.gvpintranet.model.Article
import cz.hlinkapp.gvpintranet.model.PieceOfNews
import javax.inject.Inject

/**
 * A ViewModel for screens that add content - articles and events/pieces-of-news.
 */
class ContentAddViewModel @Inject constructor(contentAddRepository: ContentAddRepository): ViewModel() {

    private val mRepository = contentAddRepository

    val postArticleStatus : LiveData<RequestInfo> get() = mRepository.postArticleStatus
    val postEventStatus : LiveData<RequestInfo> get() = mRepository.postEventStatus

    /**
     * Attempts to post the given article to the server.
     * You can observe the operation status via [postArticleStatus].
     */
    fun postArticle(article: Article) {
        mRepository.postArticle(article)
    }

    /**
     * Attempts to post the given event/piece-of-news to the server.
     * You can observe the operation status via [postEventStatus].
     */
    fun postEvent(pieceOfNews: PieceOfNews) {
        mRepository.postEvent(pieceOfNews)
    }
}