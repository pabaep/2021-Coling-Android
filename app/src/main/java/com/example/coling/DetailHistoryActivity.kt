package com.example.coling

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_detail_history.*

class DetailHistoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_history)



        //modify누르면 수정모드로 바뀜(버튼, content)
        btn_modify.setOnClickListener{
            act_content_read.visibility = View.GONE
            act_content_modify.visibility = View.VISIBLE
            btn_modify.visibility = View.GONE
            btn_save.visibility = View.VISIBLE
        }


    }

}