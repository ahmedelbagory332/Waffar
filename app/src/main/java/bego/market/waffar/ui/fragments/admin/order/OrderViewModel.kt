package bego.market.waffar.ui.fragments.admin.order

import android.util.Log
import androidx.lifecycle.*
import bego.market.waffar.data.network.ApiInterface
import bego.market.waffar.models.DeleteModel
import bego.market.waffar.models.UserOrder
import bego.market.waffar.models.UsersOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(private val api: ApiInterface) : ViewModel() {


    private val _connectionErrorForRequestOrder = MutableStateFlow("")
    private val _connectionErrorForDeleteOrder = MutableStateFlow("")
    private val _connectionErrorForUsersOrder = MutableStateFlow("")
    private val _connectionErrorForDeleteUserOrders = MutableStateFlow("")
    private val _connectionErrorForGetUserOrders = MutableStateFlow("")

    private val _responseForRequestOrder = MutableStateFlow("")
    private val _responseForUsersOrder = MutableStateFlow<MutableList<UsersOrder>>(mutableListOf())
    private val _responseForGetUserOrders = MutableStateFlow<MutableList<UserOrder>>(mutableListOf())
    private val _responseForDeleteOrder = MutableStateFlow(DeleteModel(""))
    private val _responseForDeleteUserOrders = MutableStateFlow(DeleteModel(""))

    private val _emptyListForUsersOrder = MutableStateFlow("")
    private val _emptyListForGetUserOrders = MutableStateFlow("")



    val connectionErrorForGetUserOrders: LiveData<String>
        get() = _connectionErrorForGetUserOrders.asLiveData()

    val connectionErrorForDeleteUserOrders: LiveData<String>
        get() = _connectionErrorForDeleteUserOrders.asLiveData()

    val connectionErrorForDeleteOrder: LiveData<String>
        get() = _connectionErrorForDeleteOrder.asLiveData()

    val connectionErrorForUsersOrder: LiveData<String>
        get() = _connectionErrorForUsersOrder.asLiveData()

    val connectionErrorForRequestOrder: LiveData<String>
        get() = _connectionErrorForRequestOrder.asLiveData()


    val responseForGetUserOrders: LiveData<MutableList<UserOrder>>
        get() = _responseForGetUserOrders.asLiveData()

    val responseForDeleteUserOrders: LiveData<DeleteModel>
        get() = _responseForDeleteUserOrders.asLiveData()


    val responseForDeleteOrder: LiveData<DeleteModel>
        get() = _responseForDeleteOrder.asLiveData()


    val responseForUsersOrder: LiveData<MutableList<UsersOrder>>
        get() = _responseForUsersOrder.asLiveData()


    val responseForRequestOrder: LiveData<String>
        get() = _responseForRequestOrder.asLiveData()

    val emptyListForUsersOrder: LiveData<String>
        get() = _emptyListForUsersOrder.asLiveData()

    val emptyListForGetUserOrders: LiveData<String>
        get() = _emptyListForGetUserOrders.asLiveData()


    fun restVariables() {
        viewModelScope.launch(Dispatchers.IO) {

             _connectionErrorForGetUserOrders.emit("")
             _connectionErrorForDeleteUserOrders.emit("")
             _connectionErrorForDeleteOrder.emit("")
             _connectionErrorForUsersOrder.emit("")
             _connectionErrorForRequestOrder.emit("")

            _responseForGetUserOrders.emit(mutableListOf())
            _responseForDeleteUserOrders.emit(DeleteModel(""))
            _responseForDeleteOrder.emit(DeleteModel(""))
            _responseForUsersOrder.emit(mutableListOf())
            _responseForRequestOrder.emit("")

            _emptyListForUsersOrder.emit("")
           _emptyListForGetUserOrders.emit("")

        }
    }


   suspend fun requestOrder(product_name:String, product_img:String, product_total_price:String, product_user_email:String, user_address:String, product_quantity:String, user_phone:String, user_name:String): Boolean {
        val job = Job()

        viewModelScope.launch(Dispatchers.IO + job) {
            _connectionErrorForRequestOrder.emit("")
            try {
                val response = api.addOrder(product_name,  product_img,  product_total_price,  product_user_email, user_address, product_quantity,user_phone,user_name)
                if (response.isSuccessful) {
                    _responseForRequestOrder.emit(response.body()!!.response)
                    _connectionErrorForRequestOrder.emit("تم بنجاح")
                    job.complete()
                } else {
                    _connectionErrorForRequestOrder.emit("حدث خطأ اثناء التحميل")
                    Log.d("TAG", "BEGO1: $response")
                    job.cancel()
                }
            }catch (e:Exception){
                Log.d("TAG", "BEGO2: ${e.message}")
                _connectionErrorForRequestOrder.emit("حدث خطأ اثناء التحميل")
                job.cancel()
            }
        }.join()
       return job.isCompleted
    }



    fun deleteOrder(productNumber:String) {

        viewModelScope.launch(Dispatchers.IO) {
            try {
                _responseForDeleteOrder.emit(DeleteModel(""))
                _connectionErrorForDeleteOrder.emit("")
                val response = api.deleteOrder(productNumber)
                if (response.isSuccessful) {
                    _responseForDeleteOrder.emit(response.body()!!)
                } else {
                    _connectionErrorForDeleteOrder.emit("حدث خطأ اثناء الحذف")
                }
            }catch (e:Exception){
                _connectionErrorForDeleteOrder.emit("حدث خطأ اثناء الحذف")
            }

        }


    }



    fun deleteUserOrders(mail:String) {

        viewModelScope.launch(Dispatchers.IO) {
            try {
                _responseForDeleteUserOrders.emit(DeleteModel(""))
                _connectionErrorForDeleteUserOrders.emit("")

                val response = api.deleteUserOrders(mail)
                if (response.isSuccessful) {
                    _responseForDeleteUserOrders.emit(response.body()!!)
                } else {
                    _connectionErrorForDeleteUserOrders.emit("حدث خطأ اثناء الحذف")
                }
            }catch (e:Exception){
                _connectionErrorForDeleteUserOrders.emit("حدث خطأ اثناء الحذف")
            }

        }

    }



    fun getUserOrders(userMail:String) {

        viewModelScope.launch(Dispatchers.IO) {
            try {
                _connectionErrorForUsersOrder.emit("")
                val response = api.getUserOrders(userMail)
                if (response.isSuccessful) {
                    if (response.body()!!.UserOrders.isEmpty()) {
                        _emptyListForGetUserOrders.emit( "لا توجد منتجات")
                    }
                    else {
                        _responseForGetUserOrders.emit(response.body()!!.UserOrders)
                        _connectionErrorForUsersOrder.emit("تم بنجاح")
                    }
                } else {
                    _connectionErrorForGetUserOrders.emit("حدث خطأ اثناء تحميل المنتجات")
                }
            }catch (e:Exception){
            Log.d("TAG", "BEGO2: ${e.message}")
            _connectionErrorForUsersOrder.emit("حدث خطأ اثناء تحميل المنتجات")
        }

        }


    }



    fun getUsersOrders() {

        viewModelScope.launch(Dispatchers.IO) {
            try {
                _connectionErrorForUsersOrder.emit("")
                val response = api.getUsersOrder()
                if (response.isSuccessful) {
                    if (response.body()!!.UsersOrders.isEmpty()) {
                        _emptyListForUsersOrder.emit( "لا توجد منتجات")
                    }
                    else {
                        _responseForUsersOrder.emit(response.body()!!.UsersOrders)
                        _connectionErrorForUsersOrder.emit("تم بنجاح")
                    }
                } else {
                    _connectionErrorForUsersOrder.emit("حدث خطأ اثناء تحميل المنتجات")
                }
            }catch (e:Exception){
                Log.d("TAG", "BEGO2: ${e.message}")
                _connectionErrorForUsersOrder.emit("حدث خطأ اثناء تحميل المنتجات")
            }

        }


    }




}