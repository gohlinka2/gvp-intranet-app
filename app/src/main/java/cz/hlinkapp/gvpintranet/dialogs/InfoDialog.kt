package cz.hlinkapp.gvpintranet.dialogs

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import cz.hlinkapp.gvpintranet.R
import cz.hlinkapp.gvpintranet.dialogs.ErrorMessageDialog.Companion.ARG_DESCRIPTION_TEXT
import cz.hlinkapp.gvpintranet.dialogs.ErrorMessageDialog.Companion.ARG_TITLE_TEXT
import cz.hlinkapp.gvpintranet.dialogs.InfoDialog.Companion.ARG_ACTION_BUTTON_TEXT
import cz.hlinkapp.gvpintranet.dialogs.InfoDialog.Companion.ARG_CLOSE_BUTTON_TEXT
import cz.hlinkapp.gvpintranet.dialogs.InfoDialog.Companion.ARG_DESCRIPTION_TEXT
import cz.hlinkapp.gvpintranet.dialogs.InfoDialog.Companion.ARG_TITLE_TEXT
import kotlinx.android.synthetic.main.dialog_info.view.*

/**
 * A dialog used to display an informative message, with a close button and an optional action button.
 * To show the action button, both an onClickListener and the action-button-text have to be set.
 * To set onClickListeners, a subclass must be created. (the listeners must be recreated on every activity rotation, so the child fragment should create them
 * in the [DialogFragment.onCreateDialog] method before calling super.)
 * The dialog displays a title text and a description text.
 * Use the [ARG_ACTION_BUTTON_TEXT] and [ARG_CLOSE_BUTTON_TEXT] to pass button texts.
 * Use the [ARG_TITLE_TEXT] and [ARG_DESCRIPTION_TEXT] Bundle keys to pass title-string and description-string using arguments.
 */
open class InfoDialog : DialogFragment() {

    private lateinit var v: View
    private var mCancelable : Boolean = true
    protected var closeButtonOnClickListener : (() -> Unit) = { dismiss() }
    protected var actionButtonOnClickListener : (() -> Unit)? = null
    protected var onDismissListener : (() -> Unit)? = null

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context!!)
        v = activity!!.layoutInflater.inflate(R.layout.dialog_info, null)
        val titleText = arguments?.getString(ARG_TITLE_TEXT, null)
        val descriptionText = arguments?.getString(ARG_DESCRIPTION_TEXT, null)
        val closeButtonText = arguments?.getString(ARG_CLOSE_BUTTON_TEXT,null)
        val actionButtonText = arguments?.getString(ARG_ACTION_BUTTON_TEXT,null)

        if (titleText == null) throw IllegalArgumentException("The title must be set using ARG_TITLE_TEXT.") else v.title.text = titleText
        if (descriptionText == null) IllegalArgumentException("The description must be set using ARG_DESCRIPTION_TEXT") else v.description.text = descriptionText
        if (actionButtonText == null || actionButtonOnClickListener == null) v.actionButton.visibility = View.GONE
        else {
            v.actionButton.text = actionButtonText
            v.actionButton.visibility = View.VISIBLE
            v.actionButton.setOnClickListener {
                dismiss()
                actionButtonOnClickListener?.invoke()
            }
        }
        if (closeButtonText == null) v.closeButton.text = getString(R.string.close) else v.closeButton.text = closeButtonText

        v.closeButton.setOnClickListener {
            dismiss()
            closeButtonOnClickListener.invoke()
        }
        builder.setView(v)
        isCancelable = mCancelable
        return builder.create().apply {
            setCanceledOnTouchOutside(mCancelable)
            setIsCancelable(mCancelable)
            window?.setBackgroundDrawableResource(R.drawable.bg_dialog)
        }
    }

    /**
     * If false, sets the dialog to not cancelable.
     * That means that the user will have no other way of dismissing the dialog than interacting with it. (no dismiss on back button press or click outside the dialog)
     * This has to be called before showing the dialog to take effect.
     */
    fun setIsCancelable(cancelable : Boolean) {
        mCancelable = cancelable
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        onDismissListener?.invoke()
    }

    companion object {
        const val TAG = "ActionOrCancelDialog"
        const val ARG_TITLE_TEXT = "titleText"
        const val ARG_DESCRIPTION_TEXT = "descriptionText"
        const val ARG_CLOSE_BUTTON_TEXT = "cancelButtonText"
        const val ARG_ACTION_BUTTON_TEXT = "actionButtonText"
    }
}