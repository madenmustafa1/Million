package com.maden.million.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.maden.million.adapter.ChatAdapter
import com.maden.million.model.ChatData
import com.onesignal.OneSignal
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList


class ChatViewModel : ViewModel() {

    private var db = Firebase.firestore
    private var auth = Firebase.auth

    val chatDataClass = MutableLiveData<List<ChatData>>()
    val chatList: ArrayList<ChatData> = ArrayList<ChatData>()

    private val chatAdapter = ChatAdapter(arrayListOf())

    fun getMyChat(uuid: String) {
        val chatRef = db.collection("Chats")
        chatRef
            .document(uuid)
            .collection("chat")
            .orderBy("date", Query.Direction.DESCENDING)
            .limit(200)
            .addSnapshotListener { value, error ->
                if (value != null) {

                    chatAdapter.clearList()
                    chatList.clear()

                    for (chat in value) {


                        val chat = ChatData(
                            //chat.data["username"] as String,
                            chat.data["email"] as String,
                            chat.data["message"] as String,
                            chat.data["uuid"] as String,
                            "date"
                        )

                        //val chatList = arrayListOf<ChatData>(chat)

                        chatList.add(chat)

                        chatDataClass.value = chatList
                        //chatDataClass.postValue(chatList)
                    }
                }
            }
    }


    fun sendMessage(message: String, otherEmail: String,
                    otherFullName: String, roomUUID: String) {
        if (message != "") {
            val uuid = UUID.randomUUID()
            val chatRef = db.collection("Chats")

            val data = hashMapOf(
                "message" to message,
                "email" to auth.currentUser?.email.toString(),
                "date" to Timestamp.now(),
                "uuid" to uuid.toString()
            )


            chatRef.document(roomUUID)
                .collection("chat")
                .add(data)
                .addOnSuccessListener {}
                .addOnCompleteListener {



                }
            val dbRef = db.collection("Profile")
            var nameSurname: String? = null
            dbRef.document(otherEmail)
                .collection("ChatChannel")
                .document(auth.currentUser!!.email!!.toString())
                .update("date", Timestamp.now())
                .addOnCompleteListener {

                    dbRef.document(auth.currentUser!!.email!!.toString())
                        .get().addOnSuccessListener {
                            nameSurname  = it["name"].toString() + " " +
                                    it["surname"].toString()


                        }.addOnCompleteListener {
                            dbRef.document(otherEmail)
                                .get().addOnSuccessListener {
                                    val oneSignalID = it["oneSignalID"]

                                    if(oneSignalID != null && oneSignalID != "") {
                                        try {

                                            OneSignal.postNotification(
                                                JSONObject("{'contents': {'en':'${ nameSurname }: ${ message }'}, 'include_player_ids': ['" + oneSignalID.toString() + "']}"),
                                                null
                                            )
                                        } catch (e: JSONException) {
                                            e.printStackTrace()
                                        }
                                    }
                                }
                        }


                }

            dbRef.document(auth.currentUser!!.email!!.toString())
                .collection("ChatChannel")
                .document(otherEmail)
                .update("date", Timestamp.now())

            //.set(updateChatChannel)
        }
    }
}

