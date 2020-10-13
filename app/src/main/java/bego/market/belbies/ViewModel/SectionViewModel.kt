package bego.market.belbies.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import bego.market.belbies.Models.ProductsSection
import bego.market.belbies.Network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SectionViewModel() : ViewModel() {



    private val _sectionName = MutableLiveData<String>()
    private val _connectionError = MutableLiveData<String>()
    private val _emptyList = MutableLiveData<String>()
    private val _products = MutableLiveData<ProductsSection>()


    val sectionName: LiveData<String>
        get() = _sectionName

    val emptyList: LiveData<String>
        get() = _emptyList

    val connectionError: LiveData<String>
        get() = _connectionError

    val products: LiveData<ProductsSection>
        get() = _products

    fun getSectionName(name: String): LiveData<String> {
        _sectionName.value = name
         return sectionName
    }


    fun getProductsSection() {


        ApiClient().getINSTANCE()?.getProductsSection(sectionName.value!!)
            ?.enqueue(object : Callback<ProductsSection> {
                override fun onFailure(call: Call<ProductsSection>, t: Throwable) {

                    _connectionError.value = "حدث خطأ اثناء تحميل المنتجات"



                }

                override fun onResponse(call: Call<ProductsSection>, response: Response<ProductsSection>) {

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