package cz.hlinkapp.gvpintranet.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import cz.hlinkapp.gvpintranet.R
import cz.hlinkapp.gvpintranet.adapters.MainViewPagerAdapter
import cz.hlinkapp.gvpintranet.contracts.ServerContract
import cz.hlinkapp.gvpintranet.di.MyApplication
import cz.hlinkapp.gvpintranet.utils.OnChildScrollListener
import kotlinx.android.synthetic.main.activity_main.*

/**
 * The main activity, containing the ViewPager with the two main fragments, [cz.hlinkapp.gvpintranet.fragments.main_fragments.ArticlesFragment] and [cz.hlinkapp.gvpintranet.fragments.main_fragments.NewsFragment].
 * To ensure correct behavior of the FAB, child Fragments should use the [OnChildScrollListener] interface to let the Activity know of nested scroll events.
 */
class MainActivity : AppCompatActivity(), OnChildScrollListener {

    override fun onScrollUp() {
        fab?.show()
    }

    override fun onScrollDown() {
        fab?.hide()
    }

    private val mAdapter = lazy {
        MainViewPagerAdapter(supportFragmentManager, listOf(getString(R.string.articles),getString(R.string.events)))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (applicationContext as? MyApplication)?.getApplicationComponent()?.inject(this)
        setSupportActionBar(toolbar)

        initViews()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when (item?.itemId) {
        R.id.settings -> {
            startActivity(Intent(this@MainActivity,SettingsActivity::class.java))
            true
        }
        R.id.open_in_browser -> {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(ServerContract.INTRANET_LINK)))
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun initViews() {
        viewPager.adapter = mAdapter.value
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.addOnTabSelectedListener(object: TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                super.onTabSelected(tab)
                fab?.show() //show the fab when the tab has just changed
            }
        })
        fab.setOnClickListener {
            when (viewPager.currentItem) {
                0 -> startActivity(Intent(this@MainActivity,AddArticleActivity::class.java))
                1 -> startActivity(Intent(this@MainActivity,AddEventActivity::class.java))
            }
        }
    }

    companion object {
        /** The delay after which the status layout of child fragments will disappear.*/
        const val STATUS_LAYOUT_HIDE_AFTER_TIME : Long = 2500
    }

}
