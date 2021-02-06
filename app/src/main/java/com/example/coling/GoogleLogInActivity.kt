package com.example.coling

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_google_log_in.*

class GoogleLogInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_log_in)

        google_login_btn.setOnClickListener {
            //지금은 단순하게 페이지 넘어가는 것으로 구성, 후에 로그인 구현후 넘어가게 할 것임
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}