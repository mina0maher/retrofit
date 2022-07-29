package com.example.retrofit.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.retrofit.R
import com.example.retrofit.apis.RetrofitFactory
import com.example.retrofit.models.ProductModel
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Response


class ViewProductActivity : AppCompatActivity() {
    private lateinit var productName : TextView
    private lateinit var productPrice : TextView
    private lateinit var productImage : CircleImageView
    private lateinit var nameProgressBar: ProgressBar
    private lateinit var priceProgressBar: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_product)
        initViews()
        loading(true)
        getData()

    }

    private fun getData(){
        val retrofit = RetrofitFactory().apiInterface()
        val call = retrofit.getProduct(intent.extras!!.getInt("POSITION"))
        if(isOnline(applicationContext)){
            call.enqueue(object : retrofit2.Callback<ProductModel>{
                override fun onResponse(call: Call<ProductModel>, response: Response<ProductModel>) {
                    val productModel: ProductModel = response.body()!!
                    productName.text = productModel.data.name
                    val price = "${productModel.data.price} $"
                    productPrice.text = price
                    Glide.with(View(applicationContext)).load(productModel.data.image).into(productImage)
                    loading(false)
                }

                override fun onFailure(call: Call<ProductModel>, t: Throwable) {
                    showToast(t.message.toString())
                }

            })
        }else{
            showToast("check internet connection")
            Thread.sleep(10000)
            getData()
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

    private fun loading(isLoading:Boolean){
        if(isLoading){
            nameProgressBar.visibility=View.VISIBLE
            productName.visibility=View.INVISIBLE
            priceProgressBar.visibility=View.VISIBLE
            productPrice.visibility=View.INVISIBLE
        }else{
            nameProgressBar.visibility=View.INVISIBLE
            productName.visibility=View.VISIBLE
            priceProgressBar.visibility=View.INVISIBLE
            productPrice.visibility=View.VISIBLE
        }
    }


    private fun initViews(){
        productName=findViewById(R.id.item_name)
        productPrice=findViewById(R.id.item_price)
        productImage=findViewById(R.id.item_image)
        nameProgressBar=findViewById(R.id.name_progress_bar)
        priceProgressBar=findViewById(R.id.price_progress_bar)
    }
    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }

}