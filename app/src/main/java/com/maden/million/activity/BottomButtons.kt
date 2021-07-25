package com.maden.million.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.maden.million.R
import com.maden.million.databinding.FragmentBottomButtonsBinding
import com.maden.million.view.*
import kotlinx.android.synthetic.main.fragment_bottom_buttons.*


class BottomButtons : Fragment() {

    private var _binding: FragmentBottomButtonsBinding? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBottomButtonsBinding
            .inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bottomMessageButton.setOnClickListener {
            navMessage()
        }

        binding.bottomUserProfileButton.setOnClickListener {
            navUserProfile()
        }

        binding.bottomNewUserButton.setOnClickListener {
            navNewUser()
        }
    }

    private fun navMessage() {

        if (GLOBAL_CURRENT_FRAGMENT != "chat_list") {

            when (GLOBAL_CURRENT_FRAGMENT) {
                "chat" -> {
                    val action = ChatFragmentDirections
                        .actionChatFragmentToChatListFragment()
                    Navigation.findNavController(
                        requireActivity(),
                        R.id.main_fragment_layout
                    )
                        .navigate(action)

                    GLOBAL_CURRENT_FRAGMENT = "chat_list"
                    navMessageIcon()

                }
                "profile" -> {

                    val action = ProfileFragmentDirections.actionProfileFragmentToChatListFragment()

                    Navigation.findNavController(
                        requireActivity(),
                        R.id.main_fragment_layout
                    )
                        .navigate(action)

                    GLOBAL_CURRENT_FRAGMENT = "chat_list"
                    navMessageIcon()
                }
                "other_profile" -> {

                    val action =
                        OtherProfileFragmentDirections
                            .actionOtherProfileFragmentToChatListFragment()

                    Navigation.findNavController(
                        requireActivity(),
                        R.id.main_fragment_layout
                    )
                        .navigate(action)

                    GLOBAL_CURRENT_FRAGMENT = "chat_list"
                    navMessageIcon()
                }
                "new_user" -> {
                    val action = GetNewUserFragmentDirections
                        .actionGetNewUserFragmentToChatListFragment()

                    Navigation.findNavController(
                        requireActivity(),
                        R.id.main_fragment_layout
                    )
                        .navigate(action)

                    GLOBAL_CURRENT_FRAGMENT = "chat_list"
                    navMessageIcon()
                }
                "edit_profile" -> {
                    val action = EditProfileFragmentDirections
                        .actionEditProfileFragmentToChatListFragment()

                    Navigation.findNavController(
                        requireActivity(),
                        R.id.main_fragment_layout
                    )
                        .navigate(action)

                    GLOBAL_CURRENT_FRAGMENT = "chat_list"
                    navMessageIcon()
                }
            }

        }
    }

     fun navUserProfile() {
        if (GLOBAL_CURRENT_FRAGMENT != "profile") {
            when (GLOBAL_CURRENT_FRAGMENT) {
                "chat" -> {
                    val action = ChatFragmentDirections
                        .actionChatFragmentToProfileFragment()
                    Navigation.findNavController(
                        requireActivity(),
                        R.id.main_fragment_layout
                    )
                        .navigate(action)

                    GLOBAL_CURRENT_FRAGMENT = "profile"
                    navProfileIcon()

                }
                "chat_list" -> {

                    val action =
                        ChatListFragmentDirections.actionChatListFragmentToProfileFragment()

                    Navigation.findNavController(
                        requireActivity(),
                        R.id.main_fragment_layout
                    )
                        .navigate(action)

                    GLOBAL_CURRENT_FRAGMENT = "profile"
                    navProfileIcon()

                }

                "other_profile" -> {
                    val action =
                        OtherProfileFragmentDirections
                            .actionOtherProfileFragmentToProfileFragment()

                    Navigation.findNavController(
                        requireActivity(),
                        R.id.main_fragment_layout
                    )
                        .navigate(action)

                    GLOBAL_CURRENT_FRAGMENT = "profile"
                    navProfileIcon()
                }
                "new_user" -> {
                    val action = GetNewUserFragmentDirections
                        .actionGetNewUserFragmentToProfileFragment()

                    Navigation.findNavController(
                        requireActivity(),
                        R.id.main_fragment_layout
                    )
                        .navigate(action)

                    GLOBAL_CURRENT_FRAGMENT = "profile"
                    navProfileIcon()

                }
                "edit_profile" -> {
                    val action = EditProfileFragmentDirections
                        .actionEditProfileFragmentToProfileFragment()

                    Navigation.findNavController(
                        requireActivity(),
                        R.id.main_fragment_layout
                    )
                        .navigate(action)

                    GLOBAL_CURRENT_FRAGMENT = "profile"
                    navProfileIcon()
                }
            }
        }
    }

    private fun navNewUser() {
        if (GLOBAL_CURRENT_FRAGMENT != "new_user") {
            when (GLOBAL_CURRENT_FRAGMENT) {
                "chat" -> {
                    val action = ChatFragmentDirections
                        .actionChatFragmentToGetNewUserFragment()
                    Navigation.findNavController(
                        requireActivity(),
                        R.id.main_fragment_layout
                    )
                        .navigate(action)

                    GLOBAL_CURRENT_FRAGMENT = "new_user"
                    navNewUserIcon()

                }
                "chat_list" -> {

                    val action =
                        ChatListFragmentDirections.actionChatListFragmentToGetNewUserFragment()

                    Navigation.findNavController(
                        requireActivity(),
                        R.id.main_fragment_layout
                    )
                        .navigate(action)

                    GLOBAL_CURRENT_FRAGMENT = "new_user"
                    navNewUserIcon()
                }

                "other_profile" -> {
                    val action =
                        OtherProfileFragmentDirections
                            .actionOtherProfileFragmentToGetNewUserFragment()

                    Navigation.findNavController(
                        requireActivity(),
                        R.id.main_fragment_layout
                    )
                        .navigate(action)

                    GLOBAL_CURRENT_FRAGMENT = "new_user"
                    navNewUserIcon()
                }


                "profile" -> {
                    val action = ProfileFragmentDirections
                        .actionProfileFragmentToGetNewUserFragment()

                    Navigation.findNavController(
                        requireActivity(),
                        R.id.main_fragment_layout
                    )
                        .navigate(action)

                    GLOBAL_CURRENT_FRAGMENT = "new_user"
                    navNewUserIcon()

                }
                "edit_profile" -> {
                    val action = EditProfileFragmentDirections
                        .actionEditProfileFragmentToGetNewUserFragment()

                    Navigation.findNavController(
                        requireActivity(),
                        R.id.main_fragment_layout
                    )
                        .navigate(action)

                    GLOBAL_CURRENT_FRAGMENT = "new_user"
                    navNewUserIcon()
                }
            }
        }
    }


    fun navMessageIcon(){
        binding.bottomMessageButton.setImageResource(R.drawable.ic_bottom_chat_full)
        binding.bottomNewUserButton.setImageResource(R.drawable.ic_bottom_plane)
        binding.bottomUserProfileButton.setImageResource(R.drawable.ic_bottom_user)
    }

    private fun navNewUserIcon(){
        binding.bottomMessageButton.setImageResource(R.drawable.ic_bottom_chat)
        binding.bottomNewUserButton.setImageResource(R.drawable.ic_bottom_plane_full)
        binding.bottomUserProfileButton.setImageResource(R.drawable.ic_bottom_user)
    }

    private fun navProfileIcon(){
        binding.bottomMessageButton.setImageResource(R.drawable.ic_bottom_chat)
        binding.bottomNewUserButton.setImageResource(R.drawable.ic_bottom_plane)
        binding.bottomUserProfileButton.setImageResource(R.drawable.ic_bottom_user_full)
    }
}
