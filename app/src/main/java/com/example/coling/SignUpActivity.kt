package com.example.coling

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.example.coling.model.ModelDayCheck
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val TAG : String = "CreateAccount"
    var firestore :FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        auth = FirebaseAuth.getInstance()

        val email = findViewById<EditText>(R.id.email_txt)
        val password = findViewById<EditText>(R.id.pw_txt)

        val vaildEmail = Regex("^[a-zA-Z0-9]([-_.]?[0-9a-zA-Z])@[a-zA-Z0-9]([-_.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,3}")
        val email_pattern = android.util.Patterns.EMAIL_ADDRESS;

        signUp_btn.setOnClickListener {
            if (email.text.toString().length == 0 || password.text.toString().length == 0){
                Toast.makeText(this, "email 혹은 password를 반드시 입력하세요.", Toast.LENGTH_SHORT).show()
            }
            else if(password.text.toString().length < 6){
                Toast.makeText(this, "password는 6자리 이상이어야 합니다.", Toast.LENGTH_SHORT).show()
            }
            else if(!email_pattern.matcher(email.text.toString()).matches()){
                Toast.makeText(this, "올바른 email 형식이 아닙니다.", Toast.LENGTH_SHORT).show()
            }
            else {
                auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success")
                            val user = auth.currentUser
                            //updateUI(user)
                            // 아니면 액티비티를 닫아 버린다.
                            finish()
                            //overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit)
                            createDayCheck()
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(
                                baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT
                            ).show()
                            //updateUI(null)
                            //입력필드 초기화
                            email?.setText("")
                            password?.setText("")
                            email.requestFocus()
                        }
                    }
            }
        }
    }

    fun createDayCheck() {
        for (i in 1..100) {
            var ModelDayChecks = ModelDayCheck()
            ModelDayChecks.day = i
            ModelDayChecks.day_check = false

            firestore?.collection("day_checks")?.document("day_check_${auth?.currentUser?.uid}")?.collection("${i}")?.document()?.set(ModelDayChecks)
            Log.d("${i}번째 dayCheck","생성됨 ${ModelDayChecks.day} , ${ModelDayChecks.day_check}")
        }
    }

}