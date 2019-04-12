package cz.hlinkapp.gvpintranet.data.paging

import androidx.paging.PagedList
import cz.hlinkapp.gvpintranet.data.data_sources.server.ServerDataSource
import cz.hlinkapp.gvpintranet.model.Comment

/**
 * A [PagedList.BoundaryCallback] implementation for paging of comments.
 */
class CommentsBoundaryCallback constructor(
    private var articleId : Int,
    serverDataSource: ServerDataSource
) : PagedList.BoundaryCallback<Comment>() {
    private val mServerDataSource = serverDataSource

    override fun onZeroItemsLoaded() {
        mServerDataSource.loadComments(articleId)
    }

    override fun onItemAtEndLoaded(itemAtEnd: Comment) {
        mServerDataSource.loadMoreComments(articleId,itemAtEnd.id!!)
    }
}