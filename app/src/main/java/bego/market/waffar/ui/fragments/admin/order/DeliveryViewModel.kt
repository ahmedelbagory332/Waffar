package bego.market.waffar.ui.fragments.admin.order

import androidx.lifecycle.*
import bego.market.waffar.data.network.ApiInterface
import bego.market.waffar.models.DeliveryModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeliveryViewModel @Inject constructor(private val api: ApiInterface) : ViewModel() {


    private val _connectionError = MutableStateFlow("")
    private val _response = MutableStateFlow(DeliveryModel(""))



    val connectionError: LiveData<String>
        get() = _connectionError.asLiveData()

    val response: LiveData<DeliveryModel>
        get() = _response.asLiveData()





    fun deliveryProduct(productUserEmail:String, productNumber:String) {

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = api.delivery(productUserEmail, productNumber)
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