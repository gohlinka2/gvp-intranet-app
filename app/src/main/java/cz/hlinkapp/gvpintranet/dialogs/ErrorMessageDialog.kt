package cz.hlinkapp.gvpintranet.dialogs

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.view.View
import cz.hlinkapp.gvpintranet.R
import cz.hlinkapp.gvpintranet.dialogs.ErrorMessageDialog.Companion.ARG_DESCRIPTION_TEXT
import cz.hlinkapp.gvpintranet.dialogs.ErrorMessageDialog.Companion.ARG_EXCEPTION_TEXT
import cz.hlinkapp.gvpintranet.dialogs.ErrorMessageDialog.Companion.ARG_TITLE_TEXT
import kotlinx.android.synthetic.main.dialog_error_message.view.*

/**
 * A dialog used to display an error message.
 * The dialog displays an error title text and an error description text.
 * Use the [ARG_TITLE_TEXT] and [ARG_DESCRIPTION_TEXT] Bundle keys to pass title-string and description-string using arguments.
 * You can also pass a string with [ARG_EXCEPTION_TEXT] to display additional error info, ex. the thrown exception.
 */
class ErrorMessageDialog : androidx.fragment.app.DialogFragment() {

    private lateinit var v: View

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context!!)
        v = activity!!.layoutInflater.inflate(R.layout.dialog_error_message, null)
        val titleText = arguments?.getString(ARG_TITLE_TEXT, null)
        val descriptionText = arguments?.getString(ARG_DESCRIPTION_TEXT, null)
        val exceptionText = arguments?.getString(ARG_EXCEPTION_TEXT,null)
        //If no values were passed, use the default values in the xml
        if (titleText != null) v.title.text = titleText
        if (descriptionText != null) v.description.text = descriptionText
        v.exception.visibility = View.VISIBLE
        if(exceptionText != null) v.exception.text = exceptionText else v.exception.visibility = View.GONE
        v.button.setOnClickListener {dismiss()}
        builder.setView(v)
        return builder.create().apply {
            window?.setBackgroundDrawableResource(R.drawable.bg_dialog)
        }
    }

    companion object {
        const val TAG = "ErrorMessageDialog"
        const val ARG_TITLE_TEXT = "titletext"
        const val ARG_DESCRIPTION_TEXT = "descriptiontext"
        const val ARG_EXCEPTION_TEXT = "exceptiontext"
    }
}