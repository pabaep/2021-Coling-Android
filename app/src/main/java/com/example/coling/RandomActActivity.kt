package com.example.coling

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_random_act.*

class RandomActActivity : AppCompatActivity() {

//    private lateinit var actName :TextView
//    private lateinit var actContent :TextView
    private val actName :TextView = random_act_name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_random_act)

//        actName = findViewById<TextView>(R.id.random_act_name)
//        actContent = findViewById<TextView>(R.id.random_act_content)

        btn_doit.setOnClickListener{
            val intent = Intent(this, ActRecordActivity::class.java)
           //intent.putExtra("act_name", actName.text.toString())
            startActivity(intent)
        }

        back.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


    }
}