package com.maden.million.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.maden.million.model.NewUserData


class GetNewUserViewModel : ViewModel() {

    private var db = Firebase.firestore


    val newUserDataClass = MutableLiveData<NewUserData>()

    val newUserProfilePhoto = MutableLiveData<String>()


    fun getNewUser() {

        var newUserEmail: String

        val dbRef = db.collection("NewUserList")
            .document("emails")


        dbRef.get().addOnSuccessListener {

            val size = it["emailSize"] as Number

            val randomNumber = (0..size.toInt()).random()

            val emailList = it["emails"] as List<String>

            println(randomNumber)
            println(emailList[randomNumber])
            newUserEmail = emailList[randomNumber]

            val profileRef = db.collection("Profile")
                .document(newUserEmail)
                .get().addOnSuccessListener {
                    val nameSurname = it["name"].toString() + " " +
                            it["surname"].toString()

                    val photoUrl = it["photoUrl"].toString()
                    val username = it["username"].toString()
                    val aboutMe = it ["aboutMe"].toString()

                    val user = NewUserData(nameSurname, username, aboutMe, photoUrl)

                    newUserDataClass.value = user
                }
        }
    }
}