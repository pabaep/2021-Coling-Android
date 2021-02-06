package com.example.coling

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_select_emotion.*

class SelectEmotionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_emotion)

        emoji_1_img.setOnClickListener {
            //이미지 선택하면 actRecord의 act_record_emotion_img로 넘기기?
        }

    }
}