package bego.market.belbies.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import bego.market.belbies.Models.Offer
import bego.market.belbies.Models.ProductOffers
import bego.market.belbies.Models.ProductsSection
import bego.market.belbies.Network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OfferViewModel() : ViewModel() {






    private val _connectionError = MutableLiveData<String>()
    private val _emptyList = MutableLiveData<String>()
    private val _offers = MutableLiveData<ProductOffers>()




    val emptyList: LiveData<String>
        get() = _emptyList

    val connectionError: LiveData<String>
        get() = _connectionError

    val offers: LiveData<ProductOffers>
        get() = _offers



    fun getOffers() {


        ApiClient().getINSTANCE()?.getOffers()?.enqueue(object : Callback<ProductOffers> {
                override fun onFailure(call: Call<ProductOffers>, t: Throwable) {

                    _connectionError.value = "حدث خطأ اثناء تحميل المنتجات"

                }

                override fun onResponse(call: Call<ProductOffers>, response: Response<ProductOffers>) {

                    if (response.body()!!.offers.isEmpty()) {
                        _emptyList.value = "لا توجد عروض"
                    }
                    else {
                        _offers.value = response.body()!!

                    }
                }

            })


    }

}