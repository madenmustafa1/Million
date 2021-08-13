package com.maden.million.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.maden.million.model.ChatListData
import com.maden.million.util.UserChatList

class ChatListViewModel : ViewModel() {

    private var db = Firebase.firestore
    private var auth = Firebase.auth

    val chatListDataClass = MutableLiveData<ArrayList<ChatListData>>()
    val arrayList: ArrayList<ChatListData> = ArrayList<ChatListData>()

    private val fullName = ArrayList<String>()
    val email = ArrayList<String>()
    private val uuid = ArrayList<String>()
    val message = ArrayList<String>()
    private val photoUrlList = ArrayList<String>()



    fun getMyChatList() {

        val dbRef = db.collection("Profile")
            .document(auth.currentUser!!.email!!.toString())
            .collection("ChatChannel")



        arrayList.clear()
        fullName.clear()
        email.clear()
        uuid.clear()
        message.clear()
        photoUrlList.clear()
        //
        UserChatList.userEmail.clear()
        //

        var forSize: Int = 0


        //Kullanıcının konuştuklarını tarihe göre sıralama
        dbRef.orderBy("date", Query.Direction.DESCENDING)
            .get().addOnSuccessListener {

                for (i in it) {
                    fullName.add(i["fullName"].toString())
                    email.add(i["email"].toString())
                    uuid.add(i["uuid"].toString())

                    //
                    UserChatList.userEmail.add(i["email"].toString())
                    //
                }
            }.addOnCompleteListener {
                for (number in 0 until fullName.size) {
                    message.add("")


                    val chatRef = db.collection("Chats")
                    chatRef
                        .document(uuid[number])
                        .collection("chat")
                        .orderBy("date", Query.Direction.DESCENDING)
                        .limit(1)
                        .get().addOnSuccessListener {

                            for (i in it) {
                                forSize++

                                message[number] = i["message"].toString()

                                break
                            }
                        }.addOnCompleteListener {

                            if(forSize == email.size){
                                profilePhoto(email, fullName, uuid, message)

                            }
                        }
                }
            }
    }

    private fun profilePhoto(email: ArrayList<String>, fullName: ArrayList<String>,
                             uuid: ArrayList<String>, message: ArrayList<String>){

        var forSize: Int = 0

        for (number in 0 until fullName.size ) {
            photoUrlList.add("")

            val dbRef = db.collection("Profile")
                .document(email[number])
            dbRef.get().addOnSuccessListener { photoUrl ->
                forSize++
                photoUrlList[number] = photoUrl["photoUrl"].toString()

            }.addOnCompleteListener {
                if(forSize == email.size){
                    showChatList(email, fullName, uuid, message,  photoUrlList)
                }
            }

        }
    }

    private fun showChatList(email: ArrayList<String>, fullName: ArrayList<String>,
                             uuid: ArrayList<String>, message: ArrayList<String>,
                             photoUrl: ArrayList<String>){


        for (number in 0 until photoUrl.size){
            val chat = ChatListData(
                fullName[number],
                email[number],
                message[number],
                uuid[number],
                "date",
                photoUrlList[number].toString()
            )

            arrayList.add(chat)
            chatListDataClass.postValue(arrayList)
        }
    }
}


