package com.maden.million.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.maden.million.R
import com.maden.million.activity.GLOBAL_CURRENT_FRAGMENT
import com.maden.million.databinding.ItemChatListBinding
import com.maden.million.model.ChatListData
import com.maden.million.model.DownloadPhotoUrl
import com.maden.million.view.ChatListFragmentDirections

class ChatListAdapter(
    private val chatList: ArrayList<ChatListData>,
    private val photoList: ArrayList<DownloadPhotoUrl>

) : RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder>() {

    class ChatListViewHolder(val view: ItemChatListBinding) :
        RecyclerView.ViewHolder(view.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
    : ChatListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<ItemChatListBinding>(
            inflater, R.layout.item_chat_list,
            parent, false
        )
        return ChatListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatListViewHolder, position: Int) {
        holder.view.chatListData = chatList[position]



        holder.itemView.setOnClickListener {
            navToChat(chatList[position].uuid, chatList[position].email,
                    chatList[position].username,
                chatList[position].profilePhotoUrl, it)
        }
    }



    fun updateChatList(newChatList: List<ChatListData>,) {
        chatList.clear()
        chatList.addAll(newChatList)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    private fun navToChat(uuid: String, otherEmail: String,
                          otherUsername: String,
                          downloadPhotoUrl: String ,view: View){

        val action = ChatListFragmentDirections
            .actionChatListFragmentToChatFragment(uuid, otherEmail,
                otherUsername, downloadPhotoUrl)

        view.findNavController().navigate(action)
        GLOBAL_CURRENT_FRAGMENT = "chat"
    }
}
/*

    private fun nav(view: View){
        if(GLOBAL_CURRENT_FRAGMENT == "search_story"){
            val action =
                SearchFragmentDirections.actionSearchFragmentToOtherUserProfileFragment(otherUserEmail)
            view.findNavController().navigate(action)
            GLOBAL_CURRENT_FRAGMENT = "other_profile_story"
        }
    }
 */