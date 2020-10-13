package bego.market.belbies.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import bego.market.belbies.Models.*
import bego.market.belbies.Network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VerifiedViewModel() : ViewModel() {



    private val _connectionError = MutableLiveData<String>()
    private val _serverResponse = MutableLiveData<String>()



    val connectionError: LiveData<String>
        get() = _connectionError

    val serverResponse: LiveData<String>
        get() = _serverResponse





    fun codeVerified(email:String,code:String) {


        ApiClient().getINSTANCE()?.codeVerified(email,code)?.enqueue(object : Callback<Verified> {
                override fun onFailure(call: Call<Verified>, t: Throwable) {

                    _connectionError.value = "حدث خطأ"

                }

                override fun onResponse(call: Call<Verified>, response: Response<Verified>) {

                        _serverResponse.value = response.body()!!.response
                }

            })


    }


}