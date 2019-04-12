package cz.hlinkapp.gvpintranet.fragments.abstraction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import cz.hlinkapp.gvpintranet.dialogs.ActionCompletedDialog
import cz.hlinkapp.gvpintranet.dialogs.ErrorMessageDialog
import cz.hlinkapp.gvpintranet.dialogs.InfoDialog
import cz.hlinkapp.gvpintranet.dialogs.SetUserDataDialog

/**
 * A base Fragment class containing several utilities.
 * Almost all of the app's fragments should implement this class.
 */
abstract class BaseFragment : androidx.fragment.app.Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(layoutId,container,false)
    }

    override fun onStart() {
        super.onStart()
        initViews()
    }

    /**
     * The child fragments should initialize their views in this method. This will get called automatically in [onStart] of the Fragment.
     */
    protected open fun initViews() {}

    @get:LayoutRes
    protected abstract val layoutId : Int

    /**
     * Shows an error message dialog.
     * @param titleText The error title to display.
     * @param descriptionText The error description to display.
     */
    protected fun showErrorDialog(titleText: String, descriptionText : String) {
        val bundle = Bundle()
        bundle.putString(ErrorMessageDialog.ARG_TITLE_TEXT, titleText)
        bundle.putString(ErrorMessageDialog.ARG_DESCRIPTION_TEXT,descriptionText)
        val dialog = ErrorMessageDialog()
        dialog.arguments = bundle
        dialog.show(activity!!.supportFragmentManager, ErrorMessageDialog.TAG)
    }

    /**
     * Shows an error message dialog with an exception text.
     * @param titleText The error title to display.
     * @param descriptionText The error description to display.
     * @param exceptionText The name of the exception that happened.
     */
    protected fun showErrorDialog(titleText: String, descriptionText : String, exceptionText : String?) {
        val bundle = Bundle()
        bundle.putString(ErrorMessageDialog.ARG_TITLE_TEXT, titleText)
        bundle.putString(ErrorMessageDialog.ARG_DESCRIPTION_TEXT,descriptionText)
        bundle.putString(ErrorMessageDialog.ARG_EXCEPTION_TEXT,exceptionText)
        val dialog = ErrorMessageDialog()
        dialog.arguments = bundle
        dialog.show(activity!!.supportFragmentManager, ErrorMessageDialog.TAG)
    }

    /**
     * Returns true if the error dialog is currently shown, false otherwise.
     */
    protected fun isErrorDialogShown() : Boolean {
        activity?.supportFragmentManager?.executePendingTransactions()
        val dialogFragment = activity?.supportFragmentManager?.findFragmentByTag(ErrorMessageDialog.TAG) as androidx.fragment.app.DialogFragment?
        return dialogFragment?.isVisible ?: false
    }

    /**
     * Shows an error message dialog with default message texts.
     */
    protected fun showErrorDialog() {
        val dialog = ErrorMessageDialog()
        dialog.show(activity!!.supportFragmentManager, ErrorMessageDialog.TAG)
    }

    /**
     * Shows an info message dialog.
     * @param titleText The info title to display.
     * @param descriptionText The info description to display.
     */
    protected fun showInfoDialog(titleText: String, descriptionText : String) {
        val bundle = Bundle()
        bundle.putString(InfoDialog.ARG_TITLE_TEXT, titleText)
        bundle.putString(InfoDialog.ARG_DESCRIPTION_TEXT,descriptionText)
        val dialog = InfoDialog()
        dialog.arguments = bundle
        dialog.show(activity!!.supportFragmentManager, InfoDialog.TAG)
    }

    /**
     * Shows an action-completed message dialog.
     * @param titleText The title to display.
     * @param descriptionText The description to display.
     */
    protected fun showActionCompletedDialog(titleText: String, descriptionText : String) {
        val bundle = Bundle()
        bundle.putString(InfoDialog.ARG_TITLE_TEXT, titleText)
        bundle.putString(InfoDialog.ARG_DESCRIPTION_TEXT,descriptionText)
        val dialog = ActionCompletedDialog()
        dialog.arguments = bundle
        dialog.show(activity!!.supportFragmentManager, ActionCompletedDialog.TAG)
    }


    /**
     * Shows a dialog indicating to the user that they should fill in the user data in the settings before the requested action could be completed.
     * Shows a button in the dialog that will take them to the settings.
     */
    protected fun showSetUserDataDialog() {
        val dialog = SetUserDataDialog()
        dialog.show(activity!!.supportFragmentManager,SetUserDataDialog.TAG)
    }

}