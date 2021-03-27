package com.example.coling

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
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
            Toast.makeText(this, "Cleaning music starts.", Toast.LENGTH_SHORT).show()
            stopPlay()
        }

        btn_stop_cleaning.setOnClickListener {
            mediaplayer_cleaning?.stop()
            Toast.makeText(this, "Cleaning music is turned off.", Toast.LENGTH_SHORT).show()
            startPlay()
        }

        // coffee 음악
        btn_play_coffee.setOnClickListener {
            mediaplayer_coffee = MediaPlayer.create(this, R.raw.coffee)
            mediaplayer_coffee?.start()
            Toast.makeText(this, "Coffee music starts.", Toast.LENGTH_SHORT).show()
            stopPlay()
        }

        btn_stop_coffee.setOnClickListener {
            mediaplayer_coffee?.stop()
            Toast.makeText(this, "Coffee music is turned off.", Toast.LENGTH_SHORT).show()
            startPlay()
        }

        // cooking 음악
        btn_play_cooking.setOnClickListener {
            mediaplayer_cooking = MediaPlayer.create(this, R.raw.cooking)
            mediaplayer_cooking?.start()
            Toast.makeText(this, "Cooking music starts.", Toast.LENGTH_SHORT).show()
            stopPlay()
        }

        btn_stop_cooking.setOnClickListener {
            mediaplayer_cooking?.stop()
            Toast.makeText(this, "Cooking music is turned off.", Toast.LENGTH_SHORT).show()
            startPlay()
        }

        // drive 음악
        btn_play_drive.setOnClickListener {
            mediaplayer_drive = MediaPlayer.create(this, R.raw.drive)
            mediaplayer_drive?.start()
            Toast.makeText(this, "Drive music starts.", Toast.LENGTH_SHORT).show()
            stopPlay()
        }

        btn_stop_drive.setOnClickListener {
            mediaplayer_drive?.stop()
            Toast.makeText(this, "Drive music is turned off.", Toast.LENGTH_SHORT).show()
            startPlay()
        }

        // shopping 음악
        btn_play_shopping.setOnClickListener {
            mediaplayer_shopping = MediaPlayer.create(this, R.raw.shopping)
            mediaplayer_shopping?.start()
            Toast.makeText(this, "Shopping music starts.", Toast.LENGTH_SHORT).show()
            stopPlay()
        }

        btn_stop_shopping.setOnClickListener{
            mediaplayer_shopping?.stop()
            Toast.makeText(this, "Shopping music is turned off.", Toast.LENGTH_SHORT).show()
            startPlay()
        }

        // walk 음악
        btn_play_walk.setOnClickListener {
            mediaplayer_walk = MediaPlayer.create(this, R.raw.walk)
            mediaplayer_walk?.start()
            Toast.makeText(this, "Walking music starts.", Toast.LENGTH_SHORT).show()
            stopPlay()
        }

        btn_stop_walk.setOnClickListener {
            mediaplayer_walk?.stop()
            Toast.makeText(this, "Walking music is turned off.", Toast.LENGTH_SHORT).show()
            startPlay()
        }

        // yoga 음악
        btn_play_yoga.setOnClickListener {
            mediaplayer_yoga = MediaPlayer.create(this, R.raw.yoga)
            mediaplayer_yoga?.start()
            Toast.makeText(this, "Yoga music starts.", Toast.LENGTH_SHORT).show()
            stopPlay()
        }

        btn_stop_yoga.setOnClickListener {
            mediaplayer_yoga?.stop()
            Toast.makeText(this, "Yoga music is turned off.", Toast.LENGTH_SHORT).show()
            startPlay()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaplayer_cleaning?.release()
        mediaplayer_cleaning=null
        mediaplayer_coffee?.release()
        mediaplayer_coffee=null
        mediaplayer_cooking?.release()
        mediaplayer_cooking=null
        mediaplayer_drive?.release()
        mediaplayer_drive=null
        mediaplayer_shopping?.release()
        mediaplayer_shopping=null
        mediaplayer_walk?.release()
        mediaplayer_walk=null
        mediaplayer_yoga?.release()
        mediaplayer_yoga=null
    }

    fun stopPlay(){
        btn_play_cleaning.isEnabled=false
        btn_play_coffee.isEnabled=false
        btn_play_cooking.isEnabled=false
        btn_play_drive.isEnabled=false
        btn_play_shopping.isEnabled=false
        btn_play_walk.isEnabled=false
        btn_play_yoga.isEnabled=false
    }

    fun startPlay(){
        btn_play_cleaning.isEnabled=true
        btn_play_coffee.isEnabled=true
        btn_play_cooking.isEnabled=true
        btn_play_drive.isEnabled=true
        btn_play_shopping.isEnabled=true
        btn_play_walk.isEnabled=true
        btn_play_yoga.isEnabled=true
    }
}