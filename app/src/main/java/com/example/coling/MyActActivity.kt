package com.example.coling

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_my_act.*
import kotlinx.android.synthetic.main.activity_random_act.*
import kotlinx.android.synthetic.main.activity_random_act.back
import kotlinx.android.synthetic.main.activity_random_act.btn_doit

class MyActActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_act)


        btn_doit.setOnClickListener{
            val intent = Intent(this, ActRecordActivity::class.java)
            intent.putExtra("act_name", my_act_name.text.toString())
            intent.putExtra("act_content", my_act_contents.text.toString())
            startActivity(intent)
            finish()
        }

        back.setOnClickListener{
            finish()
        }
    }
}