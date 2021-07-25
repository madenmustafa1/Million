package com.maden.million.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.maden.million.R
import com.maden.million.activity.GLOBAL_CURRENT_FRAGMENT
import com.maden.million.databinding.FragmentEditProfileBinding
import com.maden.million.viewmodel.EditProfileViewModel
import kotlinx.android.synthetic.main.appbar_edit_profile.*


class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditProfileBinding
            .inflate(inflater, container, false)
        return binding.root
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    var facebook: String? = null
    var twitter: String? = null
    var instagram: String? = null
    var aboutMe: String? = null

    private lateinit var editProfileViewModel: EditProfileViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        GLOBAL_CURRENT_FRAGMENT = "edit_profile"

        goBackButtonEditProfile.setOnClickListener {
            goToProfile()
        }

        arguments?.let {
            facebook = EditProfileFragmentArgs.fromBundle(it).facebook
            instagram = EditProfileFragmentArgs.fromBundle(it).userInstagram
            twitter = EditProfileFragmentArgs.fromBundle(it).twitter
            aboutMe = EditProfileFragmentArgs.fromBundle(it).aboutMe
            setText()

        }

        editProfileViewModel = ViewModelProvider(this)
            .get(EditProfileViewModel::class.java)


        binding.editProfileFinishButton.setOnClickListener {
            goToProfile()
        }

        binding.editProfileAboutMeButton.setOnClickListener {
            val currentValue = binding.editProfileAboutMe.text.toString()
            if(aboutMe != null) {
                editProfileViewModel.editAboutMe(aboutMe!!, currentValue)
            }
        }

        binding.editProfileFacebookButton.setOnClickListener {

            val currentValue = binding.editProfileFacebook.text.toString()
            if(facebook != null) {
                editProfileViewModel.editFacebook(facebook!!, currentValue)
            }
        }

        binding.editProfileInstagramButton.setOnClickListener {

            val currentValue = binding.editProfileInstagram.text.toString()
            if(instagram != null) {
                editProfileViewModel.editInstagram(instagram!!, currentValue)
            }
        }

        binding.editProfileTwitterButton.setOnClickListener {
            val currentValue = binding.editProfileTwitter.text.toString()
            if(twitter != null) {
                editProfileViewModel.editTwitter(twitter!!, currentValue)
            }
        }
    }

    private fun setText(){
        binding.editProfileInstagram.setText(instagram)
        binding.editProfileTwitter.setText(twitter)
        binding.editProfileFacebook.setText(facebook)
        binding.editProfileAboutMe.setText(aboutMe)
    }

    private fun goToProfile(){
        val action = EditProfileFragmentDirections
            .actionEditProfileFragmentToProfileFragment()
        Navigation.findNavController(
            requireActivity(),
            R.id.main_fragment_layout
        )
            .navigate(action)
        GLOBAL_CURRENT_FRAGMENT = "profile"
    }
}