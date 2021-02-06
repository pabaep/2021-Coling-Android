package com.example.coling

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener
{
     override fun onNavigationItemSelected(p0: MenuItem): Boolean {
         when(p0.itemId){
             R.id.menu_record ->{
                 val transaction = supportFragmentManager.beginTransaction()
                 transaction.replace(R.id.frame_layout,MainScreenFragment())
                 transaction.commit()
                 return true
             }
             R.id.menu_history ->{
                 val transaction = supportFragmentManager.beginTransaction()
                 transaction.replace(R.id.frame_layout, HistoryFragment())
                 transaction.commit()
                 return true
             }
             R.id.menu_overcome ->{
                 val transaction = supportFragmentManager.beginTransaction()
                 transaction.replace(R.id.frame_layout, OvercomeFragment())
                 transaction.commit()
                 return true
             }
             R.id.menu_setting ->{
                 val transaction = supportFragmentManager.beginTransaction()
                 transaction.replace(R.id.frame_layout, SettingFragment())
                 transaction.commit()
                 return true
             }
         }
         return false
     }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main_bottom_navigation.setOnNavigationItemSelectedListener(this)
        //디폴트 선택으로 메인화면 넣음
        main_bottom_navigation.selectedItemId = R.id.menu_record

    }


 }

