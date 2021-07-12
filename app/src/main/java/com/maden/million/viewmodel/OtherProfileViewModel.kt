package com.maden.million.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.maden.million.model.OtherUserProfileData

class OtherProfileViewModel : ViewModel() {

    private var db = Firebase.firestore
    private var auth = Firebase.auth


    val otherUserProfileData = MutableLiveData<List<OtherUserProfileData>>()
    val otherUserProfilePhoto = MutableLiveData<String>()

    val otherUserLikeControl = MutableLiveData<Boolean>()
    val otherUserDislikeControl = MutableLiveData<Boolean>()

    val otherUserLikeSize = MutableLiveData<String>()
    val otherUserDislikeSize = MutableLiveData<String>()


    fun getOtherUserProfile(email: String) {
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


                    var like = it["like"] as List<String>
                    var dislike = it["dislike"] as List<*>

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

    fun likeAndDislikeControl(email: String) {
        if (auth.currentUser != null) {
            val profileRef = db.collection("Profile")
                .document(email)

            profileRef.get().addOnSuccessListener {
                if (it != null) {


                    val likeList = it["like"] as List<String>
                    val dislikeList = it["dislike"] as List<String>

                    otherUserLikeSize.value = likeList.size.toString()
                    otherUserDislikeSize.value = dislikeList.size.toString()

                    for (like in likeList) {
                        if (like == auth.currentUser.email) {
                            otherUserLikeControl.value = true
                            break
                        } else {
                            otherUserLikeControl.value = false
                        }
                    }




                    for (dislike in dislikeList) {
                        if (dislike == auth.currentUser.email) {
                            otherUserDislikeControl.value = true

                            break
                        } else {
                            otherUserDislikeControl.value = false
                        }
                    }
                }
            }
        }
    }

    fun otherUserProfileLike(email: String) {
        if (auth.currentUser != null) {
            val profileRef = db.collection("Profile")
                .document(email)

            profileRef.get().addOnSuccessListener {
                if (it != null) {

                    var likeList = it["like"] as List<String>

                    if (likeList.isEmpty()) {
                        likeList = listOf("")
                    }



                    for (like in likeList) {
                        if (like == auth.currentUser.email) {

                            profileRef.update(
                                "like",
                                FieldValue.arrayRemove(auth.currentUser.email)
                            )
                            break
                        } else {
                            profileRef.update(
                                "like",
                                FieldValue.arrayUnion(auth.currentUser.email)
                            )

                        }
                    }

                }
            }
        }
    }

    fun otherUserProfileDislike(email: String) {
        if (auth.currentUser != null) {
            val profileRef = db.collection("Profile")
                .document(email)

            profileRef.get().addOnSuccessListener {
                if (it != null) {

                    var dislikeList = it["dislike"] as List<String>

                    if (dislikeList.isEmpty()) {
                        dislikeList = listOf("")
                    }


                    for (like in dislikeList) {
                        if (like == auth.currentUser.email) {

                            profileRef.update(
                                "dislike",
                                FieldValue.arrayRemove(auth.currentUser.email)
                            )
                            break
                        } else {
                            profileRef.update(
                                "dislike",
                                FieldValue.arrayUnion(auth.currentUser.email)
                            )

                        }
                    }

                }
            }
        }
    }
}