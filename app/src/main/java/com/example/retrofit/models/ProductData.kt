package com.example.retrofit.models

data class ProductData(
    val id: Int,
    val image: String,
    val name: String,
    val price: Int,
    val quantity: Int,
    val restaurant_id: Int
)