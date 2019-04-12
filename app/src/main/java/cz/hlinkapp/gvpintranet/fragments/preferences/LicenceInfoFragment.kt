package cz.hlinkapp.gvpintranet.fragments.preferences

import androidx.appcompat.app.AppCompatActivity
import cz.hlinkapp.gvpintranet.R
import cz.hlinkapp.gvpintranet.fragments.abstraction.BaseFragment

/**
 * A simple Fragment for displaying the licence info.
 */
class LicenceInfoFragment : BaseFragment() {
    override val layoutId: Int
        get() = R.layout.fragment_licence_info

    override fun onStart() {
        super.onStart()
        (activity as AppCompatActivity)?.supportActionBar?.title = getString(R.string.license_info)
    }

    companion object {
        const val TAG = "LicenceInfoFragment"
    }
}