package bego.market.belbies.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import bego.market.belbies.Models.Section
import bego.market.belbies.Network.ApiClient
import bego.market.belbies.R
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SectionsViewModel() : ViewModel() {



    private val _connectionError = MutableLiveData<String>()
    private val _sections = MutableLiveData<Section>()

    private val _imageList = MutableLiveData<ArrayList<SlideModel>>()

    val imageList: LiveData<ArrayList<SlideModel>>
        get() = _imageList

    val connectionError: LiveData<String>
        get() = _connectionError

    val sections: LiveData<Section>
        get() = _sections


    fun setUpSlider(){
        val list = ArrayList<SlideModel>()

        list.add(SlideModel(R.drawable.shipsproduct, "منتجات الشيبسى",ScaleTypes.CENTER_INSIDE))
        list.add(SlideModel(R.drawable.milkproduct, "منتجات الالبان",ScaleTypes.CENTER_INSIDE))
        list.add(SlideModel(R.drawable.packageproduct,"المعلبات",ScaleTypes.CENTER_INSIDE))
        list.add(SlideModel(R.drawable.cleanproduct, "المنظفات",ScaleTypes.CENTER_INSIDE))
        list.add(SlideModel(R.drawable.drinkproduct,"المشروبات", ScaleTypes.CENTER_INSIDE))
        _imageList.value = list
    }



    fun getSections() {


        ApiClient().getINSTANCE()?.getSections()
            ?.enqueue(object : Callback<Section> {
                override fun onFailure(call: Call<Section>, t: Throwable) {

                    _connectionError.value = "حدث خطأ اثناء تحميل المنتجات"


                }

                override fun onResponse(call: Call<Section>, response: Response<Section>) {

                    _sections.value = response.body()!!

                }

            })


    }





}