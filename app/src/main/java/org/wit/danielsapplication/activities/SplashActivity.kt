package org.wit.danielsapplication.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowInsets
import android.view.WindowManager
import org.jetbrains.anko.startActivity
import org.wit.danielsapplication.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

/* I had some issues trying to make this splash screen affect, obviously the goal is to make a full screen splash affect and I done this previously using just
window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
  This also work but with the method below we are supressing the deprecation from the previous method, I learnt this from here  -> https://stackoverflow.com/questions/62835053/how-to-set-fullscreen-in-android-r
  Note -- Even if your running an older version of android that doesnt support android R it should still work
 */
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
@Suppress("DEPRECATION")
        Handler().postDelayed(
            {
                // Launches Main Activity
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                finish() // Call this when ur activity is done and should close this activity
            },
            1500
        )
    }
}

// Here is another ref that I used to help me develop this splash affect
// https://abhiandroid.com/programming/splashscreen#:~:text=Splash%20Screen%20is%20most%20commonly,%2C%20name%2C%20advertising%20content%20etc.