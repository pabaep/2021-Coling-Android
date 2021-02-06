package com.example.coling

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_random_act.*

class MyActActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_act)


        btn_doit.setOnClickListener{
            val intent = Intent(this, ActRecordActivity::class.java)
            startActivity(intent)
        }

        back.setOnClickListener{
            val intent = Intent(this, ActChooseActivity::class.java)
            startActivity(intent)
        }
    }
}