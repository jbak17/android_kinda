package io.bsconsulting.cosc370

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Window
import androidx.core.content.ContextCompat
import io.bsconsulting.cosc370.activities.DashActivity

class SplashActivity : AppCompatActivity() {

    private val SplashDuration = 2000L

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        // no menu bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)

        // image
        setContentView(R.layout.activity_splash)

        scheduleSplashScreen()
    }

    private fun scheduleSplashScreen() {
        Handler().postDelayed(
            {
                start()
            },
            SplashDuration
        )
    }

    fun start(){
        val intent = Intent(this, DashActivity::class.java)
        startActivity(intent)
    }
}
