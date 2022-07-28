package com.example.retrofit.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import com.example.retrofit.R
import de.hdodenhof.circleimageview.CircleImageView


class ViewProductActivity : AppCompatActivity() {
    private lateinit var productName : TextView
    private lateinit var productPrice : TextView
    private lateinit var productImage : CircleImageView
    private lateinit var imageProgressBar: ProgressBar
    private lateinit var nameProgressBar: ProgressBar
    private lateinit var priceProgressBar: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_product)
        initViews()

    }

    private fun initViews(){
        productName=findViewById(R.id.item_name)
        productPrice=findViewById(R.id.item_price)
        productImage=findViewById(R.id.item_image)
        imageProgressBar=findViewById(R.id.image_progress_bar)
        nameProgressBar=findViewById(R.id.name_progress_bar)
        priceProgressBar=findViewById(R.id.price_progress_bar)
    }

}