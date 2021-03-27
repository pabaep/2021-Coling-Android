package com.example.coling

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_my_act.*
import kotlinx.android.synthetic.main.activity_random_act.*
import kotlinx.android.synthetic.main.activity_random_act.back
import kotlinx.android.synthetic.main.activity_random_act.btn_doit

class MyActActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_act)


        btn_doit.setOnClickListener{
            var myActName = my_act_name.text.toString()
            var myActContent = my_act_contents.text.toString()
            val intent = Intent(this, ActRecordActivity::class.java)
            /*intent.putExtra("act_name", my_act_name.text.toString())
            intent.putExtra("act_content", my_act_contents.text.toString())
            intent.putExtra("act_num", 0)
            startActivity(intent)*/
            if(myActName==""||myActContent==""){
                Toast.makeText(this,"Write act name or act content",Toast.LENGTH_SHORT).show()
            }
            else{
                intent.putExtra("act_name",myActName)
                intent.putExtra("act_content",myActContent)
                intent.putExtra("act_num", 0)
                startActivity(intent)
            }
        }

        back.setOnClickListener{
            finish()
        }
    }
}