package com.example.retrofit.utilities

import android.content.Context
import android.widget.Toast

object Constants {
    const val KEY_IS_SIGNED_IN = "isSignedIn"
    const val KEY_PRODUCT_ID="productId"
    const val KEY_PREFERENCE_NAME = "chatAppPreference"

    private var toast: Toast? = null
     fun showToast(message: String,context: Context) {
        if(toast!=null){
            toast!!.cancel()

        }
        toast =  Toast.makeText(context,message,Toast.LENGTH_SHORT)
        toast!!.show()
    }
}