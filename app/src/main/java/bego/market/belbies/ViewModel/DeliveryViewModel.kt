package bego.market.belbies.ViewModel

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import bego.market.belbies.FileUtils
import bego.market.belbies.Models.DeleteUserOrders
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

class DeliveryViewModel() : ViewModel() {


    private val _connectionError = MutableLiveData<String>()
    private val _response = MutableLiveData<DeleteUserOrders>()



    val connectionError: LiveData<String>
        get() = _connectionError

    val response: LiveData<DeleteUserOrders>
        get() = _response





    fun uploadProduct( productUserEmail:String, productNumber:String) {




           ApiClient().getINSTANCE()?.delivery(productUserEmail, productNumber)?.enqueue(object :Callback<DeleteUserOrders>{
               override fun onFailure(call: Call<DeleteUserOrders>, t: Throwable) {
                   _connectionError.value = t.message //"حدث خطأ "

               }

               override fun onResponse(call: Call<DeleteUserOrders>, response: Response<DeleteUserOrders>) {

                   _response.value = response.body()!!

               }

           })



    }






}