package bego.market.belbies.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import bego.market.belbies.Models.DeleteUserOrders
import bego.market.belbies.Models.UserOrders
import bego.market.belbies.Network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeleteOrderViewModel() : ViewModel() {




    private val _connectionError = MutableLiveData<String>()
     private val _response = MutableLiveData<DeleteUserOrders>()



    val connectionError: LiveData<String>
        get() = _connectionError

    val response: LiveData<DeleteUserOrders>
        get() = _response




    fun deleteOrders(mail:String) {


        ApiClient().getINSTANCE()?.deleteOrder(mail)
            ?.enqueue(object : Callback<DeleteUserOrders> {
                override fun onFailure(call: Call<DeleteUserOrders>, t: Throwable) {

                    _connectionError.value = "حدث خطأ اثناء الحذف"



                }

                override fun onResponse(call: Call<DeleteUserOrders>, response: Response<DeleteUserOrders>) {

                    _response.value = response.body()!!
                }

            })


    }

}