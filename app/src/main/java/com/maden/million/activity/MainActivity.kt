package com.maden.million.activity


import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.maden.million.R
import com.maden.million.util.OneSignalAppID
import com.maden.million.view.BannerFragment
import com.onesignal.OneSignal
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.appbar_chat_list.*



var GLOBAL_CURRENT_FRAGMENT: String? = null

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{

    private var db = Firebase.firestore
    private var auth = Firebase.auth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        GLOBAL_CURRENT_FRAGMENT = "chat_list"
        bottomFragment()
        bottomAdsBanner()
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP


        setSupportActionBar(toolbar)

        //Navigation Drawer
        nav_menu_view.bringToFront()
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close)

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        nav_menu_view.setNavigationItemSelectedListener(this)
        //


        OneSignal.initWithContext(this)
        OneSignal.setAppId(OneSignalAppID.appID)


        authControl()

    }


    private fun authControl(){
        var isActive: Boolean? = null

        val authRef = db.collection("Profile")
            .document(auth.currentUser!!.email!!.toString())

        authRef.get().addOnSuccessListener {
            if (it["isActive"] != null){
                isActive = it["isActive"] as Boolean
                if (isActive == false){
                    deleteUserOneSignal()
                    Toast.makeText(applicationContext,
                        "Hesabınız engellenmiştir",
                        Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.sign_out){
            deleteUserOneSignal()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun bottomAdsBanner() {
        val fragmentBottoms = supportFragmentManager
        val fragmentTransaction = fragmentBottoms.beginTransaction()
        val bottomButtons = BannerFragment()
        fragmentTransaction.replace(R.id.bottom_ads_banner, bottomButtons).commit()
    }

    private fun bottomFragment() {
        val fragmentBottoms = supportFragmentManager
        val fragmentTransaction = fragmentBottoms.beginTransaction()
        val bottomButtons = BottomButtons()
        fragmentTransaction.replace(R.id.bottom_Buttons_Layout, bottomButtons).commit()
    }


    private fun deleteUserOneSignal(){
        val userOneSignalID = db.collection("Profile")
            .document(auth.currentUser!!.email!!.toString())

        userOneSignalID
            .update("oneSignalID", "")
            .addOnCompleteListener {
                finishActivity()
            }
    }

    private fun finishActivity(){
        Firebase.auth.signOut()
        val intent = Intent(applicationContext, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }



    override fun onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.END)){
            drawerLayout.closeDrawer(GravityCompat.END)
        } else {
            super.onBackPressed()
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_info_drawer -> {
                Toast.makeText(this, "Yakında hizmete girecek!",
                    Toast.LENGTH_SHORT).show()

            }
            R.id.nav_contactUs_drawer -> {

                Toast.makeText(this, "Yakında hizmete girecek!",
                    Toast.LENGTH_SHORT).show()

            }
            R.id.nav_premium_drawer -> {

                Toast.makeText(this, "Yakında hizmete girecek!",
                    Toast.LENGTH_SHORT).show()

            }
            R.id.nav_sign_out_drawer -> {
                deleteUserOneSignal()
            }
        }

        drawerLayout.closeDrawer(GravityCompat.END)
        return true
    }
}



//val menu = nav_menu_view.menu
//menu.findItem(R.id.nav_profile).isVisible = false