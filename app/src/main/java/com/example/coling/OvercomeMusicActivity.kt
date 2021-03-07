package com.example.coling

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.coling.R
import kotlinx.android.synthetic.main.activity_overcome_music.*

class OvercomeMusicActivity : AppCompatActivity() {
    var mediaplayer_cleaning : MediaPlayer?= null
    var mediaplayer_coffee : MediaPlayer?= null
    var mediaplayer_cooking : MediaPlayer?= null
    var mediaplayer_drive : MediaPlayer?= null
    var mediaplayer_shopping : MediaPlayer?= null
    var mediaplayer_walk : MediaPlayer?= null
    var mediaplayer_yoga : MediaPlayer?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overcome_music)

        // 뒤로가기 버튼
        btn_music_back.setOnClickListener{
            finish()
        }

        // clean 음악
        btn_play_cleaning.setOnClickListener {
            mediaplayer_cleaning = MediaPlayer.create(this, R.raw.cleaning)
            mediaplayer_cleaning?.start()
            Toast.makeText(this, "start music (Cleaning)", Toast.LENGTH_SHORT).show()
        }

        btn_stop_cleaning.setOnClickListener {
            mediaplayer_cleaning?.stop()
            Toast.makeText(this, "stop music (cleaning)", Toast.LENGTH_SHORT).show()
        }

        // coffee 음악
        btn_play_coffee.setOnClickListener {
            mediaplayer_coffee = MediaPlayer.create(this, R.raw.coffee)
            mediaplayer_coffee?.start()
            Toast.makeText(this, "start music (Coffee)", Toast.LENGTH_SHORT).show()
        }

        btn_stop_coffee.setOnClickListener {
            mediaplayer_coffee?.stop()
            Toast.makeText(this, "stop music (Coffee)", Toast.LENGTH_SHORT).show()
        }

        // cooking 음악
        btn_play_cooking.setOnClickListener {
            mediaplayer_cooking = MediaPlayer.create(this, R.raw.cooking)
            mediaplayer_cooking?.start()
            Toast.makeText(this, "start music (Cooking)", Toast.LENGTH_SHORT).show()
        }

        btn_stop_cooking.setOnClickListener {
            mediaplayer_cooking?.stop()
            Toast.makeText(this, "stop music (Cooking)", Toast.LENGTH_SHORT).show()
        }

        // drive 음악
        btn_play_drive.setOnClickListener {
            mediaplayer_drive = MediaPlayer.create(this, R.raw.drive)
            mediaplayer_drive?.start()
            Toast.makeText(this, "start music (Drive)", Toast.LENGTH_SHORT).show()
        }

        btn_stop_drive.setOnClickListener {
            mediaplayer_drive?.stop()
            Toast.makeText(this, "stop music (Drive)", Toast.LENGTH_SHORT).show()
        }

        // shopping 음악
        btn_play_shopping.setOnClickListener {
            mediaplayer_shopping = MediaPlayer.create(this, R.raw.shopping)
            mediaplayer_shopping?.start()
            Toast.makeText(this, "start music (Shopping)", Toast.LENGTH_SHORT).show()
        }

        btn_stop_shopping.setOnClickListener{
            mediaplayer_shopping?.stop()
            Toast.makeText(this, "stop music (Shopping)", Toast.LENGTH_SHORT).show()
        }

        // walk 음악
        btn_play_walk.setOnClickListener {
            mediaplayer_walk = MediaPlayer.create(this, R.raw.walk)
            mediaplayer_walk?.start()
            Toast.makeText(this, "start music (Walk)", Toast.LENGTH_SHORT).show()
        }

        btn_stop_walk.setOnClickListener {
            mediaplayer_walk?.stop()
            Toast.makeText(this, "stop music (Walking)", Toast.LENGTH_SHORT).show()
        }

        // yoga 음악
        btn_play_yoga.setOnClickListener {
            mediaplayer_yoga = MediaPlayer.create(this, R.raw.yoga)
            mediaplayer_yoga?.start()
            Toast.makeText(this, "start music (Yoga)", Toast.LENGTH_SHORT).show()
        }

        btn_stop_yoga.setOnClickListener {
            mediaplayer_yoga?.stop()
            Toast.makeText(this, "stop music (Yoga)", Toast.LENGTH_SHORT).show()
        }
    }

}