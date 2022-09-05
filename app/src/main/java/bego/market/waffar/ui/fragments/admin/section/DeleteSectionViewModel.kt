package bego.market.waffar.ui.fragments.admin.section

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import bego.market.waffar.models.DeleteModel
import bego.market.waffar.data.network.ApiInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeleteSectionViewModel @Inject constructor(private val api: ApiInterface) : ViewModel() {




    private val _connectionError = MutableStateFlow("")
     private val _response = MutableStateFlow(DeleteModel(""))



    val connectionError: LiveData<String>
        get() = _connectionError.asLiveData()

    val response: LiveData<DeleteModel>
        get() = _response.asLiveData()


    fun restVariables() {
        viewModelScope.launch(Dispatchers.IO) {
            _connectionError.emit("")
            _response.emit(DeleteModel(""))
        }
    }

    fun deleteSection(sectionName:String) {

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = api.deleteSection(sectionName)
                if (response.isSuccessful) {
                    _response.emit(response.body()!!)
                } else {
                    _connectionError.emit("حدث خطأ اثناء الحذف")
                }
            }catch (e:Exception){
                _connectionError.emit("حدث خطأ اثناء الحذف")
            }

        }


    }

}