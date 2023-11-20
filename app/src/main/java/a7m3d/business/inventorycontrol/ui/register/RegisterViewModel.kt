package a7m3d.business.inventorycontrol.ui.register

import a7m3d.business.inventorycontrol.Pro
import a7m3d.business.inventorycontrol.data.Product
import a7m3d.business.inventorycontrol.data.ProductRepository
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import java.util.*

class RegisterViewModel(private val productRepository: ProductRepository) : ViewModel() {

    private val date = Pro.getTodayDate()

    fun insert(name: String, stock: Int, ctgry: String, imgP: String, details: String,
               price: Double, usbNote: String) = viewModelScope.launch {
            productRepository.insert(Product(name, ctgry, imgP, details, usbNote,
               price, stock, stock, 0, stock, 0, 0,
                0, 0, 0, 0, 0,
                Pro.green, date[0], date[1], date[2], true))
        }

}

class RegisterViewModelFactory(
    private val productRepository: ProductRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RegisterViewModel(productRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
