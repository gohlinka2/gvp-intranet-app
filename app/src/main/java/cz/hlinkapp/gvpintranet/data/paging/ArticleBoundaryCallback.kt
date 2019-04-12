package cz.hlinkapp.gvpintranet.data.paging

import androidx.paging.PagedList
import cz.hlinkapp.gvpintranet.data.data_sources.server.ServerDataSource
import cz.hlinkapp.gvpintranet.model.Article
import javax.inject.Inject

/**
 * A [PagedList.BoundaryCallback] implementation for paging of articles.
 */
class ArticleBoundaryCallback @Inject constructor(
    serverDataSource: ServerDataSource
) : PagedList.BoundaryCallback<Article>() {
    private val mServerDataSource = serverDataSource

    override fun onZeroItemsLoaded() {
        mServerDataSource.refreshArticleData(true)
    }

    override fun onItemAtEndLoaded(itemAtEnd: Article) {
        mServerDataSource.loadMoreArticles(itemAtEnd.id!!)
    }
}