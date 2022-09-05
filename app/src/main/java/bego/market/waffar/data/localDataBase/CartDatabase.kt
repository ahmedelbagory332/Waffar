package bego.market.waffar.data.localDataBase

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Cart::class], version = 1, exportSchema = false)
abstract class CartDatabase : RoomDatabase() {

    abstract fun cartDatabaseDao(): CartDatabaseDao

}
