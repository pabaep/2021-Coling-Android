package com.example.coling

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_act_choose.*

class ActChooseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_act_choose)

        btn_choose_back.setOnClickListener{
            finish()
        }

        btn_choose_random.setOnClickListener {
            val intent = Intent(this, RecordIconActivity::class.java)
            startActivityForResult(intent, 0)
        }

        btn_choose_my.setOnClickListener {
            val intent = Intent(this, MyActActivity::class.java)
            startActivityForResult(intent,0)
        }
    }
}