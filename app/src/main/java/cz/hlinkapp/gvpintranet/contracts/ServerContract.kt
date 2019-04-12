package cz.hlinkapp.gvpintranet.contracts

/**
 * An interface containing a set of constants that define how the app should communicate with the server.
 */
interface ServerContract {
    companion object {

        const val PROTOCOL = "http://"
        const val BASE_URL = "${PROTOCOL}www.gvp.cz"
        const val MAIN_API_URL = "$BASE_URL/~hlinka/intranet_api/"

        const val QUERY_PARAM_LAST_ARTICLE_ID = "lastArticleId"
        const val ARTICLES = "articles.php"
        const val NEWS = "news.php"
        const val COMMENTS = "comments.php"
        const val QUERY_PARAM_LAST_COMMENT_ID = "lastCommentId"
        const val QUERY_PARAM_ARTICLE_ID = "articleId"

        const val POST_COMMENT = "postComment.php"
        const val POST_ARTICLE = "postArticle.php"
        const val POST_EVENT = "postEvent.php"
    }
}