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
import cz.hlinkapp.gvpintranet.model.PieceOfNews
import cz.hlinkapp.gvpintranet.utils.SharedPrefUtil
import cz.hlinkapp.gvpintranet.view_models.ContentAddViewModel
import kotlinx.android.synthetic.main.fragment_add_event.*
import javax.inject.Inject

/**
 * A Fragment for composing and posting an event/piece-of-news.
 */
class AddEventFragment : BaseFragment() {

    override val layoutId: Int
        get() = R.layout.fragment_add_event

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

    override fun initViews() {
        super.initViews()

        sendButton.setOnClickListener {if (checkInput()) sendData()}

        viewModel.postEventStatus.observe(viewLifecycleOwner, Observer { requestInfo ->
            if (requestInfo.isProcessing()) {
                sendButton.setGone()
                progressBar.setVisible()
            } else {
                progressBar.setGone()
                sendButton.setVisible()
                requestInfo.requestResult.getContentIfNotHandled()?.let {
                    when (it) {
                        RequestInfo.RequestResult.OK -> {
                            showActionCompletedDialog(getString(R.string.finished),getString(R.string.event_posted_waiting_for_approval))
                            mSharedPrefUtil.clearEventDraft()
                            clearFields()
                        }
                        RequestInfo.RequestResult.FAILED -> showErrorDialog(getString(R.string.error),getString(R.string.failed_posting_event_error))
                        RequestInfo.RequestResult.NO_INTERNET -> showErrorDialog(getString(R.string.no_connection),getString(R.string.failed_posting_event_no_internet))
                        RequestInfo.RequestResult.NONE -> {}
                    }
                }
            }
        })
    }

    /**
     * Clears all of the EditTexts.
     */
    private fun clearFields() {
        title.text?.clear()
        description.text?.clear()
    }
    override fun onPause() {
        super.onPause()
        //save work
        val ev = PieceOfNews()
        ev.title = title.text.toString().trim()
        ev.description = description.text.toString().trim()
        mSharedPrefUtil.saveEventDraft(ev)
    }

    override fun onResume() {
        super.onResume()
        //retrieve previous work
        val ev = mSharedPrefUtil.retrieveEventDraft()
        title.setText(ev.title)
        description.setText(ev.description)
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
            description.text.toString().isBlank()-> {
                description.error = getString(R.string.fill_in_this_field)
                false
            }
            else -> true
        }
    }

    /**
     * Send data to the server or handles the errors.
     */
    private fun sendData() {
        val name = mSharedPrefUtil.getStringSharedPref(getString(R.string.pref_key_name))
        val email = mSharedPrefUtil.getStringSharedPref(getString(R.string.pref_key_email))
        if (name != null && email != null) {
            val event = PieceOfNews()
            event.title = title.text.toString()
            event.description = description.text.toString()
            event.author = name
            event.email = email
            viewModel.postEvent(event)
        } else showSetUserDataDialog()
    }

    companion object {
        const val TAG = "AddEventFragment"
    }
}