package com.example.coling

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val TAG : String = "CreateAccount"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        auth = FirebaseAuth.getInstance()

        val email = findViewById<EditText>(R.id.email_txt)
        val password = findViewById<EditText>(R.id.pw_txt)

        val vaildEmail = Regex("^[a-zA-X0-9]([-_.]?[0-9a-zA-Z])@[a-zA-Z0-9]([-_.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,3}")

        signUp_btn.setOnClickListener {
            if (email.text.toString().length == 0 || password.text.toString().length == 0){
                Toast.makeText(this, "email 혹은 password를 반드시 입력하세요.", Toast.LENGTH_SHORT).show()
            }
            else if(password.text.toString().length < 6){
                Toast.makeText(this, "password는 6자리 이상이어야 합니다.", Toast.LENGTH_SHORT).show()
            }
            else if(!email.text.toString().matches(vaildEmail)){
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
}