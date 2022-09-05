package bego.market.waffar.ui.fragments.report

import androidx.lifecycle.*
import bego.market.waffar.data.network.ApiInterface
import bego.market.waffar.models.Report
import bego.market.waffar.models.Reports
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddReportViewModel @Inject constructor(private val api: ApiInterface) : ViewModel() {


    private val _response = MutableStateFlow(Report(""))
    private val _connectionError = MutableStateFlow("")
    private val _reportResponse = MutableStateFlow<MutableList<Reports>>(mutableListOf())
    private val _emptyList = MutableStateFlow("")
    private val _status = MutableStateFlow("")

    val connectionError: LiveData<String>
        get() = _connectionError.asLiveData()

    val response: LiveData<Report>
        get() = _response.asLiveData()

    val emptyList: LiveData<String>
        get() = _emptyList.asLiveData()

    val reportResponse: LiveData<MutableList<Reports>>
        get() = _reportResponse.asLiveData()

    val status: LiveData<String>
        get() = _status.asLiveData()


    fun uploadReport(userReport:String,textReport:String) {
        viewModelScope.launch(Dispatchers.IO) {
        try {
            _response.emit(Report(""))
            _connectionError.emit("")

            val response = api.addReport(userReport,textReport)
            if (response.isSuccessful) {
                _response.emit(response.body()!!)
            } else {
                _connectionError.emit("حدث خطأ اثناء الارسال")
            }

        }catch (e:Exception){
            _connectionError.emit("حدث خطأ اثناء الارسال")
           }
        }

    }


    fun getReports() {

        viewModelScope.launch(Dispatchers.IO) {
            try {

                _connectionError.emit("")
                val response = api.getAllReports()
                if (response.isSuccessful) {
                    if (response.body()!!.reports.isEmpty()) {
                        _emptyList.emit("لا توجد تقارير")
                    }
                    else {
                        _reportResponse.emit(response.body()!!.reports)
                        _connectionError.emit("تم بنجاح")
                    }

                } else {
                    _connectionError.emit("حدث خطأ")
                }

            }catch (e:Exception){
                _connectionError.emit("حدث خطأ")
            }
        }


    }

    fun restVariables() {
        viewModelScope.launch(Dispatchers.IO) {
            _response.emit(Report(""))
            _connectionError.emit("")
            _emptyList.emit("")
            _status.emit("")
        }
    }


    fun deleteReports(id:String) {
        viewModelScope.launch(Dispatchers.IO) {
        try {
                val response = api.deleteReport(id)
                if (response.isSuccessful) {
                    _status.emit(response.body()!!.status)
                } else {
                    _status.emit("حدث خطأ")
                }

        }catch (e:Exception){
            _status.emit("حدث خطأ")

          }
        }
    }

}