package com.example.retrofit.apis

import com.example.retrofit.models.SignInResponseModel
import com.example.retrofit.models.UserModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiInterface {
    @POST("login")
    fun logIn(@Body userModel: UserModel): Call<SignInResponseModel>
}