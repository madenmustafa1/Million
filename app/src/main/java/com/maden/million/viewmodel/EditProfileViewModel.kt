package com.maden.million.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class EditProfileViewModel: ViewModel() {

    private var db = Firebase.firestore
    private var auth = Firebase.auth


    fun editInstagram(previousValue: String, currentValue: String){

        val p: String = previousValue.replace(" ", "").trim()
        val c: String = currentValue.replace(" ", "").trim()

        if(p != c){
            if(auth.currentUser?.email != null){
                val dbRef = db.collection("Profile")
                    .document(auth.currentUser!!.email!!)

                dbRef.update("instagram", c)
            }
        }



    }
    fun editFacebook(previousValue: String, currentValue: String){
        val p: String = previousValue.replace(" ", "").trim()
        val c: String = currentValue.replace(" ", "").trim()

        if(p != c){
            if(auth.currentUser?.email != null){
                val dbRef = db.collection("Profile")
                    .document(auth.currentUser!!.email!!)

                dbRef.update("facebook", c)
            }
        }
    }
    fun editTwitter(previousValue: String, currentValue: String){
        val p: String = previousValue.replace(" ", "").trim()
        val c: String = currentValue.replace(" ", "").trim()

        if(p != c){
            if(auth.currentUser?.email != null){
                val dbRef = db.collection("Profile")
                    .document(auth.currentUser!!.email!!)

                dbRef.update("twitter", c)
            }
        }
    }

    fun editAboutMe(previousValue: String, currentValue: String){
        val p: String = previousValue
        val c: String = currentValue

        if(p != c){
            if(auth.currentUser?.email != null){
                val dbRef = db.collection("Profile")
                    .document(auth.currentUser!!.email!!)

                dbRef.update("aboutMe", c)
            }
        }
    }
}