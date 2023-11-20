package a7m3d.business.inventorycontrol.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import androidx.room.Delete
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Query("SELECT * FROM product WHERE active = 1")
    fun getAll(): Flow<List<Product>>

    @Query("SELECT * FROM product WHERE active = 0")
    fun getDeletedProducts(): Flow<List<Product>>

    @Query("SELECT * FROM product WHERE name LIKE :text AND active = 1")
    fun searchProducts(text: String): Flow<List<Product>>

    @Query("SELECT * FROM product WHERE name LIKE :text AND active = 0")
    fun searchDeletedProducts(text: String): Flow<List<Product>>

    @Query("SELECT name FROM product WHERE active = 1 AND color = :color")
    fun getProductNames(color: Int): Flow<List<String>>

    @Query("SELECT name FROM product WHERE active = 1 AND id = :id")
    fun getName(id: Int): Flow<List<String>>

    @Query("SELECT SUM(quantity) FROM product WHERE active = 1")
    fun getAllQuantity(): Flow<List<Int>>

    @Query("SELECT SUM(quantity) FROM product WHERE category = :category AND active = 1")
    fun getQuantity(category: String): Flow<List<Int>>

    @Query("SELECT category FROM product WHERE active = 1")
    fun getCategory(): Flow<List<String>>

    @Query("SELECT id FROM product WHERE active = 1")
    fun getAllId(): Flow<List<Int>>

    @Query("SELECT subtractedQuantity FROM product WHERE active = 1 AND id = :id")
    fun getTotalQuantityOut(id: Int): Flow<List<Int>>

    @Query("SELECT quantity FROM product WHERE active = 1 AND id = :id")
    fun getQuantityInStock(id: Int): Flow<List<Int>>

    @Query("SELECT * FROM product WHERE category = :category AND active = 1")
    fun getProducts(category: String): Flow<List<Product>>

    @Query("SELECT * FROM product WHERE id = :id AND active = 1")
    fun getProduct(id: Int): Flow<Product>

    @Query("SELECT addedThisTime FROM product WHERE id = :id AND active = 1")
    fun getTotalInThisTime(id: Int): Flow<List<Int>>

    @Query("SELECT subtractedThisTime FROM product WHERE id = :id AND active = 1")
    fun getTotalOutThisTime(id: Int): Flow<List<Int>>

    @Query("SELECT subtractedThisWeek FROM product WHERE id = :id AND active = 1")
    fun getTotalOutThisWeek(id: Int): Flow<List<Int>>

    @Query("SELECT subtractedLastTime FROM product WHERE id = :id AND active = 1")
    fun getTotalOutLastTime(id: Int): Flow<List<Int>>

    @Query("SELECT subtractedLastWeek FROM product WHERE id = :id AND active = 1")
    fun getTotalOutLastWeek(id: Int): Flow<List<Int>>

    @Query("SELECT subtractedNextTime FROM product WHERE id = :id AND active = 1")
    fun getTotalOutNextTime(id: Int): Flow<List<Int>>

    @Query("SELECT subtractedNextWeek FROM product WHERE id = :id AND active = 1")
    fun getTotalOutNextWeek(id: Int): Flow<List<Int>>

    @Query("SELECT addedThisTime FROM product WHERE active = 1")
    fun getAllTotalInThisTime(): Flow<List<Int>>

    @Query("SELECT subtractedThisTime FROM product WHERE active = 1")
    fun getAllTotalOutThisTime(): Flow<List<Int>>

    @Query("SELECT subtractedThisWeek FROM product WHERE active = 1")
    fun getAllTotalOutThisWeek(): Flow<List<Int>>

    @Query("SELECT addedLastTime FROM product WHERE active = 1")
    fun getAllTotalInLastTime(): Flow<List<Int>>

    @Query("SELECT subtractedLastTime FROM product WHERE active = 1")
    fun getAllTotalOutLastTime(): Flow<List<Int>>

    @Query("SELECT subtractedLastWeek FROM product WHERE active = 1")
    fun getAllTotalOutLastWeek(): Flow<List<Int>>

    @Query("UPDATE product SET name = :name WHERE id = :id")
    suspend fun updateName(name: String, id: Int)

    @Query("UPDATE product SET imagePath = :imagePath WHERE id = :id")
    suspend fun updateImage(imagePath: String, id: Int)

    @Query("UPDATE product SET category = :category WHERE id = :id")
    suspend fun updateCategory(category: String, id: Int)

    @Query("UPDATE product SET user = :user WHERE id = :id")
    suspend fun updateUserNote(user: String, id: Int)

    @Query("UPDATE product SET price = :price WHERE id = :id")
    suspend fun updatePrice(price: Double, id: Int)

    @Query("UPDATE product SET addedQuantity = :addedQuantity WHERE id = :id")
    suspend fun updateTotalIn(addedQuantity: Int, id: Int)

    @Query("UPDATE product SET subtractedQuantity = :subtractedQuantity WHERE id = :id")
    suspend fun updateTotalOut(subtractedQuantity: Int, id: Int)

    @Query("UPDATE product SET quantity = :quantity WHERE id = :id")
    suspend fun updateStock(quantity: Int, id: Int)

    @Query("UPDATE product SET subtractedThisWeek = :subtractedThisWeek WHERE id = :id")
    suspend fun updateTotalOutThisWeek(subtractedThisWeek: Int, id: Int)

    @Query("UPDATE product SET subtractedThisTime = :subtractedThisTime WHERE id = :id")
    suspend fun updateTotalOutThisTime(subtractedThisTime: Int, id: Int)

    @Query("UPDATE product SET addedThisTime = :addedThisTime WHERE id = :id")
    suspend fun updateTotalInThisTime(addedThisTime: Int, id: Int)

    @Query("UPDATE product SET subtractedLastWeek = :subtractedLastWeek WHERE id = :id")
    suspend fun updateTotalOutLastWeek(subtractedLastWeek: Int, id: Int)

    @Query("UPDATE product SET subtractedLastTime = :subtractedLastTime WHERE id = :id")
    suspend fun updateTotalOutLastTime(subtractedLastTime: Int, id: Int)

    @Query("UPDATE product SET addedLastTime = :addedLastTime WHERE id = :id")
    suspend fun updateTotalInLastTime(addedLastTime: Int, id: Int)

    @Query("UPDATE product SET subtractedNextWeek = :subtractedNextWeek WHERE id = :id")
    suspend fun updateTotalOutNextWeek(subtractedNextWeek: Int, id: Int)

    @Query("UPDATE product SET subtractedNextTime = :subtractedNextTime WHERE id = :id")
    suspend fun updateTotalOutNextTime(subtractedNextTime: Int, id: Int)

    @Query("UPDATE product SET color = :color WHERE id = :id")
    suspend fun updateColor(color:Int, id:Int)

    @Query("UPDATE product SET usb = :usb WHERE id = :id")
    suspend fun updateState(usb: String, id: Int)

    @Query("UPDATE product SET active = :visibility WHERE id = :id")
    suspend fun updateVisibility(visibility: Boolean, id: Int)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(product: Product)

    @Update
    suspend fun update(product: Product)

    @Delete
    suspend fun delete(product: Product)

}