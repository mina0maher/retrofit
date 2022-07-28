package com.example.retrofit.apis

import com.example.retrofit.models.Data
import com.example.retrofit.models.ProductsModel
import com.example.retrofit.models.SignInResponseModel
import com.example.retrofit.models.UserModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiInterface {
    @POST("login")
    fun logIn(@Body userModel: UserModel): Call<SignInResponseModel>

    @GET("products")
    fun getData(): Call<ProductsModel>

    @GET("products/id")
    fun getProduct():Call<Data>
}