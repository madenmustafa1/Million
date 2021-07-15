package com.maden.million.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.maden.million.model.UserProfileData

class ProfileViewModel: ViewModel() {

    private var db = Firebase.firestore
    private var auth = Firebase.auth

    val profileDataClass = MutableLiveData<List<UserProfileData>>()
    val uProfilePhoto = MutableLiveData<String>()

    fun getMyProfile() {


        if (auth.currentUser != null) {
            val profileRef = db.collection("Profile")
                .document(auth.currentUser!!.email!!.toString())

            profileRef.get().addOnSuccessListener {
                if (it != null) {

                    val nameSurname = it["name"].toString() + " " +
                            it["surname"].toString()
                    val email = it["email"].toString()
                    val username = it["username"].toString()
                    val aboutMe = it ["aboutMe"].toString()


                    val facebook = it["facebook"].toString()
                    val instagram = it["instagram"].toString()
                    val twitter = it["twitter"].toString()

                    uProfilePhoto.value =  it["photoUrl"].toString()



                    var like = it["like"] as List<String>
                    var dislike = it["dislike"] as List<*>


                    val myProfileData = UserProfileData(
                        nameSurname, email, username,
                        like.size.toString(),
                        dislike.size.toString(), aboutMe,
                        facebook, instagram, twitter
                    )

                    profileDataClass.value = listOf(myProfileData)
                }
            }.addOnCompleteListener {

                /*
                ref.child(profileDataClass.value?.get(0)!!.userEmail!!)
                    .child("profilePhoto")
                    .downloadUrl.addOnSuccessListener {
                        if (it != null){
                            //uProfilePhoto.value = it.toString()
                        }
                    }
                 */
            }
        }
    }
}