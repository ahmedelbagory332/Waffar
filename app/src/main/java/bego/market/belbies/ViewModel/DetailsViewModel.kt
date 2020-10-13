package bego.market.belbies.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import bego.market.belbies.Models.ProductDetails
import bego.market.belbies.Network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailsViewModel() : ViewModel() {

    private val _setProductId = MutableLiveData<String>()
    private val _connectionError = MutableLiveData<String>()
    private val _detailsResponse = MutableLiveData<ProductDetails>()

    val productId: LiveData<String>
        get() = _setProductId

    val connectionError: LiveData<String>
        get() = _connectionError

    val detailsResponse: LiveData<ProductDetails>
        get() = _detailsResponse


    fun setProductId(id: String): LiveData<String> {
        _setProductId.value = id
        return productId
    }

    fun getProductDetails() {


        ApiClient().getINSTANCE()?.getProductDetails(productId.value!!)?.enqueue(object : Callback<ProductDetails> {
                override fun onFailure(call: Call<ProductDetails>, t: Throwable) {

                    _connectionError.value = "حدث خطأ اثناء تحميل المنتجات"

                }

                override fun onResponse(call: Call<ProductDetails>, response: Response<ProductDetails>) {
                    _detailsResponse.value = response.body()
                }

            })


    }



}