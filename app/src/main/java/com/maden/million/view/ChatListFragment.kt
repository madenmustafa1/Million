package com.maden.million.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.maden.million.activity.GLOBAL_CURRENT_FRAGMENT
import com.maden.million.adapter.ChatListAdapter
import com.maden.million.databinding.FragmentChatListBinding
import com.maden.million.viewmodel.ChatListViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.appbar_chat_list.*
import kotlinx.android.synthetic.main.fragment_bottom_buttons.*
import kotlinx.android.synthetic.main.fragment_chat_list.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.maden.million.model.UserModel
import com.maden.million.service.RequestUser
import com.maden.million.service.UserAPI
import com.maden.million.util.Info
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ChatListFragment : Fragment() {

    private var _binding: FragmentChatListBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChatListBinding
            .inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private lateinit var chatListModel: ChatListViewModel

    private val chatListAdapter = ChatListAdapter(arrayListOf(), arrayListOf())

    private var auth = Firebase.auth



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        (activity as AppCompatActivity).supportActionBar?.title = "Million"

        GLOBAL_CURRENT_FRAGMENT = "chat_list"

        chatListModel = ViewModelProvider(this).get(ChatListViewModel::class.java)
        chatListModel.getMyChatList()



        binding.chatListRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.chatListRecyclerView.adapter = chatListAdapter

        observeData()



        //Kullanıcı uygulamaya ilk defa giriyorsa
        if(auth.currentUser?.email != null){
            val sharedPreff = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
            val value = sharedPreff.getInt(auth.currentUser?.email.toString(), 0)


            if(value == 0){
                binding.waitUserLayoutChatList.visibility = View.GONE
                binding.chatListInfo.visibility = View.VISIBLE
                binding.chatListFirstLoginInfo.setText(Info.infoFirstLogin)
            }
        }

        binding.chatListInfoButton.setOnClickListener {
            binding.chatListInfo.visibility = View.GONE

            val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return@setOnClickListener
            with (sharedPref.edit()) {
                putInt(auth.currentUser?.email.toString(), 1)
                apply()
            }
        }

        MainScope().launch {
            destroyWaitLayout()
        }

    }




    private fun observeData(){

        chatListModel.chatListDataClass.observe(viewLifecycleOwner, Observer {
            it?.let {

                chatListAdapter.updateChatList(it)
                binding.waitUserLayoutChatList.visibility = View.GONE
                binding.chatListRecyclerView.visibility = View.VISIBLE
            }
        })
    }

    private suspend fun destroyWaitLayout(){
        delay(3000)

        if(chatListModel.message.isEmpty()){
            binding.waitUserLayoutChatList.visibility = View.GONE
        }
    }

}