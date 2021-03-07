package com.example.coling

import android.os.Bundle
import android.text.Html
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_setting_terms.*

class SettingTermsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_terms)

        btn_terms_back.setOnClickListener{
            finish()
        }

        tv_setting_terms.setText(Html.fromHtml(getString(R.string.terms1), Html.FROM_HTML_MODE_COMPACT))
        tv_setting_terms2.setText(Html.fromHtml(getString(R.string.terms2), Html.FROM_HTML_MODE_COMPACT))
    }
}