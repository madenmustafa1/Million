package com.maden.million.service

import com.maden.million.model.UserModel
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface UserAPI {



    @FormUrlEncoded
    @POST("your api")
    fun loginUser(
        @Field ("email") email: String,
        @Field ("password") password: String
    ): Call<RequestUser>



}
