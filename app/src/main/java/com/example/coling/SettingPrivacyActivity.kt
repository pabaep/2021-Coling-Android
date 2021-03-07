package com.example.coling

import android.os.Bundle
import android.text.Html
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_setting_privacy.*

class SettingPrivacyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_privacy)

        btn_privacy_back.setOnClickListener{
            finish()
        }

        tv_setting_privacy.setText(Html.fromHtml(getString(R.string.terms_personal), Html.FROM_HTML_MODE_COMPACT))
    }
}