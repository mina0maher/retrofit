package com.example.retrofit.interfaces

import android.media.Image
import android.widget.ImageView

interface ProductsListener {
    fun onProductClicked(position:Int,productImage:ImageView)
}