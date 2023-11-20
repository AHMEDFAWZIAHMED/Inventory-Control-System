package a7m3d.business.inventorycontrol.ui.inventory

import a7m3d.business.inventorycontrol.Pro
import a7m3d.business.inventorycontrol.data.Product
import a7m3d.business.inventorycontrol.data.ProductRepository
import a7m3d.business.inventorycontrol.data.Transaction
import a7m3d.business.inventorycontrol.data.TransactionDepository
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import java.util.*

class InventoryViewModel(
    private val productRepository: ProductRepository,
    private val transactionDepository: TransactionDepository
) : ViewModel() {

    private val date = Pro.getTodayDate()
    val allCategory: LiveData<List<String>> = productRepository.allCategory.asLiveData()
    val allQuantity: LiveData<List<Int>> = productRepository.getAllQuantity.asLiveData()
    val getAllId: LiveData<List<Int>> = productRepository.getAllId.asLiveData()

    fun getQuantity(category: String): LiveData<List<Int>> =
        productRepository.getQuantity(category).asLiveData()

    fun getProducts(cat: String): LiveData<List<Product>> =
        productRepository.getProducts(cat).asLiveData()

    fun getProduct(id: Int): LiveData<Product> =
        productRepository.getProduct(id).asLiveData()

    fun getTransactionYear(productID: Int): LiveData<List<Int>> =
        transactionDepository.getTransactionYear(productID).asLiveData()

    fun getTransactionMonth(productID: Int, tYear: Int): LiveData<List<Int>> =
        transactionDepository.getTransactionMonth(productID, tYear).asLiveData()

    fun getTransaction(productID: Int, tMonth: Int): LiveData<List<Transaction>> =
        transactionDepository.getTransaction(productID, tMonth).asLiveData()

    fun searchProducts(text: String): LiveData<List<Product>> =
        productRepository.searchProducts(text).asLiveData()

    fun delete(product: Product) = viewModelScope.launch {
        val newTransaction = Transaction(
            "Delete", product.id, product.productName, product.productImgPath,
            0, 0, date[0], date[1], date[2]
        )
        transactionDepository.insert(newTransaction)
        productRepository.updateVisibility(false, product.id)
    }

    fun updateName(name: String, product: Product) = viewModelScope.launch {
        productRepository.updateName(name, product.id)
        val newTransaction = Transaction(
            "Name", product.id, name, product.productImgPath,
            0, 0, date[0], date[1], date[2]
        )
        transactionDepository.insert(newTransaction)
    }

    fun updateImage(imagePath: String, product: Product) = viewModelScope.launch {
        productRepository.updateImage(imagePath, product.id)
        val newTransaction = Transaction(
            "Image", product.id, product.productName, imagePath,
            0, 0, date[0], date[1], date[2]
        )
        transactionDepository.insert(newTransaction)
    }

    fun updateCategory(category: String, product: Product) = viewModelScope.launch {
        productRepository.updateCategory(category, product.id)
        val newTransaction = Transaction(
            "Category", product.id, product.productName, product.productImgPath,
            0, 0, date[0], date[1], date[2]
        )
        transactionDepository.insert(newTransaction)
    }

    fun updateUserNote(user: String, product: Product) = viewModelScope.launch {
        productRepository.updateUserNote(user, product.id)
        val newTransaction = Transaction(
            "Note", product.id, product.productName, product.productImgPath,
            0, 0, date[0], date[1], date[2]
        )
        transactionDepository.insert(newTransaction)
    }

    fun updatePrice(price: Double, product: Product) = viewModelScope.launch {
        productRepository.updatePrice(price, product.id)
        val newTransaction = Transaction(
            "Price", product.id, product.productName, product.productImgPath,
            0, 0, date[0], date[1], date[2]
        )
        transactionDepository.insert(newTransaction)
    }

    fun updateTotalIn(addedQuantity: Int, product: Product) = viewModelScope.launch {
        val totalIn = addedQuantity + product.totalQuantityIn
        productRepository.updateTotalIn(totalIn, product.id)
        val current = addedQuantity + product.quantityInStock
        productRepository.updateStock(current, product.id)
        val newTransaction = Transaction(
            "In", product.id, product.productName, product.productImgPath,
            addedQuantity, 0, date[0], date[1], date[2]
        )
        transactionDepository.insert(newTransaction)
    }

    fun updateTotalOut(subtractedQuantity: Int, product: Product) = viewModelScope.launch {
        val totalOut = subtractedQuantity + product.totalQuantityOut
        productRepository.updateTotalOut(totalOut, product.id)
        val current = product.quantityInStock - subtractedQuantity
        productRepository.updateStock(current, product.id)
        val newTransaction = Transaction(
            "Out", product.id, product.productName, product.productImgPath,
            0, subtractedQuantity, date[0], date[1], date[2]
        )
        transactionDepository.insert(newTransaction)
    }
}

class InventoryViewModelFactory(
    private val productRepository: ProductRepository,
    private val transactionDepository: TransactionDepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InventoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InventoryViewModel(productRepository, transactionDepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}