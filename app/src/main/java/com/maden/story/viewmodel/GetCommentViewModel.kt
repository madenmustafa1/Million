package com.maden.story.viewmodel

import android.R.attr.data
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.maden.story.model.CommentAdapterData
import java.util.*
import kotlin.collections.ArrayList


class GetCommentViewModel: ViewModel() {


    private var db = Firebase.firestore
    private var auth = Firebase.auth

    val commentDataClass = MutableLiveData<List<CommentAdapterData>>()
    private var nameSurname: String? = null

    val profilePhoto = MutableLiveData<ArrayList<String>>()


    fun getComment(uuid: String){
        val commentRef = db.collection("Story")

        commentRef
            .whereEqualTo("uuid", uuid)
            .get().addOnSuccessListener {
                if (it != null){

                    for (i in it){

                        // buradan yorumlar çekilecek.
                        //i["comment"]
                        var stars: MutableMap<String, String> = HashMap()

                        //println(i["comment"])
                        var commentList = i["comment"] as ArrayList<String>



                        /*
                        for (co in commentList){
                            println(co)

                            val objects = listOf(co)
                            println(objects[0])
                        }

                         */



                    }
                }
            }
    }
}