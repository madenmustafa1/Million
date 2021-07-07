package com.maden.million.adapter

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.maden.million.R
import com.maden.million.databinding.ItemChatBinding
import com.maden.million.model.ChatData
import com.maden.million.model.UserControlData
import kotlinx.android.synthetic.main.item_chat.view.*


class ChatAdapter(private val chat: ArrayList<ChatData>)
    :RecyclerView.Adapter<ChatAdapter.ViewHolder>(){

    class ViewHolder(val view: ItemChatBinding): RecyclerView.ViewHolder(view.root) {

    }
    private var auth = Firebase.auth

    private var currentUser : String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        currentUser = auth.currentUser.email
        val view = DataBindingUtil.inflate<ItemChatBinding>(
            inflater, R.layout.item_chat,
            parent, false
        )
        return ChatAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.view.chatData = chat[position]
        //holder.view.username.gravity

        if(currentUser != null && currentUser != ""){
            if (currentUser == chat[position].email){
                holder.itemView.item_chat_adapter.gravity = Gravity.END

                val color = UserControlData("#B9CBED")
                holder.view.userControl = color
            } else {
                holder.itemView.item_chat_adapter.gravity = Gravity.START
                val color = UserControlData("#B9CBED")
            }
        }
    }

    override fun getItemCount(): Int {
        return chat.size
    }

    fun clearList(){
        chat.clear()
    }

    fun updateChatList(newChatList: List<ChatData>) {
        chat.clear()
        chat.addAll(newChatList)
        notifyDataSetChanged()
    }
}