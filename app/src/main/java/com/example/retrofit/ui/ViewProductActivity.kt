package com.example.retrofit.ui


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import com.bumptech.glide.Glide
import com.example.retrofit.R
import com.example.retrofit.interfaces.ViewActivityInterface
import com.example.retrofit.models.ProductModel
import com.example.retrofit.presenters.ViewActivityPresenter
import com.example.retrofit.utilities.Constants
import de.hdodenhof.circleimageview.CircleImageView
import java.lang.Thread as Thread


class ViewProductActivity : AppCompatActivity() ,ViewActivityInterface {
    private lateinit var productName : TextView
    private lateinit var productPrice : TextView
    private lateinit var productImage : CircleImageView
    private lateinit var nameProgressBar: ProgressBar
    private lateinit var priceProgressBar: ProgressBar
    private lateinit var viewActivityPresenter: ViewActivityPresenter
    private var productId:Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_product)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        initObjects()
        initViews()
        loading(true)
        Thread{viewActivityPresenter.getData(productId)}.start()
    }
    private fun initObjects(){
        viewActivityPresenter = ViewActivityPresenter(applicationContext,this@ViewProductActivity)
        productId = intent.extras!!.getInt(Constants.KEY_PRODUCT_ID)
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


    override fun onGetProductModel(productModel: ProductModel) {
        productName.text = productModel.data.name
        val price = "${productModel.data.price} $"
        productPrice.text = price
        Glide.with(View(applicationContext)).load(productModel.data.image).into(productImage)
        loading(false)
    }

    override fun pushDialog(text: String) {
        this@ViewProductActivity.runOnUiThread {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Error")
            builder.setMessage(text)
            builder.setCancelable(false)
            builder.setIcon(R.drawable.ic_no_internet)
            builder.setPositiveButton("reload") { _, _ ->
                viewActivityPresenter.getData(productId)
            }

            builder.setNegativeButton("exit") { _, _ ->
                finish()
            }


            builder.show()
        }
    }



}