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
                }

            }
        }

        bottomUserProfileButton.setOnClickListener {
            if (GLOBAL_CURRENT_FRAGMENT != "profile") {
                if (GLOBAL_CURRENT_FRAGMENT == "chat") {
                    val action = ChatFragmentDirections
                        .actionChatFragmentToProfileFragment()
                    Navigation.findNavController(
                        requireActivity(),
                        R.id.main_fragment_layout
                    )
                        .navigate(action)

                    GLOBAL_CURRENT_FRAGMENT = "profile"

                } else if (GLOBAL_CURRENT_FRAGMENT == "chat_list") {

                    val action =
                        ChatListFragmentDirections.
                        actionChatListFragmentToProfileFragment()

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

/*
        homeButton.setOnClickListener {
            if(GLOBAL_CURRENT_FRAGMENT != "show_story"){
                when (GLOBAL_CURRENT_FRAGMENT) {
                    "search_story" -> {
                        val action = SearchFragmentDirections.actionSearchFragmentToShowStoryFragment()
                        Navigation.findNavController(requireActivity(), R.id.main_navigation).navigate(action)
                        GLOBAL_CURRENT_FRAGMENT = "show_story"
                    }
                    "add_story" -> {
                        val action = AddStoryFragmentDirections.actionAddStoryFragmentToShowStoryFragment()
                        Navigation.findNavController(requireActivity(), R.id.main_navigation).navigate(action)
                        GLOBAL_CURRENT_FRAGMENT = "show_story"
                    }
                    "profile_story" -> {
                        val action = UserProfileFragmentDirections.actionUserProfileFragmentToShowStoryFragment()
                        Navigation.findNavController(requireActivity(), R.id.main_navigation).navigate(action)
                        GLOBAL_CURRENT_FRAGMENT = "show_story"
                    }
                    "other_profile_story" -> {
                        val action = OtherUserProfileFragmentDirections.actionOtherUserProfileFragmentToShowStoryFragment()
                        Navigation.findNavController(requireActivity(), R.id.main_navigation).navigate(action)
                        GLOBAL_CURRENT_FRAGMENT = "show_story"
                    }
                    "comment_story" -> {
                        val action = CommentFragmentDirections.actionCommentFragmentToShowStoryFragment()
                        Navigation.findNavController(requireActivity(), R.id.main_navigation).navigate(action)
                        GLOBAL_CURRENT_FRAGMENT = "show_story"
                    }
                }
            }
        }
 */