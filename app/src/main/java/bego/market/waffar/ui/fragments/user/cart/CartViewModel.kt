package bego.market.waffar.ui.fragments.user.cart

import androidx.lifecycle.*
import bego.market.waffar.data.localDataBase.Cart
import bego.market.waffar.data.localDataBase.CartDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(db: CartDatabase) : ViewModel() {

    private val _cart = MutableStateFlow(0) // just for notify product added
    private val _allProduct = MutableStateFlow<MutableList<Cart>>(mutableListOf())
    private val _productId = MutableStateFlow(0) // just for notify product deleted

    private val cartDao = db.cartDatabaseDao()



    val productId: LiveData<Int>
        get() = _productId.asLiveData()

    val allProduct: LiveData<MutableList<Cart>>
        get() = _allProduct.asLiveData()

    val cart: LiveData<Int>
        get() = _cart.asLiveData()

    fun setProductId(id:Int){
        viewModelScope.launch {
            _productId.emit(id)
        }
    }

    fun setProductInCart(cartProduct: Cart) {

        viewModelScope.launch(Dispatchers.IO) {
            _cart.emit(0)
            cartDao.insert(cartProduct)
            _cart.emit(1)
        }
    }

    fun removeProductInCart(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            cartDao.deleteProduct(id)
            _productId.emit(1)
        }
    }

    fun getProductsFromCart() {
        viewModelScope.launch(Dispatchers.IO) {
            cartDao.getAllProducts().collect{
                _allProduct.emit(it)
            }

        }
    }


    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}