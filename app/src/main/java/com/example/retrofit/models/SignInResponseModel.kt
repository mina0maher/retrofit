package com.example.retrofit.models

data class SignInResponseModel(
    val data: SignInData,
    val message: String,
    val status: Boolean,
    val token: String
)