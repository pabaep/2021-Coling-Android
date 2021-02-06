package com.example.coling

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import kotlin.coroutines.CoroutineContext

class SplashActivity : AppCompatActivity() {

    val SPLASH_VIEW_TIME: Long = 2000 //2초간 스플래시 화면을 보여줌 (ms)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({ //delay를 위한 handler
            startActivity(Intent(this, GoogleLogInActivity::class.java))
            finish()
        }, SPLASH_VIEW_TIME)
    }
}