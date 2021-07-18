package com.maden.million.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.maden.million.model.NewUserData
import java.util.*


class GetNewUserViewModel : ViewModel() {

    private var db = Firebase.firestore
    private var auth = Firebase.auth

    val newUserDataClass = MutableLiveData<NewUserData>()

    val newChatRoomUUID = MutableLiveData<String>()


    fun getNewUser() {
        var newUserEmail: String

        val dbRef = db.collection("NewUserList")
            .document("emails")

        dbRef.get().addOnSuccessListener {

            val size = it["emailSize"] as Number
            val randomNumber = (0..size.toInt()).random()

            val emailList = it["emails"] as List<String>

            newUserEmail = emailList[randomNumber]

            val profileRef = db.collection("Profile")
                .document(newUserEmail)
                .get().addOnSuccessListener {
                    val nameSurname = it["name"].toString() + " " +
                            it["surname"].toString()

                    val photoUrl = it["photoUrl"].toString()
                    val username = it["username"].toString()
                    val aboutMe = it["aboutMe"].toString()
                    val email = it["email"].toString()

                    val user = NewUserData(
                        nameSurname, username,
                        aboutMe, email, photoUrl
                    )
                    newUserDataClass.value = user
                }
        }
    }

    fun startChat(email: String, fullName: String) {
        val roomUUID = UUID.randomUUID()

        newChatRoomUUID.value = roomUUID.toString()

        val myChatChannel = hashMapOf(
            "fullName" to fullName,
            "email" to email,
            "date" to Timestamp.now(),
            "uuid" to roomUUID.toString()
        )

        val dbRef = db.collection("Profile")
            .document(auth.currentUser.email).collection("ChatChannel")

        dbRef.add(myChatChannel).addOnSuccessListener {

        }.addOnCompleteListener {  }.addOnFailureListener {
            println(it)
        }


        val otherChatChannel = hashMapOf(
            "fullName" to fullName,
            "email" to auth.currentUser.email,
            "date" to Timestamp.now(),
            "uuid" to roomUUID.toString()
        )

        val otherDbRef = db.collection("Profile")
            .document(email).collection("ChatChannel")

        otherDbRef.add(otherChatChannel).addOnSuccessListener {

        }.addOnCompleteListener {  }



        val messageUUID = UUID.randomUUID()
        val data = hashMapOf(
            "message" to "Merhaba",
            "email" to auth.currentUser?.email.toString(),
            "date" to Timestamp.now(),
            "uuid" to messageUUID.toString()
        )


        val chatRef = db.collection("Chats")
        chatRef.document(roomUUID.toString())
            .collection("chat")
            .add(data)
    }
}