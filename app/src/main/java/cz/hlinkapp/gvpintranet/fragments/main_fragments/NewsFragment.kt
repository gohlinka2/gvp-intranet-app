package cz.hlinkapp.gvpintranet.fragments.main_fragments

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import cz.hlinkapp.gvpintranet.R
import cz.hlinkapp.gvpintranet.activities.MainActivity.Companion.STATUS_LAYOUT_HIDE_AFTER_TIME
import cz.hlinkapp.gvpintranet.adapters.NewsRecyclerAdapter
import cz.hlinkapp.gvpintranet.config.setLayoutManagerSafely
import cz.hlinkapp.gvpintranet.data.utils.event_handling.RequestInfo
import cz.hlinkapp.gvpintranet.di.MyApplication
import cz.hlinkapp.gvpintranet.fragments.abstraction.BaseFragment
import cz.hlinkapp.gvpintranet.view_models.MainViewModel
import kotlinx.android.synthetic.main.fragment_news.*
import javax.inject.Inject

/**
 * A Fragment for displaying a list of news/events.
 */
class NewsFragment: BaseFragment() {
    override val layoutId: Int
        get() = R.layout.fragment_news

    private lateinit var viewModel : MainViewModel
    private val mAdapter = NewsRecyclerAdapter()

    private val handler = Handler()
    private val hideRunnable = {
        statusLayout?.visibility = View.GONE
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (activity?.applicationContext as? MyApplication)?.getApplicationComponent()?.inject(this)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
        viewModel.initNews()
    }

    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        dataNotSavedLayout.visibility = View.GONE
        commentsProgressBar.visibility = View.GONE
        recyclerView.setLayoutManagerSafely(LinearLayoutManager(context))
        recyclerView.adapter = mAdapter
        viewModel.news?.observe(this, Observer {
            mAdapter.news = ArrayList(it)
            dataNotSavedLayout.visibility = if (mAdapter.news.isEmpty()) View.VISIBLE else View.GONE
        })
        viewModel.newsStatus.observe (viewLifecycleOwner, Observer { requestInfo ->
            if (requestInfo.isProcessing()) showStatusLayout(getString(R.string.downloading_data),true)
            else requestInfo.requestResult.getContentIfNotHandled().let {
                when (it) {
                    RequestInfo.RequestResult.OK -> showStatusLayout(getString(R.string.finished),false)
                    RequestInfo.RequestResult.FAILED -> showStatusLayout(getString(R.string.error_try_later),false)
                    RequestInfo.RequestResult.NO_INTERNET -> showStatusLayout(getString(R.string.no_connection),false)
                    else -> hideStatusLayoutImmediately()
                }
            }
        })

        swipeRefreshLayout.setOnRefreshListener {
            viewModel.forceRefreshNews()
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
        statusLayout.visibility = View.VISIBLE
        statusProgressBar.visibility = if (inProgress) View.VISIBLE else View.GONE
        statusText.text = message
        if (!inProgress) hideStatusLayoutAfterDelay()
    }

    /**
     * Hides the articleStatus layout.
     */
    private fun hideStatusLayoutImmediately() {
        statusLayout.visibility = View.GONE
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
        const val TAG = "NewsFragment"
    }

}