package bego.market.belbies.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import bego.market.belbies.Models.UserOrders
import bego.market.belbies.Network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserOrderViewModel() : ViewModel() {




    private val _connectionError = MutableLiveData<String>()
    private val _emptyList = MutableLiveData<String>()
    private val _userOrders = MutableLiveData<UserOrders>()
    private val _userMail = MutableLiveData<String>()




    val emptyList: LiveData<String>
        get() = _emptyList

    val connectionError: LiveData<String>
        get() = _connectionError

    val userOrders: LiveData<UserOrders>
        get() = _userOrders

    val userMail: LiveData<String>
        get() = _userMail

    fun setEmail(mail:String){
        _userMail.value = mail
    }



    fun getUserOrders() {


        ApiClient().getINSTANCE()?.getUserOrder(userMail.value!!)
            ?.enqueue(object : Callback<UserOrders> {
                override fun onFailure(call: Call<UserOrders>, t: Throwable) {

                    _connectionError.value = "حدث خطأ اثناء تحميل المنتجات"



                }

                override fun onResponse(call: Call<UserOrders>, response: Response<UserOrders>) {

                    if (response.body()!!.ordersUser.isEmpty()) {
                        _emptyList.value = "لا توجد منتجات"
                    }
                    else {
                        _userOrders.value = response.body()!!
                    }
                }

            })


    }

}