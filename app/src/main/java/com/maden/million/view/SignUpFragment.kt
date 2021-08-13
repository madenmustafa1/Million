package com.maden.million.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.maden.million.R
import com.maden.million.activity.MainActivity
import kotlinx.android.synthetic.main.fragment_sign_up.*


class SignUpFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }


    private lateinit var auth: FirebaseAuth
    private var db = Firebase.firestore

    private var gender: String? = null
    private var name: String? = null
    private var surname: String? = null
    private var email: String? = null
    private var password: String? = null
    private var username: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        db = FirebaseFirestore.getInstance()

        maleBox.setOnClickListener {
            if (femaleBox.isChecked) {
                femaleBox.isChecked = false
            }
        }
        femaleBox.setOnClickListener {
            if (maleBox.isChecked) {
                maleBox.isChecked = false
            }
        }


        signInText.setOnClickListener { intentSignIn() }
        signUpButton.setOnClickListener { signUpF(it) }


    }

    private fun intentSignIn() {
        val action = SignUpFragmentDirections.actionSignUpFragmentToSignInFragment()
        Navigation.findNavController(requireActivity(), R.id.loginContainer).navigate(action)
        if(auth.currentUser.email != null) {
            auth.signOut()
        }
    }

    private fun signUpF(view: View) {
        name = nameTextSignUp.text.toString()
        surname = surnameTextSignUp.text.toString()
        username = usernameTextSignUp.text.toString()
        email = emailTextSignUp.text.toString()
        password = passwordTextSignUp.text.toString()

        gender = when {
            maleBox.isChecked -> {
                "male"
            }
            femaleBox.isChecked -> {
                "female"
            }
            else -> {
                null
            }
        }

        if (name != null && surname != null &&
            email != null && password != null &&
            gender != null && username != null
        ) {

            auth.createUserWithEmailAndPassword(email!!, password!!)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        auth.currentUser!!.sendEmailVerification()
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    Toast.makeText(
                                        context,
                                        "Mailinize gelen aktivasyonu onaylayın",
                                        Toast.LENGTH_LONG
                                    ).show()

                                    /*
                                    if(auth.currentUser?.email != null) {
                                        auth.signOut()
                                    }
                                     */
                                    profileData()

                                } else {
                                    Toast.makeText(
                                        context,
                                        it.exception!!.message!!, Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                    } else {
                        Toast.makeText(context, it.exception!!.message!!, Toast.LENGTH_LONG).show()
                    }
                }
        }
    }

    fun intentLogin() {
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        activity?.finish()
    }

    private fun profileData() {
        signUpButton.visibility = View.GONE

        var str: String
        var id: Int? = null

        val idRef = db.collection("Profile")
        idRef
            .orderBy("creationDate", Query.Direction.DESCENDING)
            .limit(1)
            .get().addOnSuccessListener {

                if(it != null) {
                    for (i in it) {

                        if (i["id"] != null){
                            str = i["id"].toString()
                            id = str.toInt()

                            if (id != null){
                                id = id!! + 1
                            }
                        } else {
                            id = 1
                        }
                    }
                } else{
                    id = 1
                }
            }.addOnCompleteListener {
                if(id != null){
                    val profile = hashMapOf(
                        "name" to name,
                        "surname" to surname,
                        "username" to username,
                        "email" to email,
                        "gender" to gender,
                        "user" to "user",
                        "like" to listOf<String>(),
                        "dislike" to listOf<String>(),
                        "aboutMe" to "",
                        "facebook" to "",
                        "instagram" to "",
                        "twitter" to "",
                        "photoUrl" to "",
                        "creationDate" to Timestamp.now(),
                        "id" to id,
                        "isActive" to true
                    )

                    val dbRef = db.collection("Profile")

                    //intentLogin()
                    dbRef.document(email!!)
                        .set(profile)
                        .addOnSuccessListener { }
                        .addOnFailureListener { println("Fail$it") }
                        .addOnCompleteListener {
                            intentSignIn()
                        }



                } else {
                    Toast.makeText(
                        context,
                        "Kayıt olurken sorun yaşandı! Lütfen tekrar deneyiniz.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }
}


/*
                    val dbListRef = db.collection("NewUserList")
                    dbListRef
                        .document("emails")
                        .update(
                            "emails",
                            FieldValue.arrayUnion(email),
                            "emailSize",
                            FieldValue.increment(1)
                        )
 */