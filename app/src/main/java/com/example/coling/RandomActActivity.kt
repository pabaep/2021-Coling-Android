package com.example.coling

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_random_act.*

class RandomActActivity : AppCompatActivity() {
    var firestore : FirebaseFirestore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_random_act)
        firestore = FirebaseFirestore.getInstance()


        var random_act = firestore?.collection("act")?.orderBy("act_check")?.orderBy("act_id")


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
