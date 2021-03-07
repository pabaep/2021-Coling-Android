package com.example.coling

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_overcome.*

class OvercomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_overcome, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //음악 버튼
        cl_music.setOnClickListener {
            val intent = Intent(activity, OvercomeMusicActivity::class.java)
            startActivity(intent)
        }

        // 자가진단테스트 버튼
        cl_test.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://nct.go.kr/distMental/rating/rating02_2.do"))
            view.context.startActivity(intent)
        }

        // 전화연결 버튼
        cl_call.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:010-1234-5678"))
            startActivity(intent)
        }
    }
}