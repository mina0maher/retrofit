package com.example.retrofit.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofit.R
import com.example.retrofit.adapters.ProductsAdapter
import com.example.retrofit.apis.RetrofitFactory
import com.example.retrofit.models.ProductsModel
import retrofit2.Call
import retrofit2.Response


class ProductsActivity : AppCompatActivity() {
    private lateinit var productsRecycler : RecyclerView
    private lateinit var layoutManager: GridLayoutManager
    private lateinit var productsAdapter: ProductsAdapter
    private lateinit var productsLayout : ConstraintLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var data : ProductsModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)
        initView()
        loading(true)
        getData()
    }


    private fun getData(){
        val retrofit = RetrofitFactory().apiInterface()
        val call = retrofit.getData()
        if(isOnline(applicationContext)){
            call.enqueue(object : retrofit2.Callback<ProductsModel>{
                override fun onResponse(call: Call<ProductsModel>, response: Response<ProductsModel>) {
                    data = response.body()!!
                    installRecycler()
                    loading(false)
                }

                override fun onFailure(call: Call<ProductsModel>, t: Throwable) {
                    showToast(t.message.toString())
                }

            })
        }else{
            showToast("check internet connection")
                Thread.sleep(10000)
                getData()
            }
    }
    private fun loading(isLoading: Boolean) {
        if (isLoading) {
            productsLayout.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
            productsLayout.visibility = View.VISIBLE
        }
    }

     private fun isOnline(context: Context): Boolean {
             val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

             val n = cm.activeNetwork
             if (n != null) {
                 val nc = cm.getNetworkCapabilities(n)
                 //It will check for both wifi and cellular network
                 return nc!!.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
             }
             return false
         }
    private fun initView(){
        productsRecycler=findViewById(R.id.products_recycler)
        progressBar = findViewById(R.id.progress_bar)
        productsLayout = findViewById(R.id.products_layout)
    }

    fun installRecycler(){
        layoutManager = GridLayoutManager(applicationContext,2)
        productsRecycler.layoutManager = layoutManager
        productsAdapter = ProductsAdapter(data.data,applicationContext)
        productsRecycler.adapter = productsAdapter
    }
    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }
}