package com.example.coling

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_act_record.*
import kotlinx.android.synthetic.main.activity_detail_history.*
import java.io.File
import java.text.DateFormat
import java.text.SimpleDateFormat

class DetailHistoryActivity : AppCompatActivity() {
    var firestore :FirebaseFirestore? = null
    var auth :FirebaseAuth? = null
    var uid :String? = null

    var day :Int? = null

    var recordDay :Int? = null

    //date는 non-null로 만들어야해서 나중에 선언
    var imgSrc :String? = null
    var actName :String? = null
    var actContent :String? = null
    var emo :String? = null
    var diary :String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_history)
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        uid = auth?.currentUser?.uid

        //HistoryFragment에서 보내준 값인 오늘의 day수와 기록일의 day수인 recordDay 받아옴
        day = intent.getIntExtra("day",0)
        recordDay = intent.getIntExtra("recordDay",0)
        Log.d("로그-intent로 받은 값","day ${day} , recordDay ${recordDay}")

        //firestore에서 기록정보 가져옴.화면에 넣음.
        firestore?.collection("Records")?.document("record_${uid}")?.collection("day")?.document("day${recordDay}")
            ?.get()
            ?.addOnSuccessListener {doc->
                Log.d("로그-1-success-record받기-","성공 ${doc.id}")
                Log.d("로그-1-success-record받기-",doc.toString())

                //제일 상단 날짜
                val df :DateFormat = SimpleDateFormat("MM.dd.yyyy")
                val date :Long = doc?.data?.get("date") as Long
                //사진 경로
                imgSrc = doc?.data?.get("img_src").toString()
                //활동 이름과 활동 내용
                actName = doc?.data?.get("act_name").toString()
                actContent = doc?.data?.get("act_content").toString()
                //Emotion, Dairy
                emo = doc?.data?.get("emo").toString()
                diary = doc?.data?.get("diary").toString()
                Log.d("로그-1-success-record받기-","emo ${emo}")

                //화면에 출력
                detail_date.text = df.format(date)

                //[호출]사진 화면에 띄우는 함수
                setPhoto(imgSrc)

                detail_act_name.text = actName
                detail_act_content.text = actContent

                //이모지 노가다 함수
                setEmoImage(emo)

                diary_read.text = diary
                diary_modify.text = diary.toEditable()

            }?.addOnFailureListener {
                Log.d("로그-1-fail-record받기-","실패 . . . .${it}")
            }



        //modify누르면 day수가 같은지(당일인지) 확인하고 수정모드로 바뀜(emotion, story, 버튼)
        btn_modify.setOnClickListener{
            //당일이면 수정모드로 바뀜(emotion, story, 버튼)
            if(day == recordDay){
                Log.d("로그-당일인가?-","day ${day} 와 recordDay${recordDay} 비교")
                //emotion부분 누르면 기분선택창으로 이동
                detail_emotion.setOnClickListener{
                    val intent = Intent(this, SelectEmotionActivity ::class.java) //프래그먼트로도 이렇게 이동 가능??
                    startActivity(intent)
                }
                //story부분에 textView가 아닌 EditText보여줌
                diary_read.visibility = View.INVISIBLE
                diary_modify.visibility = View.VISIBLE
                //버튼도 save로 보여줌
                btn_modify.visibility = View.INVISIBLE
                btn_save.visibility = View.VISIBLE
            }
            //당일 아님. 수정 못함. 토스트.
            else{
                var toast = Toast.makeText(this,"Records can only be edited on the day they were recorded.",Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.TOP , Gravity.CENTER, 90)
                toast.show()
                Log.d("로그-toast-","토스트 ${toast}")
            }

        }

        //저장하면 일단 mainActivity로 이동 (이후 수정 history로 화면이동)
        btn_save.setOnClickListener{
            firestore?.collection("Records")?.document("record_${uid}")?.collection("day")?.document("day${recordDay}")
                ?.update("diary",diary_modify.text.toString())
                ?.addOnSuccessListener {
                    Log.d("로그-fail-diary수정-","성공")
                }?.addOnFailureListener { Log.d("로그-fail-diary수정-","실패 . . . .") }

            val intent = Intent(this, MainActivity ::class.java) //프래그먼트로도 이렇게 이동 가능??
            startActivity(intent)
            finish()
        }

        //뒤로가기 일단 mainActivity로 이동 (이후 수정 history로 화면이동)
        back.setOnClickListener{
            val intent = Intent(this, MainActivity ::class.java) //프래그먼트로도 이렇게 이동 가능??
            startActivity(intent)
            finish()
        }
    }

    //사진 화면에 띄우는 함수
    fun setPhoto(imgSrc :String?){
        //절대경로의 이미지 파일을 bitmap으로 불러오고
        var bitmap = BitmapFactory.decodeFile(imgSrc)
        //bitmap개체를 ImageView에 보여줌
        detail_photo.setImageBitmap(bitmap)
    }


    fun setEmoImage(emo :String?){
        //Log.d("로그--emo-","emo ${emo}")
        if(emo == "emoji_1"){
            detail_emotion.setImageResource(R.drawable.emoji_1)
        }
        else if(emo == "emoji_2"){
            detail_emotion.setImageResource(R.drawable.emoji_2)
        }
        else if(emo == "emoji_3"){
            detail_emotion.setImageResource(R.drawable.emoji_3)
        }
        else if(emo == "emoji_4"){
            detail_emotion.setImageResource(R.drawable.emoji_4)
        }
        else if(emo == "emoji_5"){
            detail_emotion.setImageResource(R.drawable.emoji_5)
        }
        else if(emo == "emoji_6"){
            detail_emotion.setImageResource(R.drawable.emoji_6)
        }
        else if(emo == "emoji_7"){
            detail_emotion.setImageResource(R.drawable.emoji_7)
        }
        else if(emo == "emoji_8"){
            detail_emotion.setImageResource(R.drawable.emoji_8)
        }
        else if(emo == "emoji_9"){
            detail_emotion.setImageResource(R.drawable.emoji_9)
        }
        else if(emo == "emoji_10"){
            detail_emotion.setImageResource(R.drawable.emoji_10)
        }
        else if(emo == "emoji_11"){
            detail_emotion.setImageResource(R.drawable.emoji_11)
        }
        //이모지 데이터가 없을 때
        else{
            Log.d("로그-이모지-" , "이모지 데이터가 없음")
            //detail_emotion.setImageResource(R.drawable.emoji_hint_img)
        }
    }

    //EditText에 text값을 설정하기 위해 String을 Editable로 자료형 변환해주는 함수
    fun String?.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)

}