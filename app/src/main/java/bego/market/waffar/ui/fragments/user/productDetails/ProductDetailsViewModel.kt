package bego.market.waffar.ui.fragments.user.productDetails

import android.util.Log
import androidx.lifecycle.*
import bego.market.waffar.models.ProductDetails
import bego.market.waffar.data.network.ApiInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(private val api: ApiInterface) : ViewModel() {

    private val _connectionError = MutableStateFlow("")
    private val _detailsResponse = MutableStateFlow(ProductDetails("","","","","","","",""))


    val connectionError: LiveData<String>
        get() = _connectionError.asLiveData()

    val detailsResponse: LiveData<ProductDetails>
        get() = _detailsResponse.asLiveData()



    fun getProductDetails(productId:String) {

        viewModelScope.launch(Dispatchers.IO) {

            _connectionError.emit("")
            try {
                val response = api.getProductDetails(productId)
                if (response.isSuccessful) {
                    _detailsResponse.emit(response.body()!!)
                    _connectionError.emit("تم بنجاح")
                } else {
                    _connectionError.emit("حدث خطأ اثناء التحميل")
                    Log.d("TAG", "BEGO1: $response")
                }
            }catch (e:Exception){
                Log.d("TAG", "BEGO2: ${e.message}")
                _connectionError.emit("حدث خطأ اثناء التحميل")
            }


        }

    }



}