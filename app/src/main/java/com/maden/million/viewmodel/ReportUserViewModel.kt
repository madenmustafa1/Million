package com.maden.million.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class ReportUserViewModel: ViewModel() {

    private var db = Firebase.firestore
    private var auth = Firebase.auth

    val reportUser = MutableLiveData<Boolean>()

    fun reportUser(username: String, nameSurname: String, des: String){


        val uuid = UUID.randomUUID()
        val reportRef = db.collection("ReportUser")

        val data = hashMapOf(
            "userEmail" to auth.currentUser?.email.toString(),
            "date" to Timestamp.now(),
            "uuid" to uuid.toString(),
            "reportUserUsername" to username,
            "reportUserNameSurname" to nameSurname,
            "reportUserDes" to des,
            "showReport" to true
        )

        reportRef
            .add(data)
            .addOnSuccessListener { if (it != null){ reportUser.value = true }
            }.addOnFailureListener{ reportUser.value = false }
    }
}