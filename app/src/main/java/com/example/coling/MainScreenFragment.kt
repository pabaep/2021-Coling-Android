package com.example.coling

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_main_screen.*


class MainScreenFragment : Fragment() {
    var firestore : FirebaseFirestore? = null
    var auth : FirebaseAuth? = null
    var uid :String? = null
    var startDate : Long? = null
    var day : Long = 0
    var title :String? = null
    var writer :String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        uid = auth?.currentUser?.uid

        /*text_day_real.text = "999"

        getDay()
        text_day_real.text = day.toString() // 화면에 day 수 띄우줌*/



    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //text_day_real.text = "0"

        getDay()


        today_act_start_btn.setOnClickListener{
            goToStart()
        }

    }

    private fun goToStart(){
        val intent = Intent(activity, ActChooseActivity :: class.java )//화면 선택 화면(ActChooseActivity)으로 넘어감
        startActivity(intent)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_screen, container, false)
    }

    //몇일째인지(day)띄우기위해 계산하는 함수
    fun getDay(){
        firestore?.collection("Users")
            ?.whereEqualTo("uid", auth?.currentUser?.uid)
            ?.get()
            ?.addOnSuccessListener { documents ->
                for (document in documents) {
                    startDate = document.data["start_date"] as Long
                    break
                }
                Log.d("로그-4-1--",startDate.toString()+"나오나요...")
                if(startDate == null){
                    //user데이터 업로드
                    day = 1
                    text_day_real.text = day.toString() // 화면에 day 수 띄우줌
                    getQuote(day)
                }

                else {
                    val nowDate = System.currentTimeMillis()
                    day = (nowDate - startDate!!)/(24*60*60*1000) + 1
                    text_day_real.text = day.toString() // 화면에 day 수 띄우줌
                    getQuote(day)
                    /*firestore?.collection("Quote")
                        ?.whereEqualTo("qid",day.toInt())
                        ?.get()
                        ?.addOnSuccessListener { documents ->
                            for (document in documents){
                                title = document.data["title"] as String
                                writer = document.data["writer"] as String
                                break
                            }
                            today_quote_content.text = title
                            today_quote_writer.text = writer
                        }*/
                }


            }
            ?.addOnFailureListener{
                Log.d("로그-3-3--","실패 . . . . ")
            }
    }
    fun getQuote(day: Long){
        firestore?.collection("Quote")
            ?.whereEqualTo("qid",day.toInt())
            ?.get()
            ?.addOnSuccessListener { documents ->
                for (document in documents){
                    title = document.data["title"] as String
                    writer = document.data["writer"] as String
                    break
                }
                today_quote_content.text = title
                today_quote_writer.text = writer
            }
    }

}