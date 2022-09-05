package bego.market.waffar.ui.fragments.user.sections

import android.util.Log
import androidx.lifecycle.*

import bego.market.waffar.data.network.ApiInterface
import bego.market.waffar.models.AllProduct
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SectionViewModel @Inject constructor(private val api: ApiInterface): ViewModel() {



    private val _connectionError = MutableStateFlow("")
    private val _emptyList = MutableStateFlow("")
    private val _products = MutableStateFlow<MutableList<AllProduct>>(mutableListOf())


    val emptyList: LiveData<String>
        get() = _emptyList.asLiveData()

    val connectionError: LiveData<String>
        get() = _connectionError.asLiveData()

    val products: LiveData<MutableList<AllProduct>>
        get() = _products.asLiveData()


    fun restVariables(){
        viewModelScope.launch(Dispatchers.IO) {
            _emptyList.emit("")
            _connectionError.emit("")
            _products.emit(mutableListOf())

        }
    }


    fun getProductsSection(sectionName:String) {

        viewModelScope.launch(Dispatchers.IO) {
            _connectionError.emit("")
            try {
                val response = api.getProductsSection(sectionName)
                if (response.isSuccessful) {
                    _products.emit(response.body()!!.allProducts)
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