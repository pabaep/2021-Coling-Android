package com.example.coling

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_log_in.*

class LogInActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val TAG : String = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        auth = FirebaseAuth.getInstance()

        val email = findViewById<EditText>(R.id.login_email_txt)
        val password = findViewById<EditText>(R.id.login_pw_txt)

        move_to_signup.setOnClickListener {
            //지금은 단순하게 페이지 넘어가는 것으로 구성, 후에 로그인 구현후 넘어가게 할 것임
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            //signIn()
        }
        login_btn.setOnClickListener {
            if (email.text.toString().length == 0 || password.text.toString().length == 0){
                Toast.makeText(this, "Please enter the email or password", Toast.LENGTH_SHORT).show()
            } else {
                auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success")
                            val user = auth.currentUser
                            updateUI(user)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.exception)
                            Toast.makeText(
                                baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT
                            ).show()
                            updateUI(null)
                        }

                        // ...
                    }
            }
        }

    }

    override fun onStart() {
        super.onStart()
        //앱 시작 단계에서 사용자가 현재 로그인 되어 있는지 확인하고 처리 해 준다.
        val currentUser = auth?.currentUser
        //updateUI(currentUser) //이건 원하는대로 사용자 설정해 주는 부분인듯 하다.
    }

    fun updateUI(cUser : FirebaseUser? = null){
        if(cUser != null) {
            Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        } else {
            Toast.makeText(this, "Login Required", Toast.LENGTH_SHORT).show()

        }
        login_email_txt.setText("")
        login_pw_txt.setText("")
    }


}