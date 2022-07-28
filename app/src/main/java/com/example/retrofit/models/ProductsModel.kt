package com.example.retrofit.models

data class ProductsModel(
    val count: Int,
    val data: ArrayList<Data>,
    val message: String,
    val status: Boolean
)