package io.bsconsulting.cosc370.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.animation.AlphaAnimation
import io.bsconsulting.cosc370.R
import kotlinx.android.synthetic.main.activity_splash.*


class SplashActivity : AppCompatActivity() {

    private val SplashDuration = 3000L

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        // no menu bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)

        // image
        setContentView(R.layout.activity_splash)
        val fadeIn = AlphaAnimation(0.0f, 1.0f)
        titleText.startAnimation(fadeIn)
        fadeIn.duration = (SplashDuration * .7).toLong()
        fadeIn.fillAfter = true

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
