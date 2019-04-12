package cz.hlinkapp.gvpintranet.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import cz.hlinkapp.gvpintranet.fragments.main_fragments.ArticlesFragment
import cz.hlinkapp.gvpintranet.fragments.main_fragments.NewsFragment

/**
 * A [androidx.viewpager.widget.ViewPager] adapter for the main screen. Contains two fragments: [ArticlesFragment] and [NewsFragment].
 */
class MainViewPagerAdapter (fm: FragmentManager, private val titles: List<String>): FragmentStatePagerAdapter(fm) {

    init {
        if (titles.size != count) throw IllegalArgumentException("Provide correct amount of titles.")
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles[position]
    }

    override fun getItem(position: Int): Fragment = when (position) {
        0 -> ArticlesFragment()
        1 -> NewsFragment()
        else -> throw IllegalArgumentException()
    }

    override fun getCount(): Int = 2
}