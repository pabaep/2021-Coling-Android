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
import kotlinx.android.synthetic.main.fragment_main_screen.*
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
    var day :Int? = null
    var sevenDayChecks :ArrayList<Boolean?> = arrayListOf()
    var sevenDays :ArrayList<Int?> = arrayListOf()
    var sevenDayChecks_datepicker :ArrayList<Boolean?> = arrayListOf()
    var week :Int? = null
    var calday : Int? = null
    var startDate :Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        uid = auth?.currentUser?.uid


        //start_date를 가져와서 오늘의 day수 계산하기 - 세원언니 MainScreen에서 getDay() 코드가져와 수정
        firestore?.collection("Users")
            ?.whereEqualTo("uid", auth?.currentUser?.uid)
            ?.get()
            ?.addOnSuccessListener { documents ->
                for (document in documents) {
                    startDate = document.data["start_date"] as Long
                    break
                }
                Log.d("로그-start_date받기--","성공 startDate"+startDate.toString())

                if(startDate == null){
                    //처음 접속한 유저임. statDate 데이터가 없음.
                    day = 1
                }
                else {
                    //Users데이터가 있는 유저임. 현재시간nowDate와 startDate로 day수 계산.
                    val nowDate = System.currentTimeMillis()
                    var dayLong :Long = (getIgnoredTimeDays(nowDate) - getIgnoredTimeDays(startDate!!))/(24*60*60*1000) + 1
                    day = dayLong.toInt()
                    Log.d("로그-day수-","day ${day}")
                }

                //[호출]현재 주차week와 그 주차에 해당하는 day_check값을 받아오는 함수
                getWeekAndDayChecks(day)
            }
            ?.addOnFailureListener{
                Log.d("로그-start_date--","실패 . . . . ")
            }
        //여기까지, 그리고 저 아래 getIgnoredTimeDays함수까지 세원언니 코드

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
                ).apply {
                    //startDate 이전 날짜는 클릭하지 못하도록
                    if((startDate!=null)){
                        datePicker.minDate= startDate as Long
                    }
                }.show()
            }
        })

        }

    //새싹 이미지를 클릭하면 detail history로 롸면이동시키는 함수
    fun readDetail(recordDay :Int){
        val intent = Intent(activity, DetailHistoryActivity :: class.java )
        //현재 day수를 해당 기록의 day인 recordDay와 함께 보내서 비교. 당일이 아닐 경우 수정불가.
        intent.putExtra("day",day)
        intent.putExtra("recordDay",recordDay)
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
        //datepicker에서 고른 날짜를 long으로 변환
        val calDayLong = cal.getTimeInMillis()
        Log.d("cal.getTime()은",calDayLong.toString())

        //datepicker에서 고른 날짜가 day몇인지 calday에 저장
        var myDay: Long = (getIgnoredTimeDays(calDayLong) - getIgnoredTimeDays(startDate!!)) / (24 * 60 * 60 * 1000) + 1
        calday = myDay.toInt()
        Log.d("calday 계산 ", calday.toString())

        //datepicker에서 선택한 날짜로 부터 7일간의 day와 그 주차에 해당하는 day_check값을 받아오는 함수
        getDayAndDayChecks(calday)


    }

    //시간, 분, 초, 밀리초 제외시키는 함수
    fun getIgnoredTimeDays(time : Long): Long{
        return Calendar.getInstance().apply {
            timeInMillis = time
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    //현재 주차week와 그 주차의 day들에 해당하는 day_check값들을 받아오는 함수
    fun getWeekAndDayChecks(day :Int?){
        //Log.d("로그-success에서함수호출-days확인","지금은 ${day} DAY")

        //1. get week : 몇주차인지를 의미하는 week값을 이용해 현재 주차의 최소 day수와 최대 day수 계산하기
        week = (day?.minus(1))?.div(7)
        var minDay :Int = week?.times(7)?.plus(1)!!
        var maxDay :Int = week?.times(7)?.plus(7)!!
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

                    //가져온 7일의 각 day수 7개를 저장.
                    var sevenDay :Long = doc.data["day"] as Long
                    sevenDays.add(sevenDay.toInt())
                }

                //Log.d("로그-success-Check 7개받아옴", sevenDayChecks.toString())
                //Log.d("로그-success-받아온 Check 길이", sevenDayChecks.size.toString())

                //Log.d("로그-success-day 7개받아옴", sevenDays.toString())
                //Log.d("로그-success-받아온 day 길이", sevenDays.size.toString())

                //[호출]지난 날짜의 day_checks값 관리 함수
                setPastDays(sevenDays,sevenDayChecks)

            }
            ?.addOnFailureListener {
                Log.d("로그-day기간 내 문서7개 받아오기--","실패 . . . ")
            }
    }

    //지난 날짜의 day_checks값 관리 함수. 오늘 날짜의 day수를 이용해서 지난 날짜인데 day_check값이 null이면 기록을 안한 것이므로 false로 바꿈.DB의 데이터까지.
    fun setPastDays(sevenDays :ArrayList<Int?>, sevenDayChecks: ArrayList<Boolean?>){
        //Log.d("로그-setPastDays-0-","day ${day}")

/*        //dayIndex : 오늘 day수에 해당하는 sevenDayChecks의 arrayList index값
        var dayIndex : Int =
            if (day!!%7 == 0){
                6
                }else{
                    (day!!%7)-1
                }*/

        for(i in 0..6){
            //지난 날짜인데 day_check값이 null인 경우
            if(sevenDayChecks[i] == null && sevenDays[i]!! < day!!){
                //Log.d("로그-setPastDays-yes-","${i}. ${i} < dayIndex ${dayIndex} sevenDayChecks[${i}] ${sevenDayChecks[i]} ")
                sevenDayChecks[i] = false
                firestore?.collection("day_checks")?.document("day_check_${uid}")?.collection("day")?.document("day${sevenDays[i]}")
                    ?.update("day_check",false)
                    ?.addOnSuccessListener { Log.d("로그-success-setPastDays-","성공") }
                    ?.addOnFailureListener { Log.d("로그-fail-setPastDays-","실패 . . .") }
            }
            else{ //Log.d("로그-setPastDays-no-","${i}. ${i} < dayIndex ${dayIndex} sevenDayChecks[${i}] ${sevenDayChecks[i]}")
            }
        }
        //Log.d("로그-setPastDays-변경 확인", sevenDayChecks.toString())

        //[호출]dayCheck값에 따라 7개의 이미지 소스 넣고 setOnClickListener다는 함수
        setRecordCheckImages(sevenDayChecks)
    }



    //day_check값에 따라 이미지를 바꾸고, setOnClickListener를 달아서 기록한 날은 자세한 기록페이지로 이동, 기록하지 않은 날은 토스트메세지 뜨게 함.
    fun setRecordCheckImages(sevenDayChecks :ArrayList<Boolean?>){
        var noRecordToast = Toast.makeText(activity, "There is no record for this date.", Toast.LENGTH_SHORT)
        if(sevenDayChecks[0] != null){
            if(sevenDayChecks[0] == true){
                check_1.setImageResource(R.drawable.tabbar_icon_overcome)
                //새싹 이미지 클릭 시 자세한 기록 보여주는 함수 readDetail()호출
                check_1.setOnClickListener{
                    readDetail(week?.times(7)?.plus(1)!!)
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
                    readDetail(week?.times(7)?.plus(2)!!)
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
                    readDetail(week?.times(7)?.plus(3)!!)
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
                    readDetail(week?.times(7)?.plus(4)!!)
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
                    readDetail(week?.times(7)?.plus(5)!!)
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
                    readDetail(week?.times(7)?.plus(6)!!)
                    //Log.d("로그-seven5-check6-","true listenter담")
                }
            }else if(sevenDayChecks[5] == false){
                check_6.setImageResource(R.drawable.check_no_record)
                check_6.setOnClickListener{
                    noRecordToast.show()
                    //Log.d("로그-seven5-check6-","false listenter담")
                }
            }
        }
        if(sevenDayChecks[6] != null){
            if(sevenDayChecks[6] == true){
                check_7.setImageResource(R.drawable.tabbar_icon_overcome)
                check_7.setOnClickListener{
                    readDetail(week?.times(7)?.plus(7)!!)
                }
            }else if(sevenDayChecks[6] == false){
                check_7.setImageResource(R.drawable.check_no_record)
                check_7.setOnClickListener{
                    noRecordToast.show()
                }
            }
        }

    }

    //datepicker에서 선택한 날짜로 부터 7일간의 day와 그 주차에 해당하는 day_check값을 받아오는 함수
    fun getDayAndDayChecks(day :Int?){
        //sevenDayChecks_datepicker 변수 초기화
        sevenDayChecks_datepicker.clear()

        //위에 day 변경
        var lastday = calday!!.toInt()+6
        history_day.text="Day ${calday} - ${lastday}"

        //day범위에 해당하는 day_check값들 받아오기
        firestore?.collection("day_checks")?.document("day_check_${uid}")?.collection("day")
            ?.whereGreaterThanOrEqualTo("day", calday!!)
            ?.whereLessThanOrEqualTo("day", lastday)
            ?.get()
            ?.addOnSuccessListener {documents ->
                for(doc in documents){
                    if(doc.data["day_check"] != null){
                        sevenDayChecks_datepicker.add(doc.data["day_check"] as Boolean)
                        Log.d("cal day_check", doc.data["day_check"].toString())
                    }else{
                        sevenDayChecks_datepicker.add(null)
                    }
                }
                setRecordCheckImages_datepicker(sevenDayChecks_datepicker)
            }
            ?.addOnFailureListener {
                Log.d("로그-day기간 내 문서7개 받아오기--","실패 . . . ")
            }
    }

    //datepicker 선택 후 day_check값에 따라 이미지를 바꾸고, setOnClickListener를 달아서 기록한 날은 자세한 기록페이지로 이동, 기록하지 않은 날은 토스트메세지 뜨게 함.
    fun setRecordCheckImages_datepicker(sevenDayChecks_datepicker :ArrayList<Boolean?>){
        var noRecordToast = Toast.makeText(activity, "There is no record for this date.", Toast.LENGTH_SHORT)

        if(sevenDayChecks_datepicker[0] == null) {
            check_1.setImageResource(R.drawable.check_empty_record)
        }else if(sevenDayChecks_datepicker[0] == true) {
            check_1.setImageResource(R.drawable.tabbar_icon_overcome)
            //새싹 이미지 클릭 시 자세한 기록 보여주는 함수 readDetail()호출
            check_1.setOnClickListener {
                readDetail(calday!!)
            }
        }else if(sevenDayChecks_datepicker[0] == false){
                check_1.setImageResource(R.drawable.check_no_record)
                //기록하지 않은 날짜 새싹 클릭 시 토스트 메세지 띄움
                check_1.setOnClickListener{
                    noRecordToast.show()
                }
            }

        if(sevenDayChecks_datepicker[1] == null) {
            check_2.setImageResource(R.drawable.check_empty_record)
        }else if(sevenDayChecks_datepicker[1] == true) {
            check_2.setImageResource(R.drawable.tabbar_icon_overcome)
            //새싹 이미지 클릭 시 자세한 기록 보여주는 함수 readDetail()호출
            check_2.setOnClickListener {
                readDetail(calday!!)
            }
        }else if(sevenDayChecks_datepicker[1] == false){
            check_2.setImageResource(R.drawable.check_no_record)
            //기록하지 않은 날짜 새싹 클릭 시 토스트 메세지 띄움
            check_2.setOnClickListener{
                noRecordToast.show()
            }
        }

        if(sevenDayChecks_datepicker[2] == null) {
            check_3.setImageResource(R.drawable.check_empty_record)
        }else if(sevenDayChecks_datepicker[2] == true) {
            check_3.setImageResource(R.drawable.tabbar_icon_overcome)
            //새싹 이미지 클릭 시 자세한 기록 보여주는 함수 readDetail()호출
            check_3.setOnClickListener {
                readDetail(calday!!)
            }
        }else if(sevenDayChecks_datepicker[2] == false){
            check_3.setImageResource(R.drawable.check_no_record)
            //기록하지 않은 날짜 새싹 클릭 시 토스트 메세지 띄움
            check_3.setOnClickListener{
                noRecordToast.show()
            }
        }

        if(sevenDayChecks_datepicker[3] == null) {
            check_4.setImageResource(R.drawable.check_empty_record)
        }else if(sevenDayChecks_datepicker[3] == true) {
            check_4.setImageResource(R.drawable.tabbar_icon_overcome)
            //새싹 이미지 클릭 시 자세한 기록 보여주는 함수 readDetail()호출
            check_4.setOnClickListener {
                readDetail(calday!!)
            }
        }else if(sevenDayChecks_datepicker[3] == false){
            check_4.setImageResource(R.drawable.check_no_record)
            //기록하지 않은 날짜 새싹 클릭 시 토스트 메세지 띄움
            check_4.setOnClickListener{
                noRecordToast.show()
            }
        }

        if(sevenDayChecks_datepicker[4] == null) {
            check_5.setImageResource(R.drawable.check_empty_record)
        }else if(sevenDayChecks_datepicker[4] == true) {
            check_5.setImageResource(R.drawable.tabbar_icon_overcome)
            //새싹 이미지 클릭 시 자세한 기록 보여주는 함수 readDetail()호출
            check_5.setOnClickListener {
                readDetail(calday!!)
            }
        }else if(sevenDayChecks_datepicker[4] == false){
            check_5.setImageResource(R.drawable.check_no_record)
            //기록하지 않은 날짜 새싹 클릭 시 토스트 메세지 띄움
            check_5.setOnClickListener{
                noRecordToast.show()
            }
        }

        if(sevenDayChecks_datepicker[5] == null) {
            check_6.setImageResource(R.drawable.check_empty_record)
        }else if(sevenDayChecks_datepicker[5] == true) {
            check_6.setImageResource(R.drawable.tabbar_icon_overcome)
            //새싹 이미지 클릭 시 자세한 기록 보여주는 함수 readDetail()호출
            check_6.setOnClickListener {
                readDetail(calday!!)
            }
        }else if(sevenDayChecks_datepicker[5] == false){
            check_6.setImageResource(R.drawable.check_no_record)
            //기록하지 않은 날짜 새싹 클릭 시 토스트 메세지 띄움
            check_6.setOnClickListener{
                noRecordToast.show()
            }
        }

        if(sevenDayChecks_datepicker[6] == null) {
            check_7.setImageResource(R.drawable.check_empty_record)
        }else if(sevenDayChecks_datepicker[6] == true) {
            check_7.setImageResource(R.drawable.tabbar_icon_overcome)
            //새싹 이미지 클릭 시 자세한 기록 보여주는 함수 readDetail()호출
            check_7.setOnClickListener {
                readDetail(calday!!)
            }
        }else if(sevenDayChecks_datepicker[6] == false){
            check_7.setImageResource(R.drawable.check_no_record)
            //기록하지 않은 날짜 새싹 클릭 시 토스트 메세지 띄움
            check_7.setOnClickListener{
                noRecordToast.show()
            }
        }

    }
}