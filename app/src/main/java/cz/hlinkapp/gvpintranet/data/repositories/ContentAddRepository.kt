package cz.hlinkapp.gvpintranet.data.repositories

import cz.hlinkapp.gvpintranet.data.data_sources.server.ServerDataSource
import cz.hlinkapp.gvpintranet.model.Article
import cz.hlinkapp.gvpintranet.model.PieceOfNews
import javax.inject.Inject

/**
 * A repository for posting new articles or news/events to the server.
 */
class ContentAddRepository @Inject constructor(
    serverDataSource: ServerDataSource
){

    private val mServerDataSource = serverDataSource

    val postArticleStatus = mServerDataSource.postArticleStatus
    val postEventStatus = mServerDataSource.postEventStatus

    /**
     * Attempts to post the given article to the server.
     * You can observe the status with [postArticleStatus].
     */
    fun postArticle(article: Article) {
        mServerDataSource.postArticle(article)
    }

    /**
     * Attempts to post the given event/piece-of-news to the server.
     * You can observe the status with [postEventStatus].
     */
    fun postEvent(pieceOfNews: PieceOfNews) {
        mServerDataSource.postEvent(pieceOfNews)
    }
}