package com.example.coling

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_act_record.*

class ActRecordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_act_record)

        back_btn.setOnClickListener({
            val intent = Intent(this, RandomActActivity::class.java)
            startActivity(intent)
        })
    }
}