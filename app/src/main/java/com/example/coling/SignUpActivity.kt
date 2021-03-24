package com.example.coling

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.example.coling.model.ModelActCheck
import com.example.coling.model.ModelDayCheck
import com.example.coling.model.ModelUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val TAG : String = "CreateAccount"
    var firestore :FirebaseFirestore = FirebaseFirestore.getInstance()
    var ModelUser = ModelUser()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        auth = FirebaseAuth.getInstance()

        val email = findViewById<EditText>(R.id.email_txt)
        val password = findViewById<EditText>(R.id.pw_txt)
        val passwordCheck = findViewById<EditText>(R.id.pw_check_txt)

        //val vaildEmail = Regex("^[a-zA-Z0-9]([-_.]?[0-9a-zA-Z])@[a-zA-Z0-9]([-_.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,3}")
        val email_pattern = android.util.Patterns.EMAIL_ADDRESS;

        signUp_btn.setOnClickListener {
            if (email.text.toString().length == 0 || password.text.toString().length == 0){
                Toast.makeText(this, "Please enter the email or password", Toast.LENGTH_SHORT).show()
            }
            else if(password.text.toString().length < 6){
                Toast.makeText(this, "Password must be at least 6 digits", Toast.LENGTH_SHORT).show()
            }
            else if(!email_pattern.matcher(email.text.toString()).matches()){
                Toast.makeText(this, "Not a valid email format", Toast.LENGTH_SHORT).show()
            }
            else if(password.text.toString() != passwordCheck.text.toString()){
                Toast.makeText(this, "Please check your password one more time.",Toast.LENGTH_SHORT).show()
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
                            createDayAndActCheck()
                            ModelUser.uid = auth?.currentUser?.uid
                            firestore?.collection("Users")?.document("user_${auth?.currentUser?.uid}")?.set(ModelUser)

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

    fun createDayAndActCheck() {
        for (i in 1..100) {
            var modelDayChecks = ModelDayCheck()
            modelDayChecks.day = i
            modelDayChecks.day_check = null

            var modelActChecks = ModelActCheck()
            modelActChecks.act_id = i
            modelActChecks.act_check = false

            firestore?.collection("day_checks")?.document("day_check_${auth?.currentUser?.uid}")?.collection("day")?.document("day${i}")?.set(modelDayChecks)
            firestore?.collection("act_checks")?.document("act_check_${auth?.currentUser?.uid}")?.collection("act")?.document("act${i}")?.set(modelActChecks)
        }
    }

}