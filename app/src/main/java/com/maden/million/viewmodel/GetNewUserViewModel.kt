package com.maden.million.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.maden.million.model.InfoData
import com.maden.million.model.NewUserData
import com.maden.million.util.Info
import com.maden.million.util.UserChatList
import java.text.SimpleDateFormat

import java.util.*


class GetNewUserViewModel : ViewModel() {

    private var db = Firebase.firestore
    private var auth = Firebase.auth

    val newUserDataClass = MutableLiveData<NewUserData>()
    val infoDataClass = MutableLiveData<InfoData>()

    val newChatRoomUUID = MutableLiveData<String>()

    var newUser = MutableLiveData<Boolean>()
    var waitUser = MutableLiveData<Boolean>()


    private var accessNewUser: Boolean? = null

    //*date*//

    fun getNewUser() {

        waitUser.value = true
        var newUserCount: Int? = null

        val idRef = db.collection("Profile")
            .document(auth.currentUser!!.email!!)

        //Kullanıcının en son ne zaman kullandığını kontrol ediyoruz.
        if (auth.currentUser?.email != null) {
            idRef
                .get().addOnSuccessListener {
                    val timestamp1 = it["newUserDate"]
                    var countString: String = it["newUserCount"].toString()


                    if (timestamp1 != null && countString != null && countString != "") {
                        val timestamp = timestamp1 as com.google.firebase.Timestamp
                        val firebaseTimestampDay: Timestamp = Timestamp.now()


                        val newUserDay = timestamp.seconds / 60 / 60 / 24
                        val today = firebaseTimestampDay.seconds / 60 / 60 / 24



                        //En son ne zaman kullandığını kontrol ediyoruz.
                        //+ Kullanıcının kaç hakkı olduğunu kontrol ediyoruz.
                        //Eğer bir gün geçtiyse yeni hak kazanıyor.
                        newUserCount = countString.toInt()

                        if (newUserCount!! > 0){
                            accessNewUser = true
                            idRef.update(
                                "newUserCount", newUserCount!! -1
                            )
                        } else {
                            //Eğer hak sayısı yoksa ve
                            //1 gün geçtiyse yeni hak kazanıyor.
                            accessNewUser = if(newUserDay < today) {
                                idRef.update(
                                    "newUserDate", Timestamp.now(),
                                    "newUserCount", 1
                                )
                                true

                            } else {
                                false
                            }
                        }

                    } else {
                        //Eğer böyle bir değer hiç  yoksa oluşturuyoruz.
                        accessNewUser = true
                        idRef.update(
                            "newUserDate", Timestamp.now(),
                            "newUserCount", 1
                        )
                    }
                }.addOnCompleteListener {
                    //Hakkı varsa yeni kullanıcı bulunuyor.
                    if (accessNewUser == true) {
                        searchUSer()

                    } else if (accessNewUser == false) {
                        //Hakkı yoksa kullanıcıya bilgilendirme yapılıyor.
                        waitUser.value = false
                        var info = InfoData(Info.infoNewUser)
                        infoDataClass.value = info
                    }
                }
        }
    }


    fun searchUSer() {

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
                if (id != null) {
                    val randomNumber = (1..id!!).random()

                    idRef
                        .whereEqualTo("id", randomNumber)
                        .get().addOnSuccessListener {
                            for (i in it) {
                                newUserEmail = i["email"].toString()

                                if (UserChatList.userEmail.isEmpty()){
                                    newUser.value = true
                                } else {
                                    if(UserChatList.userEmail.size >= 0){

                                        for (email in UserChatList.userEmail) {
                                            if (newUserEmail == email) {
                                                newUser.value = false
                                                break
                                            } else {
                                                newUser.value = true
                                            }
                                        }
                                    } else {
                                        newUser.value = true
                                        break
                                    }
                                }


                            }
                        }.addOnCompleteListener {

                            if (newUser.value == true) {

                                val profileRef = db.collection("Profile")
                                    .document(newUserEmail!!)
                                    .get().addOnSuccessListener {


                                        val nameSurname = it["name"].toString() + " " +
                                                it["surname"].toString()

                                        val photoUrl = it["photoUrl"].toString()
                                        val username = it["username"].toString()
                                        val aboutMe = it["aboutMe"].toString()
                                        val email = it["email"].toString()


                                        waitUser.value = false

                                        val user = NewUserData(
                                            nameSurname, username,
                                            aboutMe, email, photoUrl
                                        )
                                        newUserDataClass.value = user

                                        val dateRef = db.collection("Profile")
                                            .document(auth.currentUser!!.email!!)
                                        dateRef.update("newUserDate", Timestamp.now())
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

        dbRef.document(auth.currentUser.email)
            .collection("ChatChannel")
            .document(email).set(myChatChannel).addOnSuccessListener {

            }.addOnCompleteListener { }.addOnFailureListener {
                //println(it)
            }




        var name: String

        dbRef.document(auth.currentUser.email)
            .get().addOnSuccessListener {
                name = it["name"].toString() + " " + it["surname"].toString()

                val otherChatChannel = hashMapOf(
                    "fullName" to name,
                    "email" to auth.currentUser.email,
                    "date" to Timestamp.now(),
                    "uuid" to roomUUID.toString()
                )

                val otherDbRef = db.collection("Profile")
                    .document(email)
                    .collection("ChatChannel")
                    .document(auth.currentUser.email)
                    .set(otherChatChannel)
                    .addOnSuccessListener {}
                    .addOnCompleteListener { }


        }



        //OneSignal GELECEK
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


    fun rewardNewUserCount(){
        val idRef = db.collection("Profile")
            .document(auth.currentUser!!.email!!)

        idRef.update(
            "newUserDate", Timestamp.now(),
            "newUserCount", 1
        )
    }
}


/*
                            val timestamp = timestamp1 as com.google.firebase.Timestamp
                            val milliseconds = timestamp.seconds * 1000 + timestamp.nanoseconds / 1000000

                            val sdf = SimpleDateFormat("dd/MM/yyyy")
                            val netDate = Date(milliseconds)
                            val date = sdf.format(netDate).toString()
                            println(date)
 */