package bego.market.belbies.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import bego.market.belbies.Models.ProductOfferDetails
import bego.market.belbies.Network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OffersDetailsViewModel() : ViewModel() {

    private val _ProductId = MutableLiveData<String>()
    private val _connectionError = MutableLiveData<String>()
    private val _detailsResponse = MutableLiveData<ProductOfferDetails>()

    val productId: LiveData<String>
        get() = _ProductId

    val connectionError: LiveData<String>
        get() = _connectionError

    val detailsResponse: LiveData<ProductOfferDetails>
        get() = _detailsResponse


    fun setProductId(id: String): LiveData<String> {
        _ProductId.value = id
        return productId
    }

    fun getProductOffersDetails() {


        ApiClient().getINSTANCE()?.getOffersDetails(productId.value!!)?.enqueue(object : Callback<ProductOfferDetails> {
                override fun onFailure(call: Call<ProductOfferDetails>, t: Throwable) {

                    _connectionError.value = "حدث خطأ اثناء تحميل المنتجات"

                }

                override fun onResponse(call: Call<ProductOfferDetails>, response: Response<ProductOfferDetails>) {
                    _detailsResponse.value = response.body()
                }

            })


    }



}