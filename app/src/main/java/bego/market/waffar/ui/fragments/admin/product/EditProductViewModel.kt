package bego.market.waffar.ui.fragments.admin.product

import androidx.lifecycle.*
import bego.market.waffar.data.network.ApiInterface
import bego.market.waffar.models.UploadModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProductViewModel @Inject constructor(private val api: ApiInterface) : ViewModel() {



    private val _connectionError = MutableStateFlow("")
    private val _response = MutableStateFlow(UploadModel(false,""))


    val connectionError: LiveData<String>
        get() = _connectionError.asLiveData()

    val response: LiveData<UploadModel>
        get() = _response.asLiveData()


    fun restEditProductVariables() {
        viewModelScope.launch(Dispatchers.IO) {
            _connectionError.emit("")
            _response.emit(UploadModel(false,""))
        }
    }


    fun editProduct( id:Int ,product_name:String,  product_des:String,  product_price:String,  product_section:String, product_offer_price:String,  product_offer_percentage:String) {

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = api.editProduct(id,product_name,  product_des,  product_price,  product_section, product_offer_price,  product_offer_percentage)
                if (response.isSuccessful) {
                    _response.emit(response.body()!!)
                } else {
                    _connectionError.emit("حدث خطأ")
                }
            }catch (e:Exception){
                _connectionError.emit("حدث خطأ")
            }

        }

    }



}