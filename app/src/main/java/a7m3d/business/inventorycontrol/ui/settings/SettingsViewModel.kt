package a7m3d.business.inventorycontrol.ui.settings

import a7m3d.business.inventorycontrol.Pro
import a7m3d.business.inventorycontrol.data.Product
import a7m3d.business.inventorycontrol.data.ProductRepository
import a7m3d.business.inventorycontrol.data.Transaction
import a7m3d.business.inventorycontrol.data.TransactionDepository
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SettingsViewModel(private val timer: DataStore<Preferences>,
                        private val productRepository: ProductRepository,
                        private val transactionDepository: TransactionDepository
) : ViewModel() {

    val getDeletedProducts: LiveData<List<Product>> =
        productRepository.getDeletedProducts.asLiveData()

    fun deleteProduct(product: Product) = viewModelScope.launch {
        val productId = product.id
        productRepository.delete(product)
        transactionDepository.deleteTransactionsByProductId(productId)
    }

    fun updateVisibility(visibility: Boolean, product: Product) = viewModelScope.launch {
        val date = Pro.getTodayDate()
        val newTransaction = Transaction(
            "Restore", product.id, product.productName, product.productImgPath,
            0, 0, date[0], date[1], date[2]
        )
        transactionDepository.insert(newTransaction)
        productRepository.updateVisibility(visibility, product.id)
    }

    fun updatePeriodChoice(period: Int) = viewModelScope.launch {
        timer.edit { it[Pro.PERIOD_CHOICE] = period }
        Pro.periodChoice = period
    }

    fun checkPeriodChoice() = viewModelScope.launch {
        Pro.periodChoice = timer.data.map {
            it[Pro.PERIOD_CHOICE]?: 30
        }.first()
    }

    fun updateNotificationChoice(notification: Int) = viewModelScope.launch {
        timer.edit { it[Pro.NOTIFICATION_CHOICE] = notification }
        Pro.notificationChoice = notification
    }

    fun checkNotificationChoice() = viewModelScope.launch {
        Pro.notificationChoice = timer.data.map {
            it[Pro.NOTIFICATION_CHOICE]?: 1
        }.first()
    }

    fun updateLanguageChoice(lang: Int) = viewModelScope.launch {
        timer.edit { it[Pro.LANGUAGE_CHOICE] = lang }
    }

    fun searchDeletedProducts(text: String): LiveData<List<Product>> =
        productRepository.searchDeletedProducts(text).asLiveData()
}

class SettingsViewModelFactory(private val timer: DataStore<Preferences>,
                               private val productRepository: ProductRepository,
                               private val transactionDepository: TransactionDepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SettingsViewModel(timer, productRepository, transactionDepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}