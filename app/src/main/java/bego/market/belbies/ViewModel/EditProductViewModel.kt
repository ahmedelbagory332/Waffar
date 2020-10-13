package bego.market.belbies.ViewModel

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import bego.market.belbies.FileUtils
import bego.market.belbies.Models.Section
import bego.market.belbies.Models.UplaodProduct
import bego.market.belbies.Models.UplaodSection
import bego.market.belbies.Network.ApiClient
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.lang.Exception
import java.util.*

class EditProductViewModel(application: Application) : AndroidViewModel(application) {






    private val _response = MutableLiveData<UplaodProduct>()
    private val _connectionError = MutableLiveData<String>()




    val connectionError: LiveData<String>
        get() = _connectionError




    val response: LiveData<UplaodProduct>
        get() = _response




    fun editProduct( id:Int ,product_name:String,  product_des:String,  product_price:String,  product_section:String, product_offer_price:String,  product_offer_percentage:String) {


       try {
           ApiClient().getINSTANCE()?.editProduct(id,product_name,  product_des,  product_price,  product_section, product_offer_price,  product_offer_percentage)?.enqueue(object : Callback<UplaodProduct> {
               override fun onResponse(call: Call<UplaodProduct>, response: Response<UplaodProduct>) {
                   _response.value = response.body()
               }

               override fun onFailure(call: Call<UplaodProduct>, t: Throwable) {
                   _connectionError.value  = t.message
                   Log.d("TAG", "onFailure: ${t}")
               }
           })
       }catch (c:Exception){
           Log.d("TAG", "onFailure: ${c.message}")

       }



    }



}