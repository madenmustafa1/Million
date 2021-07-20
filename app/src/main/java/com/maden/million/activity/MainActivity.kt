package com.maden.million.activity


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.maden.million.R
import com.maden.million.util.OneSignalAppID
import com.onesignal.OneSignal
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.appbar_chat_list.toolbar

var GLOBAL_CURRENT_FRAGMENT: String? = null
//const val ONESIGNAL_APP_ID = "b94353b0-4a73-435e-926a-793397711fa7"

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        GLOBAL_CURRENT_FRAGMENT = "chat_list"
        bottomFragment()
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP


        setSupportActionBar(toolbar)

        nav_menu_view.bringToFront()
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close)

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        nav_menu_view.setNavigationItemSelectedListener(this)



        // Logging set to help debug issues, remove before releasing your app.
        //OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)
        // OneSignal Initialization
        OneSignal.initWithContext(this)
        OneSignal.setAppId(OneSignalAppID.appID)


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

    override fun onBackPressed() {

        if(drawerLayout.isDrawerOpen(GravityCompat.END)){
            drawerLayout.closeDrawer(GravityCompat.END)
        } else {
            super.onBackPressed()
        }
    }
    val bottomButtons: BottomButtons = BottomButtons()
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.nav_profile_drawer){
            Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show()

            bottomButtons.navUserProfile()

        } else if (item.itemId == R.id.nav_sign_out_drawer){
            Firebase.auth.signOut()
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        drawerLayout.closeDrawer(GravityCompat.END)
        return true
    }
}



//val menu = nav_menu_view.menu
//menu.findItem(R.id.nav_profile).isVisible = false