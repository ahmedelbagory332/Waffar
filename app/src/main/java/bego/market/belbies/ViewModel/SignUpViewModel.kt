package bego.market.belbies.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import bego.market.belbies.Models.*
import bego.market.belbies.Network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpViewModel() : ViewModel() {



    private val _connectionError = MutableLiveData<String>()
    private val _serverResponse = MutableLiveData<String>()



    val connectionError: LiveData<String>
        get() = _connectionError

    val serverResponse: LiveData<String>
        get() = _serverResponse





    fun addUser(name:String,email:String,password:String) {


        ApiClient().getINSTANCE()?.addUser(name,email,password)?.enqueue(object : Callback<SignUp> {
                override fun onFailure(call: Call<SignUp>, t: Throwable) {

                    _connectionError.value = t.message

                }

                override fun onResponse(call: Call<SignUp>, response: Response<SignUp>) {

                        _serverResponse.value = response.body()!!.response
                }

            })


    }


}