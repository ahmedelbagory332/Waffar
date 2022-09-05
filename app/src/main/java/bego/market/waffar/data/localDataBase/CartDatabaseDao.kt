package bego.market.waffar.data.localDataBase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDatabaseDao {

    @Insert
    suspend fun insert(cart: Cart)

    @Query("DELETE FROM cart_table WHERE id = :id")
    suspend fun deleteProduct(id: Long): Unit

    @Query("SELECT * FROM cart_table ORDER BY productId ASC")
     fun getAllProducts(): Flow<MutableList<Cart>>

//    @Query("SELECT * from cart_table WHERE productId = :id")
//     fun getProductWithId(id: String): Flow<Cart>
}

