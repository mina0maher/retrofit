package com.example.retrofit.presenters

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.retrofit.apis.RetrofitFactory
import com.example.retrofit.interfaces.ProductsActivityInterface
import com.example.retrofit.models.ProductsModel
import com.example.retrofit.utilities.PreferenceManager
import retrofit2.Call
import retrofit2.Response

class ProductsActivityPresenter (val context: Context,val productsActivityInterface: ProductsActivityInterface){
    private lateinit var data : ProductsModel
    fun getData(){
        val retrofit = RetrofitFactory().apiInterface()
        val call = retrofit.getData()
        if(isOnline(context)){
            call.enqueue(object : retrofit2.Callback<ProductsModel>{
                override fun onResponse(call: Call<ProductsModel>, response: Response<ProductsModel>) {
                    data = response.body()!!
                    productsActivityInterface.onGetData(data)
                }

                override fun onFailure(call: Call<ProductsModel>, t: Throwable) {
                    productsActivityInterface.pushDialog(t.message.toString())
                }

            })
        }else{

            productsActivityInterface.pushDialog("no internet connection do you want try again ?")

        }
    }
    fun logout(){
        val preferenceManager = PreferenceManager(context)
        preferenceManager.clear()
        productsActivityInterface.onLogoutClicked()
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