package com.example.coling

import android.app.Activity
import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.example.coling.model.ModelDayCheck
import com.example.coling.model.ModelRecords
import com.example.coling.model.ModelUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kotlinx.android.synthetic.main.activity_act_record.*
import java.io.File
import java.io.IOException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class ActRecordActivity : AppCompatActivity() {
    val REQUEST_TAKE_PHOTO = 1
    lateinit var currentPhotoPath: String
    private val TAG : String = "getUserInfo"
    var auth : FirebaseAuth? = null
    var firestore : FirebaseFirestore? = null
    var emoString : String = ""
    var startDate : Long? = null
    var day : Long = 0
    var ModelUser = ModelUser()
    var date_lbl :String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_act_record)
        val df: DateFormat = SimpleDateFormat("MM.dd.yyyy")
        val str = df.format(System.currentTimeMillis()) //일단은 수정 고려 안하고 현재 시간 집어 넣음
        textView2.text = str
        settingPermission() //권한 설정

        back_btn.setOnClickListener {
            finish()
        }

        act_record_emotion_img.setOnClickListener {
            val intent = Intent(this, SelectEmotionActivity::class.java)
            startActivityForResult(intent,100)
        }

        act_record_photo_img.setOnClickListener {
            //카메라 사진찍기 구현
            startCapture()
        }

        act_record_save_btn.setOnClickListener {
            //val intent = Intent(this, MainActivity::class.java)//기록 보기 화면으로 추후 전환
            //startActivity(intent)
            contentUpload()


        }

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()


        /*if(emoString.isNotEmpty()){
            findEmo()
        }*/

    }

    fun contentUpload(){
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        getStartDate()



        finish()
    }

    //StartDate를 받아오는 함수
    fun getStartDate(){
        firestore?.collection("Users")
            ?.whereEqualTo("uid", auth?.currentUser?.uid)
            ?.get()
            ?.addOnSuccessListener { documents ->
                for (document in documents) {
                    //Log.d("로그3-1--", "${document.id} => ${document.data}")
                    //Log.d("로그-3-2-특정 key값만", document.data["start_date"].toString())
                    startDate = document.data["start_date"] as Long
                    Log.d("로그-4-0--",startDate.toString()+"나오나요...")
                    break
                }
                Log.d("로그-4-1--",startDate.toString()+"나오나요...")
                if(startDate == null){
                    ModelUser.uid = auth?.currentUser?.uid
                    ModelUser.start_date = System.currentTimeMillis()
                    //user데이터 업로드
                    day = 1
                    firestore?.collection("Users")?.document("user_${auth?.currentUser?.uid}")?.set(ModelUser)
                }
                //else는 들어가는데 정보 가져오는 건 안됨
                //day 다시 계산하기 - day를 long으로 정의?
                else {
                    //여기가 임의로 미래 날짜 지정하는 부분임
                    /*val nowDate = Calendar.getInstance().apply {
                        set(Calendar.YEAR, 2021)
                        set(Calendar.MONTH, 2)  // 얘가 0이 1월 달임
                        set(Calendar.DATE, 21)
                    }.timeInMillis*/
                    val nowDate = System.currentTimeMillis()
                    Log.d("로그-5-0--",nowDate.toString())

                    day = (nowDate - startDate!!)/(24*60*60*1000) + 1

                }
                //레코드 업로드 부분
                var ModelRecords = ModelRecords()
                ModelRecords.img_src = currentPhotoPath
                ModelRecords.uid = auth?.currentUser?.uid
                ModelRecords.diary = act_record_short_story.text.toString()
                ModelRecords.date = System.currentTimeMillis()
                ModelRecords.act_name = intent.getStringExtra("act_name")
                ModelRecords.act_content = intent.getStringExtra("act_content")
                ModelRecords.day = day
                ModelRecords.emo = emoString
                //레코드 업로드
                firestore?.collection("Records")?.document("record_${auth?.currentUser?.uid}")?.collection("$day")?.document()?.set(ModelRecords)

                var ModelDayCheck = ModelDayCheck()
                ModelDayCheck.day_check = true

                firestore?.collection("day_checks")?.document("day_check_${auth?.currentUser?.uid}")?.collection("day")?.document("day${day}")?.set(ModelDayCheck)


            }
            ?.addOnFailureListener{
                Log.d("로그-3-3--","실패 . . . . ")
            }
    }

    //시간, 분, 초, 밀리초 제외시키기
    fun getIgnoredTimeDays(time : Long): Long{
        return Calendar.getInstance().apply {
            timeInMillis = time
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    fun settingPermission(){
        var permis = object  : PermissionListener {
            //            어떠한 형식을 상속받는 익명 클래스의 객체를 생성하기 위해 다음과 같이 작성
            override fun onPermissionGranted() {
                Toast.makeText(this@ActRecordActivity, "Authorized", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Toast.makeText(this@ActRecordActivity, "Permission Denied", Toast.LENGTH_SHORT)
                    .show()
                ActivityCompat.finishAffinity(this@ActRecordActivity) // 권한 거부시 앱 종료
            }
        }

        TedPermission.with(this)
            .setPermissionListener(permis)
            .setRationaleMessage("Requires camera picture permission")
            .setDeniedMessage("Reject Camera Permission Request")
            .setPermissions(
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA)
            .check()
    }

    @Throws(IOException::class)
    private fun createImageFile() : File {
        val timeStamp : String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir : File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply{
            currentPhotoPath = absolutePath
        }
    }

    fun startCapture(){
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try{
                    createImageFile()
                }catch(ex:IOException){
                    null
                }
                photoFile?.also{
                    val photoURI : Uri = FileProvider.getUriForFile(
                        this,
                        "com.example.coling",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                }
            }
        }
    }
    private fun galleryAddPic() {
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
            val f = File(currentPhotoPath)
            mediaScanIntent.data = Uri.fromFile(f)
            sendBroadcast(mediaScanIntent)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK){
            val file = File(currentPhotoPath)
            if (Build.VERSION.SDK_INT < 28) {
                val bitmap = MediaStore.Images.Media
                    .getBitmap(contentResolver, Uri.fromFile(file))
                act_record_photo_img.setImageBitmap(bitmap)
            }
            else{
                val decode = ImageDecoder.createSource(this.contentResolver,
                    Uri.fromFile(file))
                val bitmap = ImageDecoder.decodeBitmap(decode)
                act_record_photo_img.setImageBitmap(bitmap)
            }
        }
        else if(requestCode == 100 && resultCode == Activity.RESULT_OK){
            emoString = data!!.getStringExtra("emo_select_string").toString()
            findEmo()
        }
    }
    fun findEmo(){
        if(emoString == "emoji_1"){
            val drawable = getDrawable(R.drawable.emoji_1)
            act_record_emotion_img.setImageDrawable(drawable)
        }
        else if(emoString == "emoji_2"){
            val drawable = getDrawable(R.drawable.emoji_2)
            act_record_emotion_img.setImageDrawable(drawable)
        }
        else if(emoString == "emoji_3"){
            val drawable = getDrawable(R.drawable.emoji_3)
            act_record_emotion_img.setImageDrawable(drawable)
        }
        else if(emoString == "emoji_4"){
            val drawable = getDrawable(R.drawable.emoji_4)
            act_record_emotion_img.setImageDrawable(drawable)
        }
        else if(emoString == "emoji_5"){
            val drawable = getDrawable(R.drawable.emoji_5)
            act_record_emotion_img.setImageDrawable(drawable)
        }
        else if(emoString == "emoji_6"){
            val drawable = getDrawable(R.drawable.emoji_6)
            act_record_emotion_img.setImageDrawable(drawable)
        }
        else if(emoString == "emoji_7"){
            val drawable = getDrawable(R.drawable.emoji_7)
            act_record_emotion_img.setImageDrawable(drawable)
        }
        else if(emoString == "emoji_8"){
            val drawable = getDrawable(R.drawable.emoji_8)
            act_record_emotion_img.setImageDrawable(drawable)
        }
        else if(emoString == "emoji_9"){
            val drawable = getDrawable(R.drawable.emoji_9)
            act_record_emotion_img.setImageDrawable(drawable)
        }
        else if(emoString == "emoji_10"){
            val drawable = getDrawable(R.drawable.emoji_10)
            act_record_emotion_img.setImageDrawable(drawable)
        }
        else if(emoString == "emoji_11"){
            val drawable = getDrawable(R.drawable.emoji_11)
            act_record_emotion_img.setImageDrawable(drawable)
        }
    }
}
