package com.maden.million.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.maden.million.model.ChatListData
import com.maden.million.model.DownloadPhotoUrl

class ChatListViewModel : ViewModel() {

    private var db = Firebase.firestore
    private var auth = Firebase.auth

    val chatListDataClass = MutableLiveData<ArrayList<ChatListData>>()
    val arrayList: ArrayList<ChatListData> = ArrayList<ChatListData>()

    fun getMyChatList() {

        //###############################################
        //Kullanıcının kimlerle mesajlaştığının bilgisi.
        //###############################################
        val dbRef = db.collection("Profile")
            .document(auth.currentUser!!.email!!.toString())
            .collection("ChatChannel")

        arrayList.clear()

        //###############################################
        //Kullanıcının sohbetleri getirmek için.
        //###############################################

        dbRef.orderBy("date", Query.Direction.DESCENDING)
            .get().addOnSuccessListener {
                for (chatChannel in it) {

                    val chatRef = db.collection("Chats")
                    chatRef
                        .document(chatChannel["uuid"].toString())
                        .collection("chat")
                        .orderBy("date", Query.Direction.DESCENDING)
                        .get().addOnSuccessListener {
                            for (i in it) {
                                val email = chatChannel["email"] as String
                                val fullName: String = chatChannel["fullName"].toString()

                                println(email)
                                //###############################################
                                // Kulllanıcı Profil fotoğrafı.
                                //###############################################

                                //Düzenlenecek.
                                val dbRef = db.collection("Profile")
                                    .document(email)
                                dbRef.get().addOnSuccessListener { photoUrl ->

                                    val chat = ChatListData(
                                        fullName,
                                        email,
                                        i["message"] as String,
                                        chatChannel["uuid"] as String,
                                        "date",
                                        photoUrl["photoUrl"].toString()
                                    )

                                    arrayList.add(chat)
                                    chatListDataClass.postValue(arrayList)

                                }

                                break
                            }
                        }
                }
            }
    }
}
