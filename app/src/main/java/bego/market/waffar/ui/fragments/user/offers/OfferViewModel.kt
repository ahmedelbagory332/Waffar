package bego.market.waffar.ui.fragments.user.offers

import android.util.Log
import androidx.lifecycle.*
import bego.market.waffar.data.network.ApiInterface
import bego.market.waffar.models.Offer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OfferViewModel @Inject constructor(private val api: ApiInterface) : ViewModel() {




    private val _connectionError = MutableStateFlow("")
    private val _emptyList = MutableStateFlow("")
    private val _offers = MutableStateFlow<MutableList<Offer>>(mutableListOf())



    val emptyList: LiveData<String>
        get() = _emptyList.asLiveData()

    val connectionError: LiveData<String>
        get() = _connectionError.asLiveData()

    val offers: LiveData<MutableList<Offer>>
        get() = _offers.asLiveData()

    fun restVariables(){
        viewModelScope.launch(Dispatchers.IO) {
            _connectionError.emit("")
            _offers.emit(mutableListOf())
          _emptyList.emit("")


        }
    }


    fun getOffers() {

        viewModelScope.launch(Dispatchers.IO) {
            _connectionError.emit("")
            try {
                val response = api.getOffers()
                if (response.isSuccessful) {
                    _connectionError.emit("تم بنجاح")
                    if (response.body()!!.offers.isEmpty()) {
                        _emptyList.emit("لا توجد عروض")
                    } else {
                        _offers.emit(response.body()!!.offers)

                    }
                } else {
                    _connectionError.emit("حدث خطأ اثناء تحميل المنتجات")
                }
            }catch (e: Exception) {
                Log.d("TAG", "BEGO2: ${e.message}")
                _connectionError.emit("حدث خطأ اثناء التحميل")
            }
        }

    }

}