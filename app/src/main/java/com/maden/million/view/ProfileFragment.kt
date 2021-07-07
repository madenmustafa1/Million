package com.maden.million.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.maden.million.R
import com.maden.million.activity.GLOBAL_CURRENT_FRAGMENT
import com.maden.million.viewmodel.ChatViewModel
import com.maden.million.viewmodel.ProfileViewModel
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    private lateinit var profileViewModel: ProfileViewModel
    val intent = Intent()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        GLOBAL_CURRENT_FRAGMENT = "profile"

        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)


        profileInstagramIcon.setOnClickListener { goToMyInstagram() }
        profileTwitterIcon.setOnClickListener { goToMyTwitter() }
        profileFacebookIcon.setOnClickListener { goToMyFacebook() }
    }

    private fun goToMyInstagram(){
        intent.action = Intent.ACTION_VIEW
        intent.addCategory(Intent.CATEGORY_BROWSABLE)
        intent.data = Uri.parse("https://www.instagram.com/mustafamadenx/")
        startActivity(intent)
    }
    private fun goToMyTwitter(){
        intent.action = Intent.ACTION_VIEW
        intent.addCategory(Intent.CATEGORY_BROWSABLE)
        intent.data = Uri.parse("https://www.twitter.com/")
        startActivity(intent)
    }
    private fun goToMyFacebook(){
        intent.action = Intent.ACTION_VIEW
        intent.addCategory(Intent.CATEGORY_BROWSABLE)
        intent.data = Uri.parse("https://www.facebook.com/")
        startActivity(intent)
    }
}