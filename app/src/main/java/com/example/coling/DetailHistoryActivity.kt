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



        //modify누르면 수정모드로 바뀜(emotion, story, 버튼)
        btn_modify.setOnClickListener{
            //emotion부분 누르면 기분선택창으로 이동
            detail_emotion_modify.setOnClickListener{
                val intent = Intent(this, SelectEmotionActivity ::class.java) //프래그먼트로도 이렇게 이동 가능??
                startActivity(intent)
            }
            //story부분에 textView가 아닌 EditText보여줌
            act_content_read.visibility = View.INVISIBLE
            act_content_modify.visibility = View.VISIBLE
            //버튼도 save로 보여줌
            btn_modify.visibility = View.INVISIBLE
            btn_save.visibility = View.VISIBLE
        }

        //저장하면 일단 mainActivity로 이동 (이후 수정 history로 화변이동)
        btn_save.setOnClickListener{
            val intent = Intent(this, MainActivity ::class.java) //프래그먼트로도 이렇게 이동 가능??
            startActivity(intent)
            finish()
        }

        //뒤로가기 일단 mainActivity로 이동 (이후 수정 history로 화변이동)
        back.setOnClickListener{
            val intent = Intent(this, MainActivity ::class.java) //프래그먼트로도 이렇게 이동 가능??
            startActivity(intent)
            finish()
        }

    }

}