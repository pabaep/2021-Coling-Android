package com.example.coling

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_act_record.*

class ActRecordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_act_record)

        back_btn.setOnClickListener {
            finish()
        }

        act_record_emotion_img.setOnClickListener {
            val intent = Intent(this, SelectEmotionActivity::class.java)
            startActivity(intent)
        }

        act_record_photo_img.setOnClickListener {
            //카메라 사진찍기 구현
        }

        act_record_save_btn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)//기록 보기 화면으로 추후 전환
            startActivity(intent)
        }
    }

}