package com.example.retrofit.interfaces

import com.example.retrofit.models.ProductModel

interface ViewActivityInterface {
    fun onGetProductModel(productModel: ProductModel)
    fun pushToast(text:String)
}