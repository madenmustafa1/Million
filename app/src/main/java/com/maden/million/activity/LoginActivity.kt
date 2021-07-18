package com.maden.million.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.maden.million.R

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth
        val currentUser = auth.currentUser

        supportActionBar?.hide()

        if (currentUser != null) {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}