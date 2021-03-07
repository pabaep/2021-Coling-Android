package com.example.coling

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_random_act.*
import kotlinx.android.synthetic.main.activity_select_emotion.*

class SelectEmotionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_emotion)

        emoji_1_img.setOnClickListener {
            //이미지 선택하면 actRecord의 act_record_emotion_img로 넘기기?
            val drawable = getDrawable(R.drawable.emoji_1)
            val intent = Intent(this, ActRecordActivity::class.java)
            //intent.putExtra("emoji_select_src", drawable)
            startActivity(intent)

        }

    }
}