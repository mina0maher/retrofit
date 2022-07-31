package com.example.retrofit.ui

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofit.R
import com.example.retrofit.adapters.ProductsAdapter
import com.example.retrofit.interfaces.ProductsActivityInterface
import com.example.retrofit.interfaces.ProductsListener
import com.example.retrofit.models.ProductsModel
import com.example.retrofit.presenters.ProductsActivityPresenter
import com.example.retrofit.utilities.Constants
import com.example.retrofit.utilities.PreferenceManager

class ProductsActivity : AppCompatActivity() ,ProductsListener ,ProductsActivityInterface{
    private lateinit var productsRecycler : RecyclerView
    private lateinit var layoutManager: GridLayoutManager
    private lateinit var productsAdapter: ProductsAdapter
    private lateinit var productsLayout : ConstraintLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var data : ProductsModel
    private lateinit var logoutImage:ImageView
    private lateinit var preferenceManager: PreferenceManager
    private lateinit var productsActivityPresenter: ProductsActivityPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)
        preferenceManager = PreferenceManager(applicationContext)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        productsActivityPresenter = ProductsActivityPresenter(this,this)
        initView()
        setListeners()
        loading(true)
        productsActivityPresenter.getData()
    }


    private fun setListeners(){
        logoutImage.setOnClickListener {
            logout()
        }
    }
    private fun logout(){
        preferenceManager.clear()
        val intent = Intent(this@ProductsActivity, SignInActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
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
        logoutImage = findViewById(R.id.logout)
    }

    private fun installRecycler(){
        layoutManager = GridLayoutManager(applicationContext,2)
        productsRecycler.layoutManager = layoutManager
        productsAdapter = ProductsAdapter(data.data,applicationContext,this)
        productsRecycler.adapter = productsAdapter
    }
    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    override fun onProductClicked(position: Int,productImage:ImageView) {
        if(isOnline(applicationContext)){
            val intent = Intent(this@ProductsActivity, ViewProductActivity::class.java)
            intent.putExtra(Constants.KEY_PRODUCT_ID,position)
            val options :ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this@ProductsActivity,
                productImage,
                ViewCompat.getTransitionName(productImage)!!
            )
            startActivity(intent,options.toBundle())
            }else{
                showToast("check internet connection")
            }
    }

    override fun onGetData(productsModel: ProductsModel) {
        data = productsModel
        installRecycler()
        loading(false)
    }


}