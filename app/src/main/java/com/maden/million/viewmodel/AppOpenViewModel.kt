package com.maden.million.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.onesignal.OneSignal

class AppOpenViewModel: ViewModel() {

    private var db = Firebase.firestore
    private var auth = Firebase.auth

    fun open (){
        val openRef = db.collection("Profile")
            .document(auth.currentUser!!.email!!.toString())

        //Kullanıcının oturum açtığında OneSingal ID'si kontrol ediliyor
        //yoksa oluşturulan id atanıyor.
        //varsa ve mevcut id'yle eşleşmiyorsa değiştiriliyor.
        openRef
            .get().addOnSuccessListener {
                val oneSignalID = it["oneSignalID"]

                openRef.update("userOnlineHour",
                    Timestamp.now())

                if(oneSignalID == null || oneSignalID == "") {
                    openRef.update("oneSignalID",
                        OneSignal.getDeviceState()?.userId)
                }else {
                    if (oneSignalID != OneSignal.getDeviceState()?.userId){
                        openRef.update("oneSignalID",
                            OneSignal.getDeviceState()?.userId)
                    }
                }
            }
    }
}