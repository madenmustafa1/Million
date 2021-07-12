package com.maden.million.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.maden.million.R
import com.maden.million.view.*
import kotlinx.android.synthetic.main.fragment_bottom_buttons.*
import kotlinx.android.synthetic.main.fragment_bottom_buttons.view.*


class BottomButtons : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_buttons, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomMessageButton.setOnClickListener {
            navMessage()
        }

        bottomUserProfileButton.setOnClickListener {
            navUserProfile()
        }

        newUserButton.setOnClickListener {
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
                }
                "profile" -> {

                    val action = ProfileFragmentDirections.actionProfileFragmentToChatListFragment()

                    Navigation.findNavController(
                        requireActivity(),
                        R.id.main_fragment_layout
                    )
                        .navigate(action)

                    GLOBAL_CURRENT_FRAGMENT = "chat_list"
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

                }
            }

        }
    }

    private fun navUserProfile() {
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

                }
            }
        }
    }

}
