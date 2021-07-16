package com.maden.million.view

import android.animation.ObjectAnimator
import android.graphics.Path
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.maden.million.R
import com.maden.million.activity.GLOBAL_CURRENT_FRAGMENT
import com.maden.million.util.downloadPhoto
import com.maden.million.viewmodel.GetNewUserViewModel
import kotlinx.android.synthetic.main.fragment_get_new_user.*


class GetNewUserFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_get_new_user, container, false)
    }

    private var newUserEmail: String? = null
    private var newUserFullName: String? = null
    private var newUserDownloadPhoto: String = ""
    private var newUserRoomUUID: String? = null

    private lateinit var getNewUserViewModel: GetNewUserViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        GLOBAL_CURRENT_FRAGMENT = "new_user"

        getNewUserViewModel = ViewModelProvider(this)
            .get(GetNewUserViewModel::class.java)

        getNewUserButton.setOnClickListener {
            getNewUserViewModel.getNewUser()
            moveToRocket()
        }


        startChatButton.setOnClickListener {
            startChat(it)
        }

        observeData()
    }


    private fun observeData() {
        getNewUserViewModel.newUserDataClass.observe(viewLifecycleOwner, Observer {
            it?.let {

                newUserProfileInfo.visibility = View.VISIBLE
                getNewUserButton.visibility = View.GONE
                newUserNameSurname.text = it.userNameSurname
                newUserUsername.text = it.username
                newUserUserAboutMe.setText(it.aboutMe)

                newUserEmail = it.email
                newUserFullName = it.userNameSurname

                if (it.photoUrl != "") {
                    newUserProfilePhoto.downloadPhoto(it.photoUrl)
                    newUserDownloadPhoto = it.photoUrl
                }
            }
        })

        getNewUserViewModel.newChatRoomUUID.observe(viewLifecycleOwner, Observer {
            it?.let {
                newUserRoomUUID = it
            }
        })
    }

    private fun startChat(view: View){
        if (newUserEmail != "" && newUserEmail != null &&
            newUserFullName != "" && newUserFullName != null) {

            getNewUserViewModel.startChat(newUserEmail!!, newUserFullName!!)

            if(newUserRoomUUID != "" && newUserRoomUUID != null){


                navToChat(newUserRoomUUID!!, newUserEmail!!,
                    newUserFullName!!, newUserDownloadPhoto!!, view)
            }
        }
    }
    private fun navToChat(uuid: String, otherEmail: String,
                          otherUsername: String,
                          downloadPhotoUrl: String, view: View){

        val action = GetNewUserFragmentDirections
            .actionGetNewUserFragmentToChatFragment(uuid, otherEmail,
                otherUsername, downloadPhotoUrl)

        view.findNavController().navigate(action)
        GLOBAL_CURRENT_FRAGMENT = "chat"
    }

    fun moveToRocket() {
        /*
        val path = Path().apply {
            arcTo(0f, 0f, 1000f, 1000f, testAnim.pivotX, -360f, true)
        }
        val animator = ObjectAnimator.ofFloat(testAnim, View.X, View.Y, path).apply {
            duration = 2000
            start()
        }
         */


        ObjectAnimator.ofFloat(testAnim, "translationX", 3000f).apply {
            duration = 2000
            start()
        }
        ObjectAnimator.ofFloat(testAnim, "translationY", -3000f).apply {
            duration = 2000
            start()

        }
    }
}