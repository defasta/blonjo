package com.apdef.mentari.views.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.apdef.mentari.R
import com.apdef.mentari.views.activities.auth.LoginActivity

class SplashActivity : AppCompatActivity() {
    private val SPLASH_TIME_OUT:Long=1000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
            finish()
        },SPLASH_TIME_OUT)
    }
}