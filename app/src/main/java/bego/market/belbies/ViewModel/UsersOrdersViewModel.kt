package bego.market.belbies.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import bego.market.belbies.Models.AllUsersOrders
import bego.market.belbies.Network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UsersOrdersViewModel() : ViewModel() {




    private val _connectionError = MutableLiveData<String>()
    private val _emptyList = MutableLiveData<String>()
    private val _userOrders = MutableLiveData<AllUsersOrders>()




    val emptyList: LiveData<String>
        get() = _emptyList

    val connectionError: LiveData<String>
        get() = _connectionError

    val allUsersOrders: LiveData<AllUsersOrders>
        get() = _userOrders


    fun getUsersOrders() {


        ApiClient().getINSTANCE()?.getUsersOrders()
            ?.enqueue(object : Callback<AllUsersOrders> {
                override fun onFailure(call: Call<AllUsersOrders>, t: Throwable) {

                    _connectionError.value = "حدث خطأ اثناء تحميل المنتجات"



                }

                override fun onResponse(call: Call<AllUsersOrders>, response: Response<AllUsersOrders>) {

                    if (response.body()!!.UsersOrders.isEmpty()) {
                        _emptyList.value = "لا توجد منتجات"
                    }
                    else {
                        _userOrders.value = response.body()!!
                    }
                }

            })


    }

}