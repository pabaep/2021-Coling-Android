package com.example.coling

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_setting_manual.*

class SettingManualActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_manual)

        // 뒤로가기 버튼
        btn_manual_back.setOnClickListener{
            finish()
        }

    }
}