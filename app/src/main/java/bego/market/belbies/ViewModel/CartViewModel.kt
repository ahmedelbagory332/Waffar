package bego.market.belbies.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch


class CartViewModel(val dataSource: CartDatabaseDao, application: Application) : AndroidViewModel(application) {

    private val _cart = MutableLiveData<Cart>()
    private val _productId = MutableLiveData<Int>() // just for notify me product deleted

    val cartProducts = dataSource.getAllProducts()



    val productId: LiveData<Int>
        get() = _productId

    val cart: LiveData<Cart>
        get() = _cart

    fun setProductInCart(cartProduct: Cart) {
        _cart.value = cartProduct
        insertProductInCart(cart.value!!)
    }

    fun removeProductInCart(id: String) {
        _productId.value =+ 1
        deleteProductInCart(id)
    }



    fun insertProductInCart(cart: Cart) = viewModelScope.launch(Dispatchers.IO) {
        dataSource.insert(cart)
    }

    fun deleteProductInCart(id: String) = viewModelScope.launch(Dispatchers.IO) {
        dataSource.deleteProduct(id)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
     }

}