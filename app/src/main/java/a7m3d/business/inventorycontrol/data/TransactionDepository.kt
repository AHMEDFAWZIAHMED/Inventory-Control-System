package a7m3d.business.inventorycontrol.data

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class TransactionDepository(private val transactionDao: TransactionDao,
                            private val reportDao :ReportDao) {

    val allTransaction: Flow<List<Transaction>> = transactionDao.getAll()
    val getYear: Flow<List<Int>> = transactionDao.getYear()
    val getReportYears: Flow<List<Int>> = reportDao.getReportYears()
    val getSumAdd: Flow<List<Int>> = transactionDao.getSumAdd()
    val getSumSubtract: Flow<List<Int>> = transactionDao.getSumSubtract()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(transaction: Transaction) {
        transactionDao.insert(transaction)
    }

    suspend fun insertReport(report: Report) {
        reportDao.insert(report)
    }

    suspend fun update(transaction: Transaction) {
        transactionDao.update(transaction)
    }

    suspend fun delete(transaction: Transaction) {
        transactionDao.delete(transaction)
    }

    suspend fun deleteTransactionsByProductId(productId: Int) {
        transactionDao.deleteById(productId)
    }

    fun getReport(day: Int, month: Int, year: Int): Flow<List<Report>> =
        reportDao.getReport(day, month, year)

    fun getReportMonths(year: Int): Flow<List<Int>> =
        reportDao.getReportMonths(year)

    fun getReportDays(month: Int, year: Int): Flow<List<Int>>  =
        reportDao.getReportDays(month, year)

    fun getTransactionYear(productID: Int): Flow<List<Int>> {
        return transactionDao.getTransactionYear(productID)
    }

    fun getTransactionMonth(productID: Int, tYear: Int): Flow<List<Int>> {
        return transactionDao.getTransactionMonth(productID, tYear)
    }

    fun getTransaction(productID: Int, month: Int): Flow<List<Transaction>> {
        return transactionDao.getTransaction(productID, month)
    }

    fun getMonth(tYear: Int): Flow<List<Int>> {
        return transactionDao.getMonth(tYear)
    }

    fun getTransactions(month: Int): Flow<List<Transaction>> {
        return transactionDao.getTransactions(month)
    }

    fun getProductDayAdd(productID: Int, day: Int, month: Int, tYear: Int): Flow<List<Int>> {
        return transactionDao.getProductDayAdd(productID, day, month, tYear)
    }

    fun getProductDaySubtract(productID: Int, day: Int, month: Int, tYear: Int): Flow<List<Int>> {
        return transactionDao.getProductDaySubtract(productID, day, month, tYear)
    }

    fun searchTransactions(text: String): Flow<List<Transaction>> =
        transactionDao.searchTransactions(text)
}