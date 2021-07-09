package com.maden.million.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.maden.million.R
import com.maden.million.activity.GLOBAL_CURRENT_FRAGMENT
import com.maden.million.adapter.ChatAdapter
import com.maden.million.viewmodel.ChatViewModel
import kotlinx.android.synthetic.main.appbar_chat.*
import kotlinx.android.synthetic.main.fragment_chat.*


class ChatFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    private lateinit var chatViewModel: ChatViewModel
    private val chatAdapter = ChatAdapter(arrayListOf())
    var chatUUID: String? = null
    var otherEmail: String? = null
    var otherFullName: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //(activity as AppCompatActivity).supportActionBar?.title = "Million"
        GLOBAL_CURRENT_FRAGMENT = "chat"

        chatViewModel = ViewModelProvider(this).get(ChatViewModel::class.java)

        arguments?.let {
            chatUUID = ChatFragmentArgs.fromBundle(it).uuid
            otherEmail = ChatFragmentArgs.fromBundle(it).otherEmail
            otherFullName = ChatFragmentArgs.fromBundle(it).otherUsername

            otherFullNameAppBar.text = otherFullName

            if (chatUUID != null) {
                chatViewModel.getMyChat(chatUUID!!)
            }
        }

        chatRecyclerView.layoutManager = LinearLayoutManager(context)
        chatRecyclerView.adapter = chatAdapter

        sendMessageButton.setOnClickListener {
            sendMessage()
        }

        goBackButton.setOnClickListener {
            goBackButtonFun()
        }

        observeData()
    }

    private fun observeData() {
        chatViewModel.chatDataClass.observe(viewLifecycleOwner, Observer {
            it?.let {
                chatAdapter.updateChatList(it)
            }
        })
    }

    private fun goBackButtonFun(){
        val action = ChatFragmentDirections
            .actionChatFragmentToChatListFragment()
        Navigation.findNavController(requireActivity(),
            R.id.main_fragment_layout)
            .navigate(action)

        GLOBAL_CURRENT_FRAGMENT = "chat_list"
    }
    private fun sendMessage(){
        val message = sendMessageText.text.toString()
        var t: String = message.replace(" ", "").trim()

        if(t != "" && chatUUID != null
            && otherEmail != null
            && otherEmail != ""
            && otherFullName != ""
            && otherFullName != null
        ){

            chatViewModel.sendMessage(message, otherEmail!!,
                otherFullName!!, chatUUID!!)

            sendMessageText.setText("")
        }
    }
}