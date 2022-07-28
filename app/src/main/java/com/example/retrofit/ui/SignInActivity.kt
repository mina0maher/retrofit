package com.example.retrofit.ui

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.example.retrofit.R
import com.example.retrofit.apis.RetrofitFactory
import com.example.retrofit.models.SignInResponseModel
import com.example.retrofit.models.UserModel
import retrofit2.Call
import retrofit2.Response
import java.lang.Exception

class SignInActivity : AppCompatActivity() {
    private lateinit var buttonSignIn :Button
    private lateinit var progressBar: ProgressBar
    private lateinit var inputEmail:EditText
    private lateinit var inputPassword:EditText
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        loading(false)
        setListeners()
    }


    private fun setListeners(){
        buttonSignIn.setOnClickListener {
            if (isValidSignInDetails()){
                try {
                    loading(true)
                    signIn()
                }catch (e:Exception){
                    showToast("try again")
                }

            }
        }
    }

    private fun isOnline(context: Context): Boolean {

            val cm = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

            val n = cm.activeNetwork
            if (n != null) {
                val nc = cm.getNetworkCapabilities(n)
                //It will check for both wifi and cellular network
                return nc!!.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
            }
            return false
        }

    private fun signIn(){
        if(isOnline(applicationContext)){
            val retrofit = RetrofitFactory().apiInterface()
            val call = retrofit.logIn(UserModel(inputEmail.text.toString(),inputPassword.text.toString()))
            call.enqueue(object :retrofit2.Callback<SignInResponseModel>{
                override fun onResponse(
                    call: Call<SignInResponseModel>,
                    response: Response<SignInResponseModel>
                ) {

                    if(response.code()==200){
                        startActivity(Intent(this@SignInActivity,ProductsActivity::class.java))
                    }else{
                        showToast("error ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<SignInResponseModel>, t: Throwable) {
                    showToast(t.message.toString())
                }

            })
        }else{
            showToast("check your internet connection")
        }
    }

    private fun init(){
        buttonSignIn = findViewById(R.id.buttonSignIn)
        progressBar = findViewById(R.id.progressBar)
        inputEmail = findViewById(R.id.inputEmail)
        inputPassword =findViewById(R.id.inputPassword)
    }

    private fun loading(isLoading: Boolean) {
        if (isLoading) {
            buttonSignIn.visibility = View.INVISIBLE
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.INVISIBLE
            buttonSignIn.visibility = View.VISIBLE
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }

    private fun isValidSignInDetails():Boolean
    {
        return if (inputEmail.text.toString().trim().isEmpty()) {
            showToast("Enter email")
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail.text.toString())
                .matches()
        ) {
            showToast("Enter valid email")
            false
        } else if (inputPassword.text.toString().trim().isEmpty()) {
            showToast("Enter password")
            false
        } else {
            true
        }
    }
}