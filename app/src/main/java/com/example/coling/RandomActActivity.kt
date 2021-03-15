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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_random_act)
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()


//        var random_act = firestore?.collection("act_checks")?.document("act_check_${auth?.currentUser?.uid}")?.collection("act")?.whereEqualTo("act_check",false)?.orderBy("act_id")?.limit(1)
        var random_act = firestore?.collection("Act")

        random_act?.get()?.addOnSuccessListener { document ->
            Log.d("로그-1-1--",document.toString())
            //아래 for문에서 컬렉션 Act 안의 전체 문서들이 documnet에 담겨있고 각 문서를 doc으로 하나씩 꺼내서 그 doc의 내용을 출력함.
            for(doc in document){
                Log.d("로그-1-2-------", "출력된다~!!")
                Log.d("로그-1-2-문서이름", doc.id)
                Log.d("로그-1-2-문서내용전체를_map으로", doc.data.toString())
                Log.d("로그-1-2-특정 key값만", doc.data["act_name"].toString())
            }
        }?.addOnFailureListener{
            Log.d("로그-1-3--","실패 . . . . ")
        }

        btn_doit.setOnClickListener{
            val intent = Intent(this, ActRecordActivity::class.java)
            intent.putExtra("act_name", random_act_name.text.toString())
            intent.putExtra("act_content", random_act_content.text.toString())
            startActivity(intent)
        }

        back.setOnClickListener{
            finish()
        }


    }
}
