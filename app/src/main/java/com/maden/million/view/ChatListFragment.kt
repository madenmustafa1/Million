package com.maden.million.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.maden.million.R
import com.maden.million.activity.GLOBAL_CURRENT_FRAGMENT
import com.maden.million.adapter.ChatListAdapter
import com.maden.million.viewmodel.ChatListViewModel
import kotlinx.android.synthetic.main.fragment_chat_list.*


class ChatListFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat_list, container, false)
    }

    private lateinit var chatListModel: ChatListViewModel
    private val chatListAdapter = ChatListAdapter(arrayListOf())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.title = "Million"

        GLOBAL_CURRENT_FRAGMENT = "chat_list"

        chatListModel = ViewModelProvider(this).get(ChatListViewModel::class.java)
        chatListModel.getMyChatList()

        chatListRecyclerView.layoutManager = LinearLayoutManager(context)
        chatListRecyclerView.adapter = chatListAdapter

        observeData()
    }

    private fun observeData(){
        chatListModel.chatListDataClass.observe(viewLifecycleOwner, Observer {
            it?.let {
                chatListAdapter.updateChatList(it)
            }
        })
    }
}