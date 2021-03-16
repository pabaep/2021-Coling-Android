package com.example.coling

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_history.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HistoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HistoryFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var cal = Calendar.getInstance()
    var firestore :FirebaseFirestore? = null
    var auth :FirebaseAuth? = null
    var uid :String? = null
    var nowDate :Long = System.currentTimeMillis()
    var day :Int? = null
    var sevenDayChecks :ArrayList<Boolean?> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        uid = auth?.currentUser?.uid
        //기록여부 확인, 새싹 이미지 소스 지정
        firestore?.collection("Users")?.document("user_${uid}")
            ?.get()
            ?.addOnSuccessListener { document->
                var startDate :Long = document.data?.get("start_date") as Long
                day = ((getIgnoredTimeDays(nowDate)-getIgnoredTimeDays(startDate))/(246060*1000)).toInt() + 1
                //Log.d("로그-success-days구하기","지금은 ${day} DAY")

                //[호출]현재 주차week와 그 주차에 해당하는 day_check값을 받아오는 함수
                getWeekAndDayChecks(day)

            }?.addOnFailureListener{
                Log.d("로그-fail--","user컬렉션 데이터 가져오기 실패")
            }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }
        }


        // when you click on the button, show DatePickerDialog that is set with OnDateSetListener
        btn_other_record!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                DatePickerDialog(
                    requireActivity(),
                    dateSetListener,
                    // set DatePickerDialog to point to today's date when it loads up
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
        })

        }

    //새싹 이미지를 클릭하면 detail history로 롸면이동시키는 함수
    fun readDetail(){
        //if(true)
        val intent = Intent(activity, DetailHistoryActivity :: class.java )
        startActivity(intent)
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HistoryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HistoryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun updateDateInView() {
        val myFormat = "MM/dd/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        //textview_date!!.text = sdf.format(cal.getTime())
    }

    //timestamp에서 시, 분, 초, 밀리초 제외시키는 함수
    fun getIgnoredTimeDays(time : Long): Long {
        return Calendar.getInstance().apply {
            timeInMillis = time
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    //현재 주차week와 그 주차의 day들에 해당하는 day_check값들을 받아오는 함수
    fun getWeekAndDayChecks(days :Int?){
        //Log.d("로그-success에서함수호출-days확인","지금은 ${days} DAY")

        //1. get week : 몇주차인지를 의미하는 week값을 이용해 현재 주차의 최소 day수와 최대 day수 계산하기
        var week = (days?.minus(1))?.div(7)
        var minDay :Any = week?.times(7)?.plus(1)!!
        var maxDay :Any = week?.times(7)?.plus(7)!!
        history_day.text="Day ${minDay} - ${maxDay}"
        //Log.d("로그-day최대 최소값 확인--", "minDay ${minDay} maxDay ${maxDay}")

        //2. get record checks : day범위에 해당하는 day_check값들 받아오기
        firestore?.collection("day_checks")?.document("day_check_${uid}")?.collection("day")
            ?.whereGreaterThanOrEqualTo("day", minDay)
            ?.whereLessThanOrEqualTo("day", maxDay)
            ?.get()
            ?.addOnSuccessListener {documents ->
                for(doc in documents){
                    if(doc.data["day_check"] != null){
                        sevenDayChecks.add(doc.data["day_check"] as Boolean)
                    }else{
                        sevenDayChecks.add(null)
                    }
                }
                //Log.d("로그-success-day기간 내 문서7개", sevenDayChecks.toString())
                //Log.d("로그-success-받아온 데이터 길이확인", sevenDayChecks.size.toString())

                //[호출]dayCheck값에 따라 7개의 이미지 소스 넣고 setOnClickListener다는 함수
                setRecordCheckImages(sevenDayChecks)
            }
            ?.addOnFailureListener {
                Log.d("로그-day기간 내 문서7개 받아오기--","실패 . . . ")
            }
    }

    //day_check값에 따라 이미지를 바꾸고, setOnClickListener를 달아서 기록한 날은 자세한 기록페이지로 이동, 기록하지 않은 날은 토스트메세지 뜨게 함.
    fun setRecordCheckImages(sevenDayChecks :ArrayList<Boolean?>){
        var noRecordToast = Toast.makeText(activity, "There is no record for this date.", Toast.LENGTH_SHORT)
        if(sevenDayChecks[0] != null){
            if(sevenDayChecks[0] == true){
                check_1.setImageResource(R.drawable.tabbar_icon_overcome)
                //새싹 이미지 클릭 시 자세한 기록 보여주는 함수 readDetail()호출
                check_1.setOnClickListener{
                    readDetail()
                }
            }else if(sevenDayChecks[0] == false){
                check_1.setImageResource(R.drawable.check_no_record)
                //기록하지 않은 날짜 새싹 클릭 시 토스트 메세지 띄움
                check_1.setOnClickListener{
                    noRecordToast.show()
                }
            }
        }
        if(sevenDayChecks[1] != null){
            if(sevenDayChecks[1] == true){
                check_2.setImageResource(R.drawable.tabbar_icon_overcome)
                check_2.setOnClickListener{
                    readDetail()
                }
            }else if(sevenDayChecks[1] == false){
                check_2.setImageResource(R.drawable.check_no_record)
                check_2.setOnClickListener{
                    noRecordToast.show()
                }
            }
        }
        if(sevenDayChecks[2] != null){
            if(sevenDayChecks[2] == true){
                check_3.setImageResource(R.drawable.tabbar_icon_overcome)
                check_3.setOnClickListener{
                    readDetail()
                }
            }else if(sevenDayChecks[2] == false){
                check_3.setImageResource(R.drawable.check_no_record)
                check_3.setOnClickListener{
                    noRecordToast.show()
                }
            }
        }
        if(sevenDayChecks[3] != null){
            if(sevenDayChecks[3] == true){
                check_4.setImageResource(R.drawable.tabbar_icon_overcome)
                check_4.setOnClickListener{
                    readDetail()
                }
            }else if(sevenDayChecks[3] == false){
                check_4.setImageResource(R.drawable.check_no_record)
                check_4.setOnClickListener{
                    noRecordToast.show()
                }
            }
        }
        if(sevenDayChecks[4] != null){
            if(sevenDayChecks[4] == true){
                check_5.setImageResource(R.drawable.tabbar_icon_overcome)
                check_5.setOnClickListener{
                    readDetail()
                }
            }else if(sevenDayChecks[4] == false){
                check_5.setImageResource(R.drawable.check_no_record)
                check_5.setOnClickListener{
                    noRecordToast.show()
                }
            }
        }
        if(sevenDayChecks[5] != null){
            if(sevenDayChecks[5] == true){
                check_6.setImageResource(R.drawable.tabbar_icon_overcome)
                check_6.setOnClickListener{
                    readDetail()
                }
            }else if(sevenDayChecks[5] == false){
                check_6.setImageResource(R.drawable.check_no_record)
                check_6.setOnClickListener{
                    noRecordToast.show()
                }
            }
        }
        if(sevenDayChecks[6] != null){
            if(sevenDayChecks[6] == true){
                check_7.setImageResource(R.drawable.tabbar_icon_overcome)
                check_7.setOnClickListener{
                    readDetail()
                }
            }else if(sevenDayChecks[6] == false){
                check_7.setImageResource(R.drawable.check_no_record)
                check_7.setOnClickListener{
                    noRecordToast.show()
                }
            }
        }

    }
}