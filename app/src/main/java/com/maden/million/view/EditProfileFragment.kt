package com.maden.million.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.maden.million.R
import com.maden.million.activity.GLOBAL_CURRENT_FRAGMENT
import com.maden.million.databinding.FragmentEditProfileBinding
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        goBackButtonEditProfile.setOnClickListener {
            goToProfile()
        }

        arguments?.let {
            facebook = EditProfileFragmentArgs.fromBundle(it).facebook
            instagram = EditProfileFragmentArgs.fromBundle(it).userInstagram
            twitter = EditProfileFragmentArgs.fromBundle(it).twitter
            aboutMe = EditProfileFragmentArgs.fromBundle(it).aboutMe

            binding.editProfileInstagram.setText(instagram)
            binding.editProfileTwitter.setText(twitter)
            binding.editProfileFacebook.setText(facebook)
            binding.editProfileAboutMe.setText(aboutMe)
        }



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