package cz.hlinkapp.gvpintranet.fragments.detail

import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import cz.hlinkapp.gvpintranet.R
import cz.hlinkapp.gvpintranet.activities.ArticleDetailActivity.Companion.KEY_ARTICLE_ID
import cz.hlinkapp.gvpintranet.adapters.ArticleNodesRecyclerAdapter
import cz.hlinkapp.gvpintranet.adapters.CommentsRecyclerAdapter
import cz.hlinkapp.gvpintranet.config.setGone
import cz.hlinkapp.gvpintranet.config.setLayoutManagerSafely
import cz.hlinkapp.gvpintranet.config.setVisible
import cz.hlinkapp.gvpintranet.data.utils.event_handling.RequestInfo
import cz.hlinkapp.gvpintranet.di.MyApplication
import cz.hlinkapp.gvpintranet.fragments.abstraction.BaseFragment
import cz.hlinkapp.gvpintranet.model.Comment
import cz.hlinkapp.gvpintranet.utils.ArticleNodesParser
import cz.hlinkapp.gvpintranet.utils.SharedPrefUtil
import cz.hlinkapp.gvpintranet.view_models.ArticleDetailViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_article_detail.*
import javax.inject.Inject

/**
 * Displays a detail of an article and its comments.
 * Provide the article ID using the [KEY_ARTICLE_ID] activity extras key.
 * Only loads the article from the DB, not from the server (this does not apply for the comments).
 */
class ArticleDetailFragment : BaseFragment() {

    override val layoutId: Int
        get() = R.layout.fragment_article_detail

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel : ArticleDetailViewModel
    private var mArticleId : Int = -1
    private val mCommentsAdapter = CommentsRecyclerAdapter()
    private lateinit var mItemDecoration : DividerItemDecoration
    @Inject
    lateinit var mSharedPrefUtil : SharedPrefUtil

    private val mDescriptionAdapter = ArticleNodesRecyclerAdapter().apply { textSize = 17f }
    private val mContentAdapter = ArticleNodesRecyclerAdapter().apply { textSize = 16f }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val fromSavedInstanceState = savedInstanceState?.getInt(KEY_ARTICLE_ID,-1) ?: -1
        val fromExtras = activity?.intent?.getIntExtra(KEY_ARTICLE_ID,-1) ?: -1
        mArticleId = if (fromExtras != -1) fromExtras else if (fromSavedInstanceState != 1) fromSavedInstanceState else throw IllegalArgumentException("No article id provided with KEY_ARTICLE_ID!")

        (activity?.applicationContext as? MyApplication)?.getApplicationComponent()?.inject(this)
        mItemDecoration = DividerItemDecoration(context,DividerItemDecoration.VERTICAL)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ArticleDetailViewModel::class.java)
        viewModel.init(mArticleId)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(KEY_ARTICLE_ID,mArticleId)
        super.onSaveInstanceState(outState)
    }

    override fun initViews() {
        super.initViews()
        (activity as? AppCompatActivity)?.supportActionBar?.title = getString(R.string.article_detail)
        fab?.setImageResource(R.drawable.ic_add_comment)

        title.movementMethod = LinkMovementMethod.getInstance()
        commentsRecyclerView.setLayoutManagerSafely(LinearLayoutManager(context))
        commentsRecyclerView.adapter = mCommentsAdapter
        commentsRecyclerView.removeItemDecoration(mItemDecoration)
        commentsRecyclerView.addItemDecoration(mItemDecoration)

        description.setLayoutManagerSafely(LinearLayoutManager(context))
        description.adapter = mDescriptionAdapter
        content.setLayoutManagerSafely(LinearLayoutManager(context))
        content.adapter = mContentAdapter

        viewModel.article.observe(viewLifecycleOwner, Observer {
            title.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) Html.fromHtml(it.title,
                Html.FROM_HTML_MODE_LEGACY) else Html.fromHtml(it.title)
            mDescriptionAdapter.nodes = ArticleNodesParser().parseText(it.description)
            mContentAdapter.nodes = ArticleNodesParser().parseText(it.content)
            author.text = it.author
            date.text = it.date
        })

        viewModel.commentsStatus.observe(viewLifecycleOwner, Observer { requestInfo ->
            if(requestInfo.isProcessing()) {
                commentsProgressBar.setVisible()
                resultIcon.setGone()
            } else {
                commentsProgressBar.setGone()
                requestInfo.requestResult.getContentIfNotHandled()?.let {
                    when (it) {
                        RequestInfo.RequestResult.OK, RequestInfo.RequestResult.NONE-> resultIcon.setGone()
                        RequestInfo.RequestResult.NO_INTERNET -> with(resultIcon) {
                            setVisible()
                            setImageResource(R.drawable.ic_no_internet)
                            setOnClickListener {Toast.makeText(context,getString(R.string.comments_not_updated_no_internet),Toast.LENGTH_LONG).show()}
                        }
                        RequestInfo.RequestResult.FAILED -> with(resultIcon) {
                            setVisible()
                            setImageResource(R.drawable.ic_error)
                            setOnClickListener { Toast.makeText(context,getString(R.string.comments_not_updated_error),Toast.LENGTH_LONG).show()}
                        }
                    }
                }
            }
        })

        viewModel.comments.observe(viewLifecycleOwner, Observer {
            mCommentsAdapter.submitList(it)
            if (it.isEmpty()) noCommentsLayout.setVisible() else noCommentsLayout.setGone()
        })

        commentAddButton.setOnClickListener {
            if (comment.text.toString().isBlank()) {
                comment.error = getString(R.string.fill_in_this_field)
                return@setOnClickListener
            }
            val name = mSharedPrefUtil.getStringSharedPref(getString(R.string.pref_key_name))
            val email = mSharedPrefUtil.getStringSharedPref(getString(R.string.pref_key_email))
            val relation = mSharedPrefUtil.getStringSharedPref(getString(R.string.pref_key_relation))
            if (name == null || email == null || relation == null) showSetUserDataDialog() else {
                val comm = Comment()
                comm.author = "$name ($relation)"
                comm.articleId = mArticleId
                comm.email = email
                comm.text = comment.text.toString()
                viewModel.postComment(comm)
            }
        }

        viewModel.postCommentStatus.observe(viewLifecycleOwner, Observer { requestInfo ->
            if (requestInfo.isProcessing()) {
                commentAddButton.setGone()
                commentAddProgressBar.setVisible()
            } else {
                commentAddProgressBar.setGone()
                commentAddButton.setVisible()
                requestInfo.requestResult.getContentIfNotHandled()?.let {
                    when (it) {
                        RequestInfo.RequestResult.OK ->  {
                            showInfoDialog(getString(R.string.finished),getString(R.string.comment_posted_waiting_for_approval))
                            comment.text?.clear()
                        }
                        RequestInfo.RequestResult.FAILED -> showErrorDialog(getString(R.string.error),getString(R.string.failed_posting_comment_error))
                        RequestInfo.RequestResult.NO_INTERNET -> showErrorDialog(getString(R.string.no_connection),getString(R.string.failed_posting_comment_no_internet))
                        RequestInfo.RequestResult.NONE -> {}
                    }
                }
            }
        })

        //Set correct tint of the comment add button
        comment.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            commentAddButton.imageTintList = if (hasFocus) ColorStateList.valueOf(ContextCompat.getColor(context!!,R.color.colorAccent))
            else ColorStateList.valueOf(ContextCompat.getColor(context!!,R.color.colorDivider))
        }
    }

    companion object {
        const val TAG = "ArticleDetailFragment"
    }
}