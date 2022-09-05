package bego.market.waffar.ui.fragments.user.homePage

import android.util.Log
import androidx.lifecycle.*
import bego.market.waffar.models.Section
import bego.market.waffar.data.network.ApiInterface
import bego.market.waffar.R
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomePageViewModel  @Inject constructor(private val api: ApiInterface): ViewModel() {



    private val _connectionError = MutableStateFlow("")
    private val _sections = MutableStateFlow<MutableList<Section>>(mutableListOf())
    private val _imageList = MutableStateFlow<ArrayList<SlideModel>>(ArrayList(mutableListOf()))


    val imageList: LiveData<ArrayList<SlideModel>>
        get() = _imageList.asLiveData()

    val connectionError: LiveData<String>
        get() = _connectionError.asLiveData()

    val sections: LiveData<MutableList<Section>>
        get() = _sections.asLiveData()


    fun setUpSlider(){
        val list = ArrayList<SlideModel>()
        viewModelScope.launch(Dispatchers.Main) {
            list.add(SlideModel(R.drawable.shipsproduct, "منتجات الشيبسى",ScaleTypes.CENTER_INSIDE))
            list.add(SlideModel(R.drawable.milkproduct, "منتجات الالبان",ScaleTypes.CENTER_INSIDE))
            list.add(SlideModel(R.drawable.packageproduct,"المعلبات",ScaleTypes.CENTER_INSIDE))
            list.add(SlideModel(R.drawable.cleanproduct, "المنظفات",ScaleTypes.CENTER_INSIDE))
            list.add(SlideModel(R.drawable.drinkproduct,"المشروبات", ScaleTypes.CENTER_INSIDE))
            _imageList.emit(list)
        }

    }


    fun restVariables(){
        viewModelScope.launch(Dispatchers.IO) {
            _connectionError.emit("")
            _sections.emit(mutableListOf())

        }
    }

    fun getSections() {
        viewModelScope.launch(Dispatchers.IO) {
                    _connectionError.emit("")
            try {
                val response = api.getSections()
                if (response.isSuccessful) {
                    _sections.emit(response.body()!!.sections)
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