package a7m3d.business.inventorycontrol.ui.history

import a7m3d.business.inventorycontrol.Pro
import a7m3d.business.inventorycontrol.data.ProductRepository
import a7m3d.business.inventorycontrol.data.Report
import a7m3d.business.inventorycontrol.data.Transaction
import a7m3d.business.inventorycontrol.data.TransactionDepository
import android.content.Context
import androidx.lifecycle.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class HistoryViewModel(private val transactionDepository: TransactionDepository,
                       private val productRepository: ProductRepository) : ViewModel() {

    val getYear: LiveData<List<Int>> = transactionDepository.getYear.asLiveData()
    val reportYears: LiveData<List<Int>> = transactionDepository.getReportYears.asLiveData()
    val date = Pro.getTodayDate()
    private val thisWeek = Pro.getLastDate(5, date)
    val sumAdd: LiveData<List<Int>> = transactionDepository.getSumAdd.asLiveData()
    val sumSubtract: LiveData<List<Int>> = transactionDepository.getSumSubtract.asLiveData()

    fun report(day: Int, month: Int, year: Int): LiveData<List<Report>> =
        transactionDepository.getReport(day, month, year).asLiveData()

    fun getMonth(tYear: Int): LiveData<List<Int>> =
        transactionDepository.getMonth(tYear).asLiveData()

    fun getTransactions(tMonth: Int): LiveData<List<Transaction>> =
        transactionDepository.getTransactions(tMonth).asLiveData()

    fun searchTransactions(text: String): LiveData<List<Transaction>> =
        transactionDepository.searchTransactions(text).asLiveData()

    fun getReportMonths(year: Int): LiveData<List<Int>> =
        transactionDepository.getReportMonths(year).asLiveData()

    fun getReportDays(month: Int, year: Int): LiveData<List<Int>> =
        transactionDepository.getReportDays(month, year).asLiveData()

    fun makePdfReport(context: Context) = viewModelScope.launch {
        var reportSequence = sequenceOf<Report>()
        for (id in productRepository.getAllId.first()) {
            val daysSubtract = mutableListOf<Int>()
            val current = productRepository.getProduct(id).first().quantityInStock
            val name = productRepository.getName(id).first()[0]

            for (weekDate in thisWeek) {
                val daySingleSubtract = transactionDepository.getProductDaySubtract(
                    id, weekDate[0], weekDate[1], weekDate[2]).first()
                daysSubtract += daySingleSubtract.sum()
            }
            val stockLastWeek = current + daysSubtract.sum()
            val report = Report(name, current, stockLastWeek, "week", daysSubtract[0],
                daysSubtract[1], daysSubtract[2], daysSubtract[3], daysSubtract[4], daysSubtract[5],
                daysSubtract[6], Pro.dayOfWeek, date[0], date[1], date[2])
            reportSequence += report
        }
        if (reportSequence.toList().isEmpty()) return@launch
        Pro.saveWeekReportToFile(reportSequence.toList(), context)
    }
}

class HistoryViewModelFactory(private val transactionDepository: TransactionDepository,
              private val productRepository: ProductRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HistoryViewModel(transactionDepository, productRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}