package com.utn.ddam.clase04.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.utn.ddam.clase04.R

class SplashScreenActivity : AppCompatActivity() {
    private val SPLASH_TIME : Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (supportActionBar != null)
            supportActionBar?.hide()
        setContentView(R.layout.activity_splash_screen)

        Handler().postDelayed({
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }, SPLASH_TIME);
    }
}