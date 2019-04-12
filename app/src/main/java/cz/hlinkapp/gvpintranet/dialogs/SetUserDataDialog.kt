package cz.hlinkapp.gvpintranet.dialogs

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import cz.hlinkapp.gvpintranet.R
import cz.hlinkapp.gvpintranet.activities.SettingsActivity

/**
 * A dialog indicating to the user that they should fill in the user data in the settings before the requested action could be completed.
 * Shows a button in the dialog that will take them to the settings.
 */
class SetUserDataDialog : InfoDialog() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        actionButtonOnClickListener = {startActivity(Intent(context, SettingsActivity::class.java))}
        val bundle = Bundle()
        bundle.putString(InfoDialog.ARG_TITLE_TEXT,getString(R.string.set_user_data_first))
        bundle.putString(InfoDialog.ARG_DESCRIPTION_TEXT,getString(R.string.set_data_for_posting_to_post))
        bundle.putString(InfoDialog.ARG_ACTION_BUTTON_TEXT,getString(R.string.open_settings))
        arguments = bundle
        return super.onCreateDialog(savedInstanceState)
    }

    companion object {
        const val TAG = "SetUserDataDialog"
    }
}