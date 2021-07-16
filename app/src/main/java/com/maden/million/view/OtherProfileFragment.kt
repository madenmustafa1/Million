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

    var likeControl: Boolean? = false
    var dislikeControl: Boolean? = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        GLOBAL_CURRENT_FRAGMENT = "other_profile"

        otherProfileViewModel = ViewModelProvider(this)
            .get(OtherProfileViewModel::class.java)

        arguments?.let {
            otherUserEmail = OtherProfileFragmentArgs.fromBundle(it).otherUserEmail
            if (otherUserEmail != null && otherUserEmail != "") {
                otherProfileViewModel.getOtherUserProfile(otherUserEmail!!)
                otherProfileViewModel.likeAndDislikeControl(otherUserEmail!!)
            }
        }

        observeMyProfileData()

        otherProfileInstagramIcon.setOnClickListener { goToMyInstagram() }
        otherProfileTwitterIcon.setOnClickListener { goToMyTwitter() }
        otherProfileFacebookIcon.setOnClickListener { goToMyFacebook() }

        otherUserLikeIcon.setOnClickListener { likeControlFun() }
        otherUserDislikeIcon.setOnClickListener { dislikeControlFun()}

    }

    private fun observeMyProfileData() {
        otherProfileViewModel.otherUserProfileData.observe(viewLifecycleOwner, Observer {
            it?.let {
                otherUserNameSurname.text = it[0].userNameSurname
                otherUsername.text = "#"+it[0].username
                otherUserAboutMe.setText(it[0].aboutMe)

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

        otherProfileViewModel.otherUserLikeControl.observe(viewLifecycleOwner, Observer {
            //Like control

            likeControl = if (it){
                otherUserLikeIcon.setAlpha(127)
                otherUserDislikeIcon.setAlpha(255)

                true
            } else { false }
        })

        otherProfileViewModel.otherUserDislikeControl.observe(viewLifecycleOwner, Observer {
            //Dislike control

            dislikeControl = if(it){
                otherUserDislikeIcon.setAlpha(127)
                otherUserLikeIcon.setAlpha(255)

                true
            } else { false }
        })

        otherProfileViewModel.otherUserLikeSize.observe(viewLifecycleOwner, Observer {
            it?.let {
                otherUserLikeText.text = it
            }
        })

        otherProfileViewModel.otherUserDislikeSize.observe(viewLifecycleOwner, Observer {
            it?.let {
                otherUserDisLikeText.text = it
            }
        })

    }

    private fun likeControlFun(){
        if (likeControl == true){
            otherUserLikeIcon.setAlpha(255)
            likeControl = false
        } else if(likeControl == false){
            otherUserLikeIcon.setAlpha(127)
            likeControl = true
        }

        if (otherUserEmail != null && otherUserEmail != ""){
            otherProfileViewModel.otherUserProfileLike(otherUserEmail!!)
        }
    }
    private fun dislikeControlFun(){
        if (dislikeControl == true){
            otherUserDislikeIcon.setAlpha(255)
            dislikeControl = false
        } else if(dislikeControl == false){
            otherUserDislikeIcon.setAlpha(127)
            dislikeControl = true
        }

        if (otherUserEmail != null && otherUserEmail != ""){
            otherProfileViewModel.otherUserProfileDislike(otherUserEmail!!)
        }
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