package com.maden.million.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.maden.million.model.OtherUserProfileData
import com.maden.million.model.UserProfileData

class OtherProfileViewModel: ViewModel() {

    private var db = Firebase.firestore
    private var auth = Firebase.auth

    private val storage = FirebaseStorage.getInstance()

    val otherUserProfileData = MutableLiveData<List<OtherUserProfileData>>()

    val otherUserProfilePhoto = MutableLiveData<String>()

    fun getOtherUserProfile(email: String){
        if (auth.currentUser != null) {
            val profileRef = db.collection("Profile")
                .document(email)

            profileRef.get().addOnSuccessListener {
                if (it != null) {

                    val nameSurname = it["name"].toString() + " " +
                            it["surname"].toString()
                    val email = it["email"].toString()
                    val username = it["username"].toString()
                    val aboutMe = it["aboutMe"].toString()

                    val facebook = it["facebook"].toString()
                    val instagram = it["instagram"].toString()
                    val twitter = it["twitter"].toString()

                    otherUserProfilePhoto.value = it["photoUrl"].toString()


                    // Değişecek----------------------------- //
                    var like = it["like"] as List<String> //
                    var dislike = it["like"] as List<*>  //
                    // ---------------------------------------//

                    val otherUserData = OtherUserProfileData(
                        nameSurname, email, username,
                        like.size.toString(),
                        dislike.size.toString(), aboutMe,
                        facebook, instagram, twitter
                    )

                    otherUserProfileData.value = listOf(otherUserData)
                }
            }
        }

    }


}