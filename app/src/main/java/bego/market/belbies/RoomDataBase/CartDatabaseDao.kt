package bego.market.belbies.ViewModel

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CartDatabaseDao {

    @Insert
    fun insert(cart: Cart)


    @Query("DELETE FROM cart_table WHERE productId = :id")
     fun deleteProduct(id: String): Unit

    @Query("SELECT * FROM cart_table ORDER BY productId ASC")
    fun getAllProducts(): LiveData<MutableList<Cart>>

    @Query("SELECT * from cart_table WHERE productId = :id")
    fun getProductWithId(id: String): LiveData<Cart>
}

