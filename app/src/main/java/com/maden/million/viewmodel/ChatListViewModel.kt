package com.maden.million.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.maden.million.model.ChatListData

class ChatListViewModel : ViewModel() {

    private var db = Firebase.firestore
    private var auth = Firebase.auth

    val chatListDataClass = MutableLiveData<ArrayList<ChatListData>>()
    val arrayList: ArrayList<ChatListData> = ArrayList<ChatListData>()

    fun getMyChatList() {


        val dbRef = db.collection("Profile")
            .document(auth.currentUser!!.email!!.toString())
            .collection("ChatChannel")

        arrayList.clear()


        dbRef
            .orderBy("date", Query.Direction.DESCENDING)
            .get().addOnSuccessListener {
                for(ini in it) {

                    val chatRef = db.collection("Chats")
                    chatRef
                        .document(ini["uuid"].toString())
                        .collection("chat")
                        .orderBy("date", Query.Direction.DESCENDING)
                        .get().addOnSuccessListener {
                            for(i in it){
                                println(ini)
                                val fullName: String = ini["fullName"].toString()
                                val chat = ChatListData(
                                    fullName,
                                    ini["email"] as String,
                                    i["message"] as String,
                                    ini["uuid"] as String,
                                    "date",
                                    "url"
                                )

                                //val chatList = arrayListOf<ChatListData>(chat)
                                //chatListDataClass.value = chatList

                                arrayList.add(chat)
                                chatListDataClass.postValue(arrayList)

                                break
                            }
                        }
                }
            }
    }
}
