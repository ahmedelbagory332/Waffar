package bego.market.belbies.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import bego.market.belbies.Models.AdminProducts
import bego.market.belbies.Models.ProductsSection
import bego.market.belbies.Network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminAllProductViewModel() : ViewModel() {





    private val _connectionError = MutableLiveData<String>()
    private val _emptyList = MutableLiveData<String>()
    private val _products = MutableLiveData<AdminProducts>()



    val emptyList: LiveData<String>
        get() = _emptyList

    val connectionError: LiveData<String>
        get() = _connectionError

    val products: LiveData<AdminProducts>
        get() = _products



    fun getProducts() {


        ApiClient().getINSTANCE()?.getAllProduct()
            ?.enqueue(object : Callback<AdminProducts> {
                override fun onFailure(call: Call<AdminProducts>, t: Throwable) {

                    _connectionError.value = "حدث خطأ اثناء تحميل المنتجات"



                }

                override fun onResponse(call: Call<AdminProducts>, response: Response<AdminProducts>) {

                    if (response.body()!!.allProducts.isEmpty()) {
                        _emptyList.value = "لا توجد منتجات"
                    }
                    else {
                        _products.value = response.body()!!
                    }
                }

            })


    }

}