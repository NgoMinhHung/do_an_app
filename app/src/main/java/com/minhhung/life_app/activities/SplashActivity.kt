package com.minhhung.life_app.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.minhhung.life_app.utils.getToken

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onStart()
    }

    override fun onStart() {
        super.onStart()
        if (getToken()!!.isBlank()) {
            startActivity(Intent(this, SignInActivity::class.java))
        } else {
            startActivity(Intent(this, MainActivity::class.java))
        }
        finish()
    }
}