package cz.hlinkapp.gvpintranet.fragments.main_fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cz.hlinkapp.gvpintranet.R
import cz.hlinkapp.gvpintranet.activities.ArticleDetailActivity
import cz.hlinkapp.gvpintranet.activities.MainActivity.Companion.STATUS_LAYOUT_HIDE_AFTER_TIME
import cz.hlinkapp.gvpintranet.adapters.ArticlesRecyclerAdapter
import cz.hlinkapp.gvpintranet.config.setLayoutManagerSafely
import cz.hlinkapp.gvpintranet.data.utils.event_handling.RequestInfo
import cz.hlinkapp.gvpintranet.di.MyApplication
import cz.hlinkapp.gvpintranet.fragments.abstraction.BaseFragment
import cz.hlinkapp.gvpintranet.utils.OnChildScrollListener
import cz.hlinkapp.gvpintranet.view_models.MainViewModel
import kotlinx.android.synthetic.main.fragment_articles.*
import javax.inject.Inject

/**
 * A fragment for displaying a paged list of articles, with the possibility to click the articles to open their detail.
 */
class ArticlesFragment : BaseFragment() {
    override val layoutId: Int
        get() = R.layout.fragment_articles

    private lateinit var viewModel : MainViewModel

    private val mPagedAdapter = ArticlesRecyclerAdapter {
        val intent = Intent(context, ArticleDetailActivity::class.java)
        intent.putExtra(ArticleDetailActivity.KEY_ARTICLE_ID,it.id)
        startActivity(intent)
    }

    private val handler = Handler()
    private val hideRunnable = {
        statusLayout?.visibility = GONE
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun initDependencies(savedInstanceState: Bundle?) {
        super.initDependencies(savedInstanceState)
        (activity?.applicationContext as? MyApplication)?.getApplicationComponent()?.inject(this)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
        viewModel.initArticles()
    }

    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        dataNotSavedLayout.visibility = GONE
        commentsProgressBar.visibility = GONE
        recyclerView.setLayoutManagerSafely(LinearLayoutManager(context))
        recyclerView.adapter = mPagedAdapter
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                when {
                    dy > 1 -> (activity as? OnChildScrollListener)?.onScrollDown()
                    dy < 1 -> (activity as? OnChildScrollListener)?.onScrollUp()
                }
            }
        })

        viewModel.pagedArticles?.observe(viewLifecycleOwner, Observer {
            mPagedAdapter.submitList(it) { recyclerView.scrollToPosition(0) }
            dataNotSavedLayout.visibility = if (it.isEmpty()) VISIBLE else GONE
        })

        viewModel.articleStatus.observe (viewLifecycleOwner, Observer { requestInfo ->
            if (requestInfo.isProcessing()) showStatusLayout(getString(R.string.downloading_data),true)
            else requestInfo.requestResult.getContentIfNotHandled().let {
                when (it) {
                    RequestInfo.RequestResult.OK -> {
                        showStatusLayout(getString(R.string.finished),false)
                        recyclerView.smoothScrollToPosition(0)
                    }
                    RequestInfo.RequestResult.FAILED -> showStatusLayout(getString(R.string.error_try_later),false)
                    RequestInfo.RequestResult.NO_INTERNET -> showStatusLayout(getString(R.string.no_connection),false)
                    else -> hideStatusLayoutImmediately()
                }
            }
        })

        swipeRefreshLayout.setOnRefreshListener {
            viewModel.forceRefreshArticles()
            swipeRefreshLayout.isRefreshing = false
        }
    }

    /**
     * Shows the articleStatus layout.
     * @param message The articleStatus message to display
     * @param inProgress True if the ProgressBar should be shown, false otherwise. If set to false, the status layout
     * will hide itself after a delay.
     */
    private fun showStatusLayout(message: String, inProgress: Boolean) {
        handler.removeCallbacks(hideRunnable)
        statusLayout.visibility = VISIBLE
        statusProgressBar.visibility = if (inProgress) VISIBLE else GONE
        statusText.text = message
        if (!inProgress) hideStatusLayoutAfterDelay()
    }

    /**
     * Hides the articleStatus layout.
     */
    private fun hideStatusLayoutImmediately() {
        statusLayout.visibility = GONE
    }

    /**
     * Hides the articleStatus layout after a delay of millis defined in [STATUS_LAYOUT_HIDE_AFTER_TIME].
     * Use when an action has completed and the result has to be shown to the user.
     */
    private fun hideStatusLayoutAfterDelay() {
        handler.removeCallbacks(hideRunnable)
        handler.postDelayed(hideRunnable, STATUS_LAYOUT_HIDE_AFTER_TIME)
    }

    companion object {
        const val TAG = "ArticlesFragment"
    }
}