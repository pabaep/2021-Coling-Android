package com.example.coling

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.coling.model.ModelAct
import com.example.coling.model.ModelActCheck
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.getField
import kotlinx.android.synthetic.main.activity_random_act.*

class RandomActActivity : AppCompatActivity() {
    var firestore : FirebaseFirestore? = null
    var auth :FirebaseAuth? = null
    var uid :String? = null
    var actName :String? = null
    var actContent :String? = null
    var actNum :Int? = null
    var todayAgain :Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_random_act)
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        uid = auth?.currentUser?.uid

        //오늘 이 유저가 randomAgain을 눌렀는지 확인하는 값 가져오기
        firestore?.collection("Users")?.document("user_${uid}")?.get()
            ?.addOnSuccessListener{ doc->
                todayAgain = doc?.data?.get("today_again") as Boolean
                Log.d("로그-0success-today_again","${ doc?.data?.get("today_again") }")
                Log.d("로그-0-success-todayAgain","${ todayAgain }")
            }?.addOnFailureListener { Log.d("로그-0-fail-","실패 . . . .") }

        firestore?.collection("Users")?.document("user_${uid}")
            ?.get()
            ?.addOnSuccessListener { document ->
                //Log.d("로그-1-0-actNum받아오기","${document.data?.get("act_num")}")
                //Log.d("로그-1-0-actNum받아오기","${document.data?.get("act_num")!!::class.simpleName}")
                //act_num을 받아오고 보니 int가 아니라 long 형이라서 long 변수로 받아온 다음 int로 변환함. (값을 완전히 받아오기 전에는 Any로 취급해서 형변환이 안됨.)
                var actNumLong :Long = document.data?.get("act_num") as Long
                actNum = actNumLong.toInt()
                Log.d("로그-1-success-actNum받아오기","actNum ${actNum}")

                //[호출]현 user의 actNum과 act_id가 같은 randomAct 이름과 내용 받아오기
                getRandomAct(actNum)
            }
            ?.addOnFailureListener{
                Log.d("로그-1-actNum받아오기-","실패 . . . ")
            }


        btn_random_again.setOnClickListener {
            //Log.d("로그-2-againClicked","againClicked ${againClicked}")
            if(!todayAgain!!){
                //[호출]활동을 넘길 때 넘긴 활동의 act_id에 +100함. getRandomAct와 actNumPlusOne도 호출.
                RandomAgain()
                todayAgain = true
                //Log.d("로그-2-again에 Listener","실행끝")
                //Log.d("로그-2-againClicked","againClicked ${againClicked}")
                firestore?.collection("Users")?.document("user_${uid}")
                    ?.update("today_again",true)
                    ?.addOnSuccessListener {
                        Log.d("로그-2-2-success-update","today_again을 true로 성공")
                    }?.addOnFailureListener{ Log.d("로그-2-2-fail-update","실패 . . . .") }
            }else if(todayAgain!!){
                Toast.makeText(this,"The activity can only be re-recommended once a day.",Toast.LENGTH_LONG).show()
                //Log.d("로그-2-2-againClicked","againClicked ${againClicked}")
            }


        }


        btn_doit.setOnClickListener{
            val intent = Intent(this, ActRecordActivity::class.java)
            intent.putExtra("act_name", actName)
            intent.putExtra("act_content", actContent)
            intent.putExtra("act_num", actNum)
            startActivity(intent)

            //[호출]지금 실행한 활동 act_check를 true로 바꾸기.
            actCheckTrue()

//세원언니 수정 이후 이부분 코드 지우기!!!!!!
            //[호출]활동을 하나 한거니까 현 user의 actNum +1하기
            actNumPlusOne(false)

        }

        back.setOnClickListener{
            finish()
        }
    }



    //RandomAct를 받아와서 화면의 TextView에 보여주는 함수
    fun getRandomAct(actNum :Int?){
        var randomAct = firestore?.collection("Act")
        randomAct
            ?.whereEqualTo("act_check",false)
            ?.whereEqualTo("act_id",actNum)
            ?.get()
            ?.addOnSuccessListener { documents ->
                //아래 for문에서 컬렉션 Act 안의 전체 문서들이 documnets에 담겨있고 각 문서를 doc으로 하나씩 꺼내서 그 doc의 내용을 출력함.
                for(doc in documents){
                    actName = doc.data["act_name"].toString()
                    actContent =  doc.data["act_content"].toString()
                    break
                }
                random_act_name.text = actName
                random_act_content.text = actContent
            }
            ?.addOnFailureListener{ Log.d("로그-1-3--","실패 . . . . ") }
    }

    //RandomAgain 를 눌렀을 때 실행되는 함수 -> 넘긴 활동의 act_id에 +100함. actNumPlusOne 호출 (현user의 act_num을 +1 하는 함수)
    fun RandomAgain(){
        //다음 활동 불러옴
        getRandomAct(actNum?.plus(1))


        //넘긴 활동 act_check_uid 의 act_id값 수정. +100
        firestore?.collection("act_checks")?.document("act_check_${uid}")?.collection("act")?.document("act${actNum}")
            ?.update("act_id",actNum?.plus(100))
            ?.addOnSuccessListener {
                Log.d("로그-3-success-넘긴 actId수정","성공")
            }
            ?.addOnFailureListener { Log.d("로그-3-넘긴 actId수정","실패 . . .") }

        //[호출]활동을 넘겼으므로 현재 actNum변수와 DB의 user_uid의 actNum값 수정하는 함수. +1
        actNumPlusOne(true)
    }

    //활동을 넘겼으므로 user_uid의 actNum값 수정하는 함수. +1
    fun actNumPlusOne(again :Boolean){
        firestore?.collection("Users")?.document("user_${uid}")
            ?.update("act_num", actNum?.plus(1))
            ?.addOnSuccessListener {
                Log.d("로그-4-success-act_num+1","성공")
            }?.addOnFailureListener { Log.d("로그-4-actNum+1","실패 . . . ") }

        //활동을 넘겼으므로 현재 actNum변수+1
        if(again){
            Log.d("로그-4-before-actNum+1","actNum ${actNum}")
            actNum = actNum?.plus(1)
            Log.d("로그-4-after-actNum+1","actNum ${actNum}")
        }

    }

    fun actCheckTrue(){
        firestore?.collection("act_checks")?.document("act_check_${uid}")?.collection("act")?.document("act${actNum}")
            ?.update("act_check",true)
            ?.addOnSuccessListener {
                Log.d("로그-5-actCheckTrue","성공")
            }?.addOnFailureListener { Log.d("로그-5-actCheckTrue","실 패 . . . .") }
    }

}
