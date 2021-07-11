package com.maden.million.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.maden.million.R
import com.maden.million.activity.GLOBAL_CURRENT_FRAGMENT
import com.maden.million.util.downloadPhoto
import com.maden.million.viewmodel.OtherProfileViewModel
import kotlinx.android.synthetic.main.fragment_other_profile.*
import kotlinx.android.synthetic.main.fragment_profile.*


class OtherProfileFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_other_profile, container, false)
    }


    private lateinit var otherProfileViewModel: OtherProfileViewModel
    var otherUserEmail: String? = null
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        GLOBAL_CURRENT_FRAGMENT = "other_profile"

        otherProfileViewModel = ViewModelProvider(this)
            .get(OtherProfileViewModel::class.java)

        arguments?.let {
            otherUserEmail = OtherProfileFragmentArgs.fromBundle(it).otherUserEmail
            if (otherUserEmail != null && otherUserEmail != "") {
                otherProfileViewModel.getOtherUserProfile(otherUserEmail!!)
            }
        }

        observeMyProfileData()

        otherProfileInstagramIcon.setOnClickListener { goToMyInstagram() }
        otherProfileTwitterIcon.setOnClickListener { goToMyTwitter() }
        otherProfileFacebookIcon.setOnClickListener { goToMyFacebook() }
    }

    private fun observeMyProfileData() {
        otherProfileViewModel.otherUserProfileData.observe(viewLifecycleOwner, Observer {
            it?.let {
                otherUserNameSurname.text = it[0].userNameSurname
                otherUsername.text = "#"+it[0].username
                otherUserAboutMe.setText(it[0].aboutMe)
                otherUserLikeText.text = it[0].like
                otherUserDisLikeText.text = it[0].dislike
                instagram = it[0].instagram
                facebook = it[0].facebook
                twitter = it[0].twitter
            }
        })

        otherProfileViewModel.otherUserProfilePhoto.observe(viewLifecycleOwner, Observer {
            it?.let {
                otherUserProfilePhoto.downloadPhoto(it)
            }
        })
    }





    private var instagram: String? = null
    private var facebook: String? = null
    private var twitter: String? = null
    val intent = Intent()

    private fun goToMyInstagram(){
        if(instagram != null && instagram != ""){
            intent.action = Intent.ACTION_VIEW
            intent.addCategory(Intent.CATEGORY_BROWSABLE)
            intent.data = Uri.parse("https://www.instagram.com/$instagram")
            startActivity(intent)
        } else {
            Toast.makeText(context,
                "Instagram adresi bulunamadı",
                Toast.LENGTH_SHORT).show()
        }

    }

    private fun goToMyTwitter(){
        if(twitter != null && twitter != ""){
            intent.action = Intent.ACTION_VIEW
            intent.addCategory(Intent.CATEGORY_BROWSABLE)
            intent.data = Uri.parse("https://www.twitter.com/$twitter")
            startActivity(intent)
        } else {
            Toast.makeText(context,
                "Twitter adresi bulunamadı",
                Toast.LENGTH_SHORT).show()
        }
    }
    private fun goToMyFacebook(){
        if(facebook != null && facebook != ""){
            intent.action = Intent.ACTION_VIEW
            intent.addCategory(Intent.CATEGORY_BROWSABLE)
            intent.data = Uri.parse("https://www.facebook.com/$facebook")
            startActivity(intent)
        } else {
            Toast.makeText(context,
                "Facebook adresi bulunamadı",
                Toast.LENGTH_SHORT).show()
        }
    }
}