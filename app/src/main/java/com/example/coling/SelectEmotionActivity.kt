package com.example.coling

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
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
            val intent = Intent()
            intent.putExtra("emo_select_string","emoji_1" )
            setResult(Activity.RESULT_OK,intent)
            finish()
        }
        emoji_2_img.setOnClickListener {
            //이미지 선택하면 actRecord의 act_record_emotion_img로 넘기기?
            val intent = Intent()
            intent.putExtra("emo_select_string","emoji_2" )
            setResult(Activity.RESULT_OK,intent)
            finish()
        }
        emoji_3_img.setOnClickListener {
            //이미지 선택하면 actRecord의 act_record_emotion_img로 넘기기?
            val intent = Intent()
            intent.putExtra("emo_select_string","emoji_3" )
            setResult(Activity.RESULT_OK,intent)
            finish()
        }
        emoji_4_img.setOnClickListener {
            //이미지 선택하면 actRecord의 act_record_emotion_img로 넘기기?
            val intent = Intent()
            intent.putExtra("emo_select_string","emoji_4" )
            setResult(Activity.RESULT_OK,intent)
            finish()
        }
        emoji_5_img.setOnClickListener {
            //이미지 선택하면 actRecord의 act_record_emotion_img로 넘기기?
            val intent = Intent()
            intent.putExtra("emo_select_string","emoji_5" )
            setResult(Activity.RESULT_OK,intent)
            finish()
        }
        emoji_6_img.setOnClickListener {
            //이미지 선택하면 actRecord의 act_record_emotion_img로 넘기기?
            val intent = Intent()
            intent.putExtra("emo_select_string","emoji_6" )
            setResult(Activity.RESULT_OK,intent)
            finish()
        }
        emoji_7_img.setOnClickListener {
            //이미지 선택하면 actRecord의 act_record_emotion_img로 넘기기?
            val intent = Intent()
            intent.putExtra("emo_select_string","emoji_7" )
            setResult(Activity.RESULT_OK,intent)
            finish()
        }
        emoji_8_img.setOnClickListener {
            //이미지 선택하면 actRecord의 act_record_emotion_img로 넘기기?
            val intent = Intent()
            intent.putExtra("emo_select_string","emoji_8" )
            setResult(Activity.RESULT_OK,intent)
            finish()
        }
        emoji_9_img.setOnClickListener {
            //이미지 선택하면 actRecord의 act_record_emotion_img로 넘기기?
            val intent = Intent()
            intent.putExtra("emo_select_string","emoji_9" )
            setResult(Activity.RESULT_OK,intent)
            finish()
        }
        emoji_10_img.setOnClickListener {
            //이미지 선택하면 actRecord의 act_record_emotion_img로 넘기기?
            val intent = Intent()
            intent.putExtra("emo_select_string","emoji_10" )
            setResult(Activity.RESULT_OK,intent)
            finish()
        }
        emoji_11_img.setOnClickListener {
            //이미지 선택하면 actRecord의 act_record_emotion_img로 넘기기?
            val intent = Intent()
            intent.putExtra("emo_select_string","emoji_11" )
            setResult(Activity.RESULT_OK,intent)
            finish()
        }
    }
}