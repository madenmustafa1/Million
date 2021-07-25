package com.maden.million.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.maden.million.model.NewUserData
import com.maden.million.util.UserChatList

import java.util.*


class GetNewUserViewModel : ViewModel() {

    private var db = Firebase.firestore
    private var auth = Firebase.auth

    val newUserDataClass = MutableLiveData<NewUserData>()

    val newChatRoomUUID = MutableLiveData<String>()

    var newUser = MutableLiveData<Boolean>()

    //*date*//

    fun getNewUser() {

        var newUserEmail: String? = null

        var str: String
        var id: Int? = null


        val idRef = db.collection("Profile")

        idRef
            .orderBy("creationDate", Query.Direction.DESCENDING)
            .limit(1)
            .get().addOnSuccessListener {
                for (i in it) {

                    str = i["id"].toString()
                    id = str.toInt()

                }
            }.addOnCompleteListener {
                if(id != null) {
                    val randomNumber = (1..id!!).random()

                    println("$randomNumber RANDOM NUMBERR")

                    idRef
                        .whereEqualTo("id", randomNumber)
                        .get().addOnSuccessListener {
                            for (i in it){
                                newUserEmail = i["email"].toString()
                                for (email in UserChatList.userEmail) {
                                    if(newUserEmail == email) {
                                        newUser.value = false
                                        break
                                    } else {
                                        newUser.value = true
                                    }
                                }
                            }
                        }.addOnCompleteListener {

                            if(newUser.value == true){
                                val profileRef = db.collection("Profile")
                                    .document(newUserEmail!!)
                                    .get().addOnSuccessListener {
                                        println(it["photoUrl"].toString())
                                        println(it["username"].toString())
                                        println(it["aboutMe"].toString())
                                        println(it["email"].toString())

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

        dbRef.document(email).set(myChatChannel).addOnSuccessListener {

        }.addOnCompleteListener { }.addOnFailureListener {
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

        }.addOnCompleteListener { }


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