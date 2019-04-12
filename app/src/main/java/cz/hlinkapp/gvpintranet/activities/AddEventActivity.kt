package cz.hlinkapp.gvpintranet.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.transaction
import cz.hlinkapp.gvpintranet.R
import cz.hlinkapp.gvpintranet.fragments.content_add_fragments.AddEventFragment
import kotlinx.android.synthetic.main.activity_add_event.*

/**
 * An activity for composing an event. The activity will use only one fragment, [AddEventFragment].
 */
class AddEventActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_event)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportFragmentManager.transaction {
            replace(R.id.addEventActivityFragmentContainer, AddEventFragment(), AddEventFragment.TAG)
        }
    }
}
