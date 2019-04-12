package cz.hlinkapp.gvpintranet.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.transaction
import cz.hlinkapp.gvpintranet.R
import cz.hlinkapp.gvpintranet.fragments.content_add_fragments.AddArticleFragment
import kotlinx.android.synthetic.main.activity_add_article.*

/**
 * An activity for composing an article. The activity will use only one fragment, [AddArticleFragment].
 */
class AddArticleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_article)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportFragmentManager.transaction {
            replace(R.id.addArticleActivityFragmentContainer, AddArticleFragment(), AddArticleFragment.TAG)
        }
    }
}
