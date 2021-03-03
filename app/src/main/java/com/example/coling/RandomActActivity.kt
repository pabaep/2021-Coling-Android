package com.example.coling

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_random_act.*

class RandomActActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_random_act)

        btn_doit.setOnClickListener{
            val intent = Intent(this, ActRecordActivity::class.java)
            intent.putExtra("act_name", random_act_name.text.toString())
            intent.putExtra("act_content", random_act_content.text.toString())
            startActivity(intent)
        }

        back.setOnClickListener{
            finish()
        }


    }
}
