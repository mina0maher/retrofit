package com.example.retrofit.interfaces

import com.example.retrofit.models.ProductsModel

interface ProductsActivityInterface {
    fun onGetData(productsModel: ProductsModel)
}