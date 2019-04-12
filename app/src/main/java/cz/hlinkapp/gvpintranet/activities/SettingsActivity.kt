package cz.hlinkapp.gvpintranet.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.transaction
import cz.hlinkapp.gvpintranet.R
import cz.hlinkapp.gvpintranet.fragments.preferences.MainPreferenceFragment
import kotlinx.android.synthetic.main.activity_settings.*

/**
 * An activity for the app's settings.
 */
class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) supportFragmentManager.transaction {
            replace(R.id.settingsActivityFragmentContainer,
                MainPreferenceFragment(), MainPreferenceFragment.TAG)
        }
    }
}
