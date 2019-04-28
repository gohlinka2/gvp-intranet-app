package cz.hlinkapp.gvpintranet.fragments.preferences

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.transaction
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import cz.hlinkapp.gvpintranet.R
import cz.hlinkapp.gvpintranet.config.resolveActivityOrNull
import cz.hlinkapp.gvpintranet.contracts.ServerContract


/**
 * A Fragment containing the main preferences of the app.
 */
class MainPreferenceFragment : PreferenceFragmentCompat() {

    override fun onStart() {
        super.onStart()
        (activity as AppCompatActivity)?.supportActionBar?.title = getString(R.string.settings)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.main_preferences, rootKey)

        val namePref = findPreference(getString(R.string.pref_key_name)) as EditTextPreference
        namePref.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, newValue ->
            if (newValue.toString().isNotBlank()) true else {
                Toast.makeText(
                    context,
                    getString(R.string.field_must_not_be_empty),
                    Toast.LENGTH_LONG
                ).show()
                false
            }
        }
        val emailPref = findPreference(getString(R.string.pref_key_email)) as EditTextPreference
        emailPref.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, newValue ->
            if (newValue.toString().isNotBlank()) {
                if (Patterns.EMAIL_ADDRESS.matcher(newValue.toString()).matches()) true else {
                    Toast.makeText(
                        context,
                        getString(R.string.enter_valid_email),
                        Toast.LENGTH_LONG
                    ).show()
                    false
                }
            } else {
                Toast.makeText(
                    context,
                    getString(R.string.field_must_not_be_empty),
                    Toast.LENGTH_LONG
                ).show()
                false
            }
        }
        val relationPref =
            findPreference(getString(R.string.pref_key_relation)) as EditTextPreference
        relationPref.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, newValue ->
            if (newValue.toString().isNotBlank()) true else {
                Toast.makeText(
                    context,
                    getString(R.string.field_must_not_be_empty),
                    Toast.LENGTH_LONG
                ).show()
                false
            }
        }

        val sendEmailPref = findPreference(getString(R.string.pref_key_send_email))
        sendEmailPref.setOnPreferenceClickListener {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:${getString(R.string.author_email)}")
                putExtra(Intent.EXTRA_EMAIL, getString(R.string.author_email))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_email_subject))
            }
            if (intent.resolveActivityOrNull(activity?.packageManager) != null) startActivity(
                Intent.createChooser(
                    intent, getString(
                        R.string.send_mail_to_developer
                    )
                )
            )
            true
        }

        val licenceInfoPref = findPreference(getString(R.string.pref_key_licence_info))
        licenceInfoPref.setOnPreferenceClickListener {
            activity?.supportFragmentManager?.transaction {
                replace(
                    R.id.settingsActivityFragmentContainer,
                    LicenceInfoFragment(),
                    LicenceInfoFragment.TAG
                )
                addToBackStack(LicenceInfoFragment.TAG)
            }
            true
        }

        val githubPref = findPreference(getString(R.string.pref_key_github))
        githubPref.setOnPreferenceClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(ServerContract.GITHUB_REPO_LINK)))
            true
        }

        val ratePref = findPreference(getString(R.string.pref_key_rate))
        ratePref.setOnPreferenceClickListener {
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + context?.packageName))
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + context?.packageName)))
            }
            true
        }
    }

    companion object {
        const val TAG = "MainPreferenceFragment"
    }

}