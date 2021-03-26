package com.example.coling

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_setting.*

class SettingFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        btn_logout.setOnClickListener{
            auth.signOut()
            val intent = Intent(activity, LogInActivity :: class.java )//로그인화면으로 넘어감
            startActivity(intent)
        }

        // 메뉴얼 버튼
        btn_manual.setOnClickListener{
            val intent = Intent(activity, SettingManualActivity::class.java)
            startActivity(intent)
        }

        // 사용자 약관 버튼
        btn_terms.setOnClickListener {
            val intent = Intent(activity, SettingTermsActivity::class.java)
            startActivity(intent)
        }

        // 개인정보 버튼
        btn_privacy.setOnClickListener {
            val intent = Intent(activity, SettingPrivacyActivity::class.java)
            startActivity(intent)
        }

        //FAQ 버튼
        btn_chatbot.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://bot.dialogflow.com/9800e977-c0be-4f36-aa3e-d1d2fe8a1df6"))
            view.context.startActivity(intent)
        }
    }


}