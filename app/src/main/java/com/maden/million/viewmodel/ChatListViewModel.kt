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
                                val chat = ChatListData(
                                    ini["otherUsername"] as String,
                                    i["email"] as String,
                                    i["message"] as String,
                                    ini["uuid"] as String,
                                    "date",
                                    "url"
                                )

                                val chatList = arrayListOf<ChatListData>(chat)
                                arrayList.add(chat)
                                //chatListDataClass.value = chatList
                                chatListDataClass.postValue(arrayList)

                                break
                            }
                        }
                }
            }


        /*
        dbRef
            .orderBy("date", Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->

                if (value != null) {
                    for (v in value) {

                        val chatRef = db.collection("Chats")

                        chatRef
                            .document(v["uuid"].toString())
                            .collection("chat")
                            .orderBy("date", Query.Direction.DESCENDING)
                            .addSnapshotListener { value, error ->
                                if (value != null) {

                                    for (chat in value) {
                                        val chat = ChatListData(
                                            chat.data["username"] as String,
                                            chat.data["email"] as String,
                                            chat.data["message"] as String,
                                            chat.data["uuid"] as String,
                                            "date",
                                            "url"
                                        )

                                        val chatList = arrayListOf<ChatListData>(chat)

                                        chatListDataClass.postValue(chatList)

                                        break
                                    }
                                }
                            }
                    }
                }
            }
                 */
    }


}


/*
dbRef
   // .orderBy("date", Query.Direction.DESCENDING)
    .get().addOnSuccessListener {
        for (i in it){
            //println(i["uuid"])
        }
    }
 */