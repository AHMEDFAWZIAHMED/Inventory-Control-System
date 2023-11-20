package a7m3d.business.inventorycontrol.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Product::class, Transaction::class, Report::class],
                    version = 1)
abstract class ProductRoomDatabase: RoomDatabase() {

    abstract fun productDao(): ProductDao
    abstract fun transactionDao(): TransactionDao
    abstract fun reportDao(): ReportDao
    //Add 'exportSchema = false' in @Database.. for tests
    //Change Room.databaseBuilder to Room.inMemoryDatabaseBuilder.. for test
    //String name in Room.databaseBuilder params is "product_database"

    companion object {
        @Volatile
        private var INSTANCE: ProductRoomDatabase? = null

        fun getDatabase(context: Context): ProductRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ProductRoomDatabase::class.java,
                    "product_database"
                )
                    //.allowMainThreadQueries()// Not recommended
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

}
