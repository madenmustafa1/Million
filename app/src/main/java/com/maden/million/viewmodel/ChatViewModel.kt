package com.maden.million.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.maden.million.model.ChatData
import com.onesignal.OneSignal
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList
import java.text.SimpleDateFormat





class ChatViewModel : ViewModel() {

    private var db = Firebase.firestore
    private var auth = Firebase.auth

    val chatDataClass = MutableLiveData<List<ChatData>>()
    val userHour = MutableLiveData<String>()
    val userDate = MutableLiveData<String>()

    private val chatList: ArrayList<ChatData> = ArrayList<ChatData>()

    private val uuidList = ArrayList<String>()


    fun getMyChat(uuid: String) {
        val chatRef = db.collection("Chats")
        chatRef
            .document(uuid)
            .collection("chat")
            .orderBy("date", Query.Direction.DESCENDING)
            .limit(75)
            .get()
            .addOnSuccessListener { value ->
                if (value != null) {

                    //Açılacak
                    //chatAdapter.clearList()
                    //chatList.clear()

                    for (chat in value) {

                        val chats = ChatData(
                            chat.data["email"] as String,
                            chat.data["message"] as String,
                            chat.data["uuid"] as String,
                            "date"
                        )


                        chatList.add(chats)
                        chatDataClass.value = chatList
                        //chatDataClass.postValue(chatList)

                        uuidList.add(chat["uuid"].toString())

                    }
                }
            }.addOnCompleteListener {
                //İlk girişte bütün mesajlar getirtiliyor.
                //Sonra mesaj geldiğinde en son 5 mesaj
                // uuid'lerine göre kontrol ediliyor ve olmayanlar ekleniyor.
                chatRef
                    .document(uuid)
                    .collection("chat")
                    .orderBy("date", Query.Direction.DESCENDING)
                    .limit(5)
                    .addSnapshotListener { value, error ->
                        if(value != null ){
                            var index: Int = 0

                            for (chat in value) {
                                var isShow: Boolean? = false

                                if (uuidList.isNotEmpty()){
                                    if (uuidList.size > 5) {
                                        for (i in 0..5) {
                                            if (chat["uuid"] != null) {
                                                if (chat["uuid"].toString() == uuidList[i]){
                                                    isShow = true
                                                }
                                            }

                                        }
                                    } else {
                                        for (i in 0 until uuidList.size) {
                                            if (chat["uuid"] != null) {
                                                if (chat["uuid"].toString() == uuidList[i]){
                                                    isShow = true
                                                }
                                            }

                                        }
                                    }
                                }



                                if (isShow == false){

                                    val chats = ChatData(
                                        chat["email"] as String,
                                        chat["message"] as String,
                                        chat["uuid"] as String,
                                        "date"
                                    )

                                    uuidList.add(index,chat["uuid"].toString())
                                    chatList.add(index,chats)

                                    index += 1
                                    if(index == 5){ index = 0 }
                                    chatDataClass.value = chatList

                                }
                            }
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

    fun userOnline(email: String){
        val dbRef = db.collection("Profile")

        dbRef
            .document(email)
            .get().addOnSuccessListener {

                if(it["userOnlineHour"] != null){
                    var userOnlineHour = it["userOnlineHour"] as Timestamp

                    val dateUser = SimpleDateFormat("dd-MM-yyyy")
                    val date = dateUser.format(Date(userOnlineHour.toDate().time))
                    val firebaseFormat = dateUser.format(Date(Timestamp.now().toDate().time))

                    if (firebaseFormat != date){
                        userDate.value = date
                    }

                    val dateUserHour = SimpleDateFormat("HH:mm")
                    val hour = dateUserHour.format(Date(userOnlineHour.toDate().time))
                    userHour.value = hour
                }
        }
    }
}

