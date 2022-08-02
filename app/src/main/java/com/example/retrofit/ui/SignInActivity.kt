package com.example.retrofit.ui

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.retrofit.R
import com.example.retrofit.apis.RetrofitFactory
import com.example.retrofit.models.SignInResponseModel
import com.example.retrofit.models.UserModel
import com.example.retrofit.utilities.Constants
import com.example.retrofit.utilities.PreferenceManager
import retrofit2.Call
import retrofit2.Response

class SignInActivity : AppCompatActivity() {
    private lateinit var buttonSignIn :Button
    private lateinit var progressBar: ProgressBar
    private lateinit var inputEmail:EditText
    private lateinit var inputPassword:EditText
    private lateinit var preferenceManager: PreferenceManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferenceManager = PreferenceManager(applicationContext)
        if(preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)){
            val intent = Intent(this@SignInActivity, ProductsActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            finish()
        }
        setContentView(R.layout.activity_sign_in)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        init()
        loading(false)
        setListeners()
    }


    private fun setListeners(){
        buttonSignIn.setOnClickListener {
            if (isValidSignInDetails()){
                    signIn()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loading(false)
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
        loading(true)
        if(isOnline(applicationContext)){
            val retrofit = RetrofitFactory().apiInterface()
            val call = retrofit.logIn(UserModel(inputEmail.text.toString(),inputPassword.text.toString()))
            call.enqueue(object :retrofit2.Callback<SignInResponseModel>{
                override fun onResponse(
                    call: Call<SignInResponseModel>,
                    response: Response<SignInResponseModel>
                ) {

                    if (response.code() == 200) {
                        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true)
                        val intent = Intent(this@SignInActivity, ProductsActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                    } else if (response.code() == 422) {
                        showToast("email & password not correct")
                        loading(false)
                    }else if(response.code()==401){
                        showToast("password not correct")
                        loading(false)
                    }else{
                        showToast("error ${response.code()}")
                        loading(false)
                    }

                }

                override fun onFailure(call: Call<SignInResponseModel>, t: Throwable) {
                    showToast(t.message.toString())
                    loading(false)
                }

            })
        }else{
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Error")
            builder.setMessage("check your internet connection and try again")
            builder.setCancelable(false)
            builder.setIcon(R.drawable.ic_no_internet)
            builder.setPositiveButton("reload") { _, _ ->
                signIn()
            }

            builder.setNegativeButton("exit") { _, _ ->
                finish()
            }


            builder.show()
            loading(false)
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