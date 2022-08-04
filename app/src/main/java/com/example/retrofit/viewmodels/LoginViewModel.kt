package com.example.retrofit.viewmodels


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.retrofit.apis.RetrofitFactory
import com.example.retrofit.models.SignInResponseModel
import com.example.retrofit.models.UserModel
import retrofit2.Call
import retrofit2.Response

class LoginViewModel : ViewModel() {
     var codesMD: SingleLiveEvent<Int> = SingleLiveEvent()
     val codesLiveData:LiveData<Int>
          get() = codesMD

    var bodyMD: SingleLiveEvent<SignInResponseModel> = SingleLiveEvent()
    val bodyLiveData:LiveData<SignInResponseModel>
        get() = bodyMD

    var errorMessageMD: SingleLiveEvent<String> = SingleLiveEvent()
    val errorMessageLiveData:LiveData<String>
        get() = errorMessageMD

     fun signIn(userModel: UserModel){
            val retrofit = RetrofitFactory().apiInterface()
            val call = retrofit.logIn(userModel)
            call.enqueue(object :retrofit2.Callback<SignInResponseModel>{
                override fun onResponse(
                    call: Call<SignInResponseModel>,
                    response: Response<SignInResponseModel>
                ) {
                    codesMD.postValue(response.code())
                    bodyMD.postValue(response.body())
                }
                override fun onFailure(call: Call<SignInResponseModel>, t: Throwable) {
                    errorMessageMD.postValue(t.message.toString())
                }

            })
        }
    }

