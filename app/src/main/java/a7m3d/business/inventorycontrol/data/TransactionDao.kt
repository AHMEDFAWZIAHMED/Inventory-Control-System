package a7m3d.business.inventorycontrol.data
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import androidx.room.Delete
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Query("SELECT * FROM `transaction`")
    fun getAll(): Flow<List<Transaction>>

    @Query("SELECT * FROM `transaction` WHERE name LIKE :text")
    fun searchTransactions(text: String): Flow<List<Transaction>>

    @Query("SELECT SUM(addProduct) FROM `transaction`")
    fun getSumAdd(): Flow<List<Int>>

    @Query("SELECT SUM(subtractProduct) FROM `transaction`")
    fun getSumSubtract(): Flow<List<Int>>

    @Query("SELECT addProduct FROM `transaction` WHERE tTimeDay = :day" +
            " AND tTimeMonth = :month AND tTimeYear = :tYear")
    fun getAllDayAdd(day: Int, month: Int, tYear: Int): Flow<List<Int>>

    @Query("SELECT subtractProduct FROM `transaction` WHERE tTimeDay = :day" +
            " AND tTimeMonth = :month AND tTimeYear = :tYear")
    fun getAllDaySubtract(day: Int, month: Int, tYear: Int): Flow<List<Int>>

    @Query("SELECT subtractProduct FROM `transaction` WHERE" +
            " tTimeMonth = :month AND tTimeYear = :tYear")
    fun getAllMonthSubtract(month: Int, tYear: Int): Flow<List<Int>>

    @Query("SELECT addProduct FROM `transaction` WHERE productNo = :productID " +
            "AND tTimeDay = :day AND tTimeMonth = :month AND tTimeYear = :tYear")
    fun getProductDayAdd(productID: Int, day: Int, month: Int, tYear: Int): Flow<List<Int>>

    @Query("SELECT subtractProduct FROM `transaction` WHERE productNo = :productID " +
            "AND tTimeDay = :day AND tTimeMonth = :month AND tTimeYear = :tYear")
    fun getProductDaySubtract(productID: Int, day: Int, month: Int, tYear: Int): Flow<List<Int>>

    @Query("SELECT addProduct FROM `transaction` WHERE productNo = :productID" +
            " AND tTimeMonth = :month AND tTimeYear = :tYear")
    fun getProductMonthAdd(productID: Int, month: Int, tYear: Int): Flow<List<Int>>

    @Query("SELECT addProduct FROM `transaction` WHERE productNo = :productID " +
            "AND tTimeMonth = :month AND tTimeYear = :tYear")
    fun getProductMonthSubtract(productID: Int, month: Int, tYear: Int): Flow<List<Int>>

    @Query("SELECT tTimeYear FROM `transaction` WHERE productNo = :productID")
    fun getTransactionYear(productID: Int): Flow<List<Int>>

    @Query("SELECT tTimeMonth FROM `transaction`" +
            " WHERE productNo = :productID AND tTimeYear = :tYear")
    fun getTransactionMonth(productID: Int, tYear: Int): Flow<List<Int>>

    @Query("SELECT * FROM `transaction` WHERE productNo = :productID AND tTimeMonth = :month")
    fun getTransaction(productID: Int, month: Int): Flow<List<Transaction>>

    @Query("SELECT tTimeYear FROM `transaction`")
    fun getYear(): Flow<List<Int>>

    @Query("SELECT tTimeMonth FROM `transaction` WHERE tTimeYear = :tYear")
    fun getMonth(tYear: Int): Flow<List<Int>>

    @Query("SELECT * FROM `transaction` WHERE tTimeMonth = :month")
    fun getTransactions(month: Int): Flow<List<Transaction>>

    @Query("SELECT * FROM `transaction` WHERE productNo = :number")
    fun getProductId(number: Int): Flow<Transaction>

    @Query("DELETE FROM `transaction` WHERE productNo = :productId")
    suspend fun deleteById(productId: Int)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(transaction: Transaction)

    @Update
    suspend fun update(transaction: Transaction)

    @Delete
    suspend fun delete(transaction: Transaction)
}