package cz.hlinkapp.gvpintranet.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.transaction
import cz.hlinkapp.gvpintranet.R
import cz.hlinkapp.gvpintranet.activities.ArticleDetailActivity.Companion.KEY_ARTICLE_ID
import cz.hlinkapp.gvpintranet.fragments.detail.ArticleDetailFragment
import kotlinx.android.synthetic.main.activity_article_detail.*

/**
 * Displays a detail of an article.
 * Provide the article ID using the [KEY_ARTICLE_ID] extras key.
 * Only loads data from the DB, not from the server.
 * Uses only one fragment, [ArticleDetailFragment].
 */
class ArticleDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_detail)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportFragmentManager.transaction {
            replace(R.id.articleDetailActivityFragmentContainer,ArticleDetailFragment(),ArticleDetailFragment.TAG)
        }
        initViews()
    }

    private fun initViews() {
    }

    companion object {
        const val KEY_ARTICLE_ID = "articleId"
    }
}
