package bego.market.belbies.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import bego.market.belbies.Models.*
import bego.market.belbies.Network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel() : ViewModel() {



    private val _connectionError = MutableLiveData<String>()
    private val _serverResponse = MutableLiveData<String>()



    val connectionError: LiveData<String>
        get() = _connectionError

    val serverResponse: LiveData<String>
        get() = _serverResponse





    fun loginUser(email:String,password:String) {


        ApiClient().getINSTANCE()?.signIn(email,password)?.enqueue(object : Callback<SignIn> {
                override fun onFailure(call: Call<SignIn>, t: Throwable) {

                    _connectionError.value = "حدث خطأ"

                }

                override fun onResponse(call: Call<SignIn>, response: Response<SignIn>) {

                        _serverResponse.value = response.body()!!.response
                }

            })


    }


}