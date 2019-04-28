package cz.hlinkapp.gvpintranet.fragments.content_add_fragments

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import cz.hlinkapp.gvpintranet.R
import cz.hlinkapp.gvpintranet.config.setGone
import cz.hlinkapp.gvpintranet.config.setVisible
import cz.hlinkapp.gvpintranet.data.utils.event_handling.RequestInfo
import cz.hlinkapp.gvpintranet.di.MyApplication
import cz.hlinkapp.gvpintranet.fragments.abstraction.BaseFragment
import cz.hlinkapp.gvpintranet.model.Article
import cz.hlinkapp.gvpintranet.utils.SharedPrefUtil
import cz.hlinkapp.gvpintranet.view_models.ContentAddViewModel
import kotlinx.android.synthetic.main.fragment_add_article.*
import javax.inject.Inject

/**
 * A Fragment for composing and posting an article.
 */
class AddArticleFragment : BaseFragment() {

    override val layoutId: Int
        get() = R.layout.fragment_add_article

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel : ContentAddViewModel
    @Inject
    lateinit var mSharedPrefUtil : SharedPrefUtil

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (activity?.applicationContext as? MyApplication)?.getApplicationComponent()?.inject(this)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ContentAddViewModel::class.java)
    }

    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)

        sendButton.setOnClickListener {if (checkInput()) sendData()}

        viewModel.postArticleStatus.observe(viewLifecycleOwner, Observer { requestInfo ->
            if (requestInfo.isProcessing()) {
                sendButton.setGone()
                progressBar.setVisible()
            } else {
                progressBar.setGone()
                sendButton.setVisible()
                requestInfo.requestResult.getContentIfNotHandled()?.let {
                    when (it) {
                        RequestInfo.RequestResult.OK -> {
                            showActionCompletedDialog(getString(R.string.finished),getString(R.string.article_posted_waiting_for_approval))
                            mSharedPrefUtil.clearArticleDraft()
                            clearFields()
                        }
                        RequestInfo.RequestResult.FAILED -> showErrorDialog(getString(R.string.error),getString(R.string.failed_posting_article_error))
                        RequestInfo.RequestResult.NO_INTERNET -> showErrorDialog(getString(R.string.no_connection),getString(R.string.failed_posting_article_no_internet))
                        RequestInfo.RequestResult.NONE -> {}
                    }
                }
            }
        })
    }

    /**
     * Clears all of the text fields.
     */
    private fun clearFields() {
        title.text?.clear()
        description.text?.clear()
        content.text?.clear()
        note.text?.clear()
    }

    /**
     * Checks the input and returns true if it is valid.
     * Also handles displaying error messages if it's not.
     */
    private fun checkInput() : Boolean{
        return when {
            title.text.toString().isBlank() -> {
                title.error = getString(R.string.fill_in_this_field)
                false
            }
            description.text.toString().isBlank() && content.text.toString().isBlank() -> {
                description.error = getString(R.string.fill_in_at_least_one_of_these_fields)
                content.error = getString(R.string.fill_in_at_least_one_of_these_fields)
                false
            }
            else -> true
        }
    }

    /**
     * Sends the data to the server or handles errors.
     */
    private fun sendData() {
        val name = mSharedPrefUtil.getStringSharedPref(getString(R.string.pref_key_name))
        val email = mSharedPrefUtil.getStringSharedPref(getString(R.string.pref_key_email))
        if (name != null && email != null) {
            val art = Article()
            art.title = title.text.toString()
            art.description = description.text.toString()
            art.content = content.text.toString()
            art.note = note.text.toString()
            art.author = name
            art.email = email
            viewModel.postArticle(art)
        } else showSetUserDataDialog()
    }

    override fun onResume() {
        super.onResume()
        //save work
        val art = mSharedPrefUtil.retrieveArticleDraft()
        title.setText(art.title)
        description.setText(art.description)
        content.setText(art.content)
        note.setText(art.note)
    }

    override fun onPause() {
        super.onPause()
        //retrieve previous work
        val art = Article()
        art.title = title.text.toString().trim()
        art.description = description.text.toString().trim()
        art.content = content.text.toString().trim()
        art.note = note.text.toString().trim()
        mSharedPrefUtil.saveArticleDraft(art)
    }

    companion object {
        const val TAG = "AddArticleFragment"
    }
}