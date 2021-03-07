package com.example.coling

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.example.coling.model.ModelRecords
import com.google.android.gms.auth.GoogleAuthUtil.getToken
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kotlinx.android.synthetic.main.activity_act_record.*
import java.io.File
import java.io.IOException
import java.lang.Byte.decode
import java.text.SimpleDateFormat
import java.util.*

class ActRecordActivity : AppCompatActivity() {
    val REQUEST_TAKE_PHOTO = 1
    lateinit var currentPhotoPath: String
    var auth : FirebaseAuth? = null
    var firestore : FirebaseFirestore? = null
    var emoString : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_act_record)

        settingPermission() //권한 설정

        back_btn.setOnClickListener {
            finish()
        }

        act_record_emotion_img.setOnClickListener {
            val intent = Intent(this, SelectEmotionActivity::class.java)
            startActivityForResult(intent,100)
            //emoString = intent.getStringExtra("emo_select_string").toString()
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
        var ModelRecords = ModelRecords()
        ModelRecords.img_src = currentPhotoPath
        ModelRecords.uid = auth?.currentUser?.uid
        ModelRecords.diary = act_record_short_story.text.toString()
        ModelRecords.date = System.currentTimeMillis()
        ModelRecords.act_name = intent.getStringExtra("act_name")
        ModelRecords.act_content = intent.getStringExtra("act_content")
        ModelRecords.day = 999
        ModelRecords.emo = emoString

        var day = 999

        firestore?.collection("Records")?.document("record_${auth?.currentUser?.uid}")?.collection("$day")?.document()?.set(ModelRecords)


        finish()
    }



    fun settingPermission(){
        var permis = object  : PermissionListener {
            //            어떠한 형식을 상속받는 익명 클래스의 객체를 생성하기 위해 다음과 같이 작성
            override fun onPermissionGranted() {
                Toast.makeText(this@ActRecordActivity, "권한 허가", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Toast.makeText(this@ActRecordActivity, "권한 거부", Toast.LENGTH_SHORT)
                    .show()
                ActivityCompat.finishAffinity(this@ActRecordActivity) // 권한 거부시 앱 종료
            }
        }

        TedPermission.with(this)
            .setPermissionListener(permis)
            .setRationaleMessage("카메라 사진 권한 필요")
            .setDeniedMessage("카메라 권한 요청 거부")
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
