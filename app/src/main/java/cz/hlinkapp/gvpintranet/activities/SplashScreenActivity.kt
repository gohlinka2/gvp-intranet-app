package cz.hlinkapp.gvpintranet.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * This is the launcher activity.
 * The only purpose of this activity is to show the app's branding (defined in the activity's theme) before the app has been initialized, and then start the main activity.
 */
class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this,MainActivity::class.java))
    }
}
