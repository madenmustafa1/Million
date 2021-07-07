package com.maden.million.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.maden.million.R

var GLOBAL_CURRENT_FRAGMENT: String? = null

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        GLOBAL_CURRENT_FRAGMENT = "chat_list"
        bottomFragment()
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.sign_out){
            Firebase.auth.signOut()
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    fun bottomFragment() {
        val fragmentBottoms = supportFragmentManager
        val fragmentTransaction = fragmentBottoms.beginTransaction()
        val bottomButtons = BottomButtons()
        fragmentTransaction.replace(R.id.bottom_Buttons_Layout, bottomButtons).commit()
    }
}