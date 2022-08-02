package com.example.retrofit.presenters

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.retrofit.apis.RetrofitFactory
import com.example.retrofit.interfaces.ViewActivityInterface
import com.example.retrofit.models.ProductModel
import retrofit2.Call
import retrofit2.Response

class ViewActivityPresenter (val context: Context,val viewActivityInterface: ViewActivityInterface){


     fun getData(productId:Int){
        val retrofit = RetrofitFactory().apiInterface()
        val call = retrofit.getProduct(productId)
        if(isOnline(context)){
            call.enqueue(object : retrofit2.Callback<ProductModel>{
                override fun onResponse(call: Call<ProductModel>, response: Response<ProductModel>) {
                    val productModel: ProductModel = response.body()!!
                   viewActivityInterface.onGetProductModel(productModel)
                }

                override fun onFailure(call: Call<ProductModel>, t: Throwable) {
                    viewActivityInterface.pushDialog(t.message.toString())
                }

            })
        }else{
            viewActivityInterface.pushDialog("no internet connection do you want try again ?")
        }
    }

    private fun isOnline(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val n = cm.activeNetwork
        if (n != null) {
            val nc = cm.getNetworkCapabilities(n)
            //It will check for both wifi and cellular network
            return nc!!.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || nc.hasTransport(
                NetworkCapabilities.TRANSPORT_WIFI)
        }
        return false
    }


}