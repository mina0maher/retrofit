package com.example.retrofit.apis

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitFactory {
    fun apiInterface(): ApiInterface {
        return Retrofit.Builder()
            .baseUrl("https://android-training.appssquare.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)
    }
}