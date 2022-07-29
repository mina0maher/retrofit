package com.example.retrofit.apis

import com.example.retrofit.models.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiInterface {
    @POST("login")
    fun logIn(@Body userModel: UserModel): Call<SignInResponseModel>

    @GET("products")
    fun getData(): Call<ProductsModel>

    @GET("products/{id}")
    fun getProduct(@Path("id")productId:Int):Call<ProductModel>
}