package bego.market.belbies.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import bego.market.belbies.Models.*
import bego.market.belbies.Network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderViewModel() : ViewModel() {



    private val _connectionError = MutableLiveData<String>()
    private val _serverResponse = MutableLiveData<String>()



    val connectionError: LiveData<String>
        get() = _connectionError

    val serverResponse: LiveData<String>
        get() = _serverResponse





    fun addOrder(product_name:String,  product_img:String,  product_total_price:String,  product_user_email:String, user_address:String) {


        ApiClient().getINSTANCE()?.addOrder(product_name,  product_img,  product_total_price,  product_user_email, user_address)?.enqueue(object : Callback<Orders> {
                override fun onFailure(call: Call<Orders>, t: Throwable) {

                    _connectionError.value = "حدث خطأ"

                }

                override fun onResponse(call: Call<Orders>, response: Response<Orders>) {

                        _serverResponse.value = response.body()!!.response
                }

            })


    }


}