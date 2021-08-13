package com.maden.million.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.auth.User
import com.google.firebase.ktx.Firebase
import com.maden.million.R
import com.maden.million.activity.MainActivity
import com.maden.million.model.UserModel
import com.maden.million.service.RequestUser
import com.maden.million.service.UserAPI
import com.maden.million.util.ServerInfo
import com.maden.million.util.UserData
import kotlinx.android.synthetic.main.fragment_sign_in.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SignInFragment : Fragment() {


    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        signUpText.setOnClickListener { intentSignUp() }
        signInButton.setOnClickListener { signIn(view) }

        login("", "")
    }


    private fun login(email: String, password: String){
        val retrofit = Retrofit.Builder()
            .baseUrl(ServerInfo.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(UserAPI::class.java)

        //(Test şifresidir.)
        val call = service.loginUser("test.maden@gmail.com", "123456")

        call.enqueue(object : Callback<RequestUser> {
            override fun onResponse(call: Call<RequestUser>, response: Response<RequestUser>) {
                if(response.isSuccessful){
                    response.body()?.let {

                        println(it.message)
                        //println(it.token)
                        UserData.loginTOKEN = it.token
                        println(UserData.loginTOKEN)

                    }
                }
            }

            override fun onFailure(call: Call<RequestUser>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }



    private fun intentSignUp(){
        val action = SignInFragmentDirections.actionSignInFragmentToSignUpFragment()
        Navigation.findNavController(requireActivity(), R.id.loginContainer).navigate(action)
    }

    private var email: String? = null
    private var password: String? = null
    private fun signIn(view: View){

        email = emailTextSignIn.text.toString()
        password = passwordTextSignIn.text.toString()


        if (email != null && password != null) {

            auth.signInWithEmailAndPassword(email!!, password!!)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        if(auth.currentUser!!.isEmailVerified){
                            intentSignIn()
                        } else {
                            Toast.makeText(context,
                                "Mailinize gelen aktivasyonu onaylayın",
                                Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Toast.makeText(context,
                            it.exception?.localizedMessage.toString(),
                            Toast.LENGTH_LONG).show()
                    }
                }
        }

    }

    private fun intentSignIn(){
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        activity?.finish()
    }

}
