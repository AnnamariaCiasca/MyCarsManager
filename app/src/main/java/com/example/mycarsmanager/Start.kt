package com.example.mycarsmanager

import android.content.Intent
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class Start : AppCompatActivity() {
private val SPLASH_TIME_OUT: Long=4000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activitystart)

        Handler().postDelayed({

            startActivity(Intent(this,MainActivity::class.java))

            finish()

        }, SPLASH_TIME_OUT)
    }
}
