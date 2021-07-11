package com.maden.million.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.maden.million.R
import com.maden.million.view.ChatFragmentDirections
import com.maden.million.view.ChatListFragmentDirections
import com.maden.million.view.OtherProfileFragmentDirections
import com.maden.million.view.ProfileFragmentDirections
import kotlinx.android.synthetic.main.fragment_bottom_buttons.*


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

    }

    private fun navMessage(){

        if (GLOBAL_CURRENT_FRAGMENT != "chat_list") {

            if (GLOBAL_CURRENT_FRAGMENT == "chat") {
                val action = ChatFragmentDirections
                    .actionChatFragmentToChatListFragment()
                Navigation.findNavController(requireActivity(),
                    R.id.main_fragment_layout)
                    .navigate(action)

                GLOBAL_CURRENT_FRAGMENT = "chat_list"
            } else if (GLOBAL_CURRENT_FRAGMENT == "profile") {

                val action = ProfileFragmentDirections.
                actionProfileFragmentToChatListFragment()

                Navigation.findNavController(
                    requireActivity(),
                    R.id.main_fragment_layout
                )
                    .navigate(action)

                GLOBAL_CURRENT_FRAGMENT = "chat_list"
            } else if(GLOBAL_CURRENT_FRAGMENT == "other_profile"){

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

        }
    }

    private fun navUserProfile(){
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

            }
        }
    }

}
