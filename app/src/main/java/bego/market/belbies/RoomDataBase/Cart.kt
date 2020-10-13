package bego.market.belbies.ViewModel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_table")
data class Cart(

        @PrimaryKey(autoGenerate = true)
        var id: Long = 0L,

        @ColumnInfo(name = "productId")
        var productId: String = "",

        @ColumnInfo(name = "productName")
        var productName: String = "",

        @ColumnInfo(name = "productImage")
        var productImage: String = "",

        @ColumnInfo(name = "productQuantity")
        var productQuantity: String = "",

        @ColumnInfo(name = "totalPrice")
        var totalPrice: String = ""
)
