package com.example.coling

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
    var actName :String = ""
    var actContent :String = ""

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_random_act)
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        getRandomAct()

        btn_doit.setOnClickListener{
            val intent = Intent(this, ActRecordActivity::class.java)
            intent.putExtra("act_name", actName)
            intent.putExtra("act_content", actContent)
            startActivity(intent)
        }

        btn_random_again.setOnClickListener {
            //임시로 암거나 넣어둔 코드
            random_act_name.text = "next"
            random_act_content.text = "random"

            //처음으로 띄운 randomAct화면이 아니라 Random again을 눌러서 getRandomAct가 호출된 것일때
            // -> 넘긴 활동의 act_id에 +100하고 현user의 act_num을 +1 하는 내용 이후 추가할 것
            //getRandomAct()
        }

        back.setOnClickListener{
            finish()
        }
    }



    //RandomAct를 받아오는 함수
    fun getRandomAct(){
        //day_num값을 받아와서 whereEqualTo의 act_id로 쓸 것
        var randomAct = firestore?.collection("Act")
        randomAct
            ?.whereEqualTo("act_check",false)
            ?.whereEqualTo("act_id",1)
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
            }?.addOnFailureListener{
                Log.d("로그-1-3--","실패 . . . . ")
            }

    }

}
