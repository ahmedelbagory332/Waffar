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
import bego.market.belbies.Models.*
import bego.market.belbies.Network.ApiClient
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.*

class AddReportViewModel() : ViewModel() {


    private val _response = MutableLiveData<Report>()
    private val _connectionErrorUploadSection = MutableLiveData<String>()

    private val _reportResponse = MutableLiveData<AllReports>()
    private val _emptyList = MutableLiveData<String>()

    private val _status = MutableLiveData<String>()

    val connectionErrorUploadSection: LiveData<String>
        get() = _connectionErrorUploadSection

    val response: LiveData<Report>
        get() = _response

    val emptyList: LiveData<String>
        get() = _emptyList

    val reportResponse: LiveData<AllReports>
        get() = _reportResponse

    val status: LiveData<String>
        get() = _status


    fun uploadReport(userReport:String,textReport:String) {

        ApiClient().getINSTANCE()?.addReport(userReport,textReport)?.enqueue(object : Callback<Report> {
            override fun onResponse(call: Call<Report>, response: Response<Report>) {
               _response.value = response.body()
            }

            override fun onFailure(call: Call<Report>, t: Throwable) {
                    _connectionErrorUploadSection.value  = "حدث خطأ اثناء الارسال"
            }
        })


    }


    fun getReports() {

        ApiClient().getINSTANCE()?.getAllReports()?.enqueue(object : Callback<AllReports> {
            override fun onResponse(call: Call<AllReports>, response: Response<AllReports>) {

                if (response.body()!!.reports.isEmpty()) {
                    _emptyList.value = "لا توجد تقارير"
                }
                else {
                    _reportResponse.value = response.body()!!
                }
            }

            override fun onFailure(call: Call<AllReports>, t: Throwable) {
                _connectionErrorUploadSection.value  = "حدث خطأ اثناء الارسال"
            }
        })


    }


    fun deleteReports(id:String) {

        ApiClient().getINSTANCE()?.deleteReport(id)?.enqueue(object : Callback<DeleteUserOrders> {
            override fun onResponse(call: Call<DeleteUserOrders>, response: Response<DeleteUserOrders>) {

                _status.value = response.body()?.status

            }

            override fun onFailure(call: Call<DeleteUserOrders>, t: Throwable) {
                _status.value  = "حدث خطأ"
            }
        })


    }

}