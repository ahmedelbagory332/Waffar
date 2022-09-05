package bego.market.waffar.ui.fragments.user.userAuthentication

import androidx.lifecycle.*
import bego.market.waffar.data.network.ApiInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VerifiedViewModel  @Inject constructor(private val api: ApiInterface) : ViewModel() {


    private val _connectionError = MutableStateFlow("")
    private val _serverResponse = MutableStateFlow("")


    val connectionError: LiveData<String>
        get() = _connectionError.asLiveData()

    val serverResponse: LiveData<String>
        get() = _serverResponse.asLiveData()



    fun codeVerified(email:String,code:String) {


        viewModelScope.launch(Dispatchers.IO) {
            try{
                val response = api.codeVerified(email,code)
                if (response.isSuccessful) {
                    _serverResponse.emit(response.body()!!.response)
                } else {
                    _connectionError.emit("حدث خطأ")
                }
            }catch (e:Exception){
                _connectionError.emit("حدث خطأ")
            }
        }

    }


}