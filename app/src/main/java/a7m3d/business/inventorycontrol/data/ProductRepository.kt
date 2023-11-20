package a7m3d.business.inventorycontrol.data

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class ProductRepository(private val productDao: ProductDao) {

    val allProduct: Flow<List<Product>> = productDao.getAll()
    val getAllQuantity: Flow<List<Int>> = productDao.getAllQuantity()
    val allCategory: Flow<List<String>> = productDao.getCategory()
    val getAllId: Flow<List<Int>> = productDao.getAllId()
    val getDeletedProducts: Flow<List<Product>> = productDao.getDeletedProducts()
    val getAllTotalInThisTime: Flow<List<Int>> = productDao.getAllTotalInThisTime()
    val getAllTotalOutThisTime: Flow<List<Int>> = productDao.getAllTotalOutThisTime()
    val getAllTotalOutThisWeek: Flow<List<Int>> = productDao.getAllTotalOutThisWeek()
    val getAllTotalInLastTime: Flow<List<Int>> = productDao.getAllTotalInLastTime()
    val getAllTotalOutLastTime: Flow<List<Int>> = productDao.getAllTotalOutLastTime()
    val getAllTotalOutLastWeek: Flow<List<Int>> = productDao.getAllTotalOutLastWeek()

    //val allReport: Flow<List<Report>> = reportDao.getReports()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(product: Product) {
        productDao.insert(product)
    }

    suspend fun update(product: Product) {
        productDao.update(product)
    }

    suspend fun delete(product: Product) {
        productDao.delete(product)
    }

    suspend fun updateName(name: String, id: Int) {
        productDao.updateName(name, id)
    }

    suspend fun updateImage(imagePath: String, id: Int) {
        productDao.updateImage(imagePath, id)
    }

    suspend fun updateCategory(category: String, id: Int) {
        productDao.updateCategory(category, id)
    }

    suspend fun updateUserNote(user: String, id: Int) {
        productDao.updateUserNote(user, id)
    }

    suspend fun updateTotalIn(addedQuantity: Int, id: Int) {
        productDao.updateTotalIn(addedQuantity, id)
    }

    suspend fun updateTotalOut(subtractedQuantity: Int, id: Int) {
        productDao.updateTotalOut(subtractedQuantity, id)
    }

    suspend fun updateStock(quantity: Int, id: Int) {
        productDao.updateStock(quantity, id)
    }

    suspend fun updatePrice(price: Double, id: Int) {
        productDao.updatePrice(price, id)
    }

    suspend fun updateTotalOutThisWeek(subtractedThisWeek: Int, id: Int) {
        productDao.updateTotalOutThisWeek(subtractedThisWeek, id)
    }

    suspend fun updateTotalOutThisTime(subtractedThisTime: Int, id: Int) {
        productDao.updateTotalOutThisTime(subtractedThisTime, id)
    }

    suspend fun updateTotalInThisTime(addedThisTime: Int, id: Int) {
        productDao.updateTotalInThisTime(addedThisTime, id)
    }

    suspend fun updateTotalOutLastWeek(subtractedLastWeek: Int, id: Int) {
        productDao.updateTotalOutLastWeek(subtractedLastWeek, id)
    }

    suspend fun updateTotalOutLastTime(subtractedLastTime: Int, id: Int) {
        productDao.updateTotalOutLastTime(subtractedLastTime, id)
    }

    suspend fun updateTotalInLastTime(addedLastTime: Int, id: Int) {
        productDao.updateTotalInLastTime(addedLastTime, id)
    }

    suspend fun updateTotalOutNextWeek(subtractedNextWeek: Int, id: Int) {
        productDao.updateTotalOutNextWeek(subtractedNextWeek, id)
    }

    suspend fun updateTotalOutNextTime(subtractedNextTime: Int, id: Int) {
        productDao.updateTotalOutNextTime(subtractedNextTime, id)
    }

    suspend fun updateColor(color:Int, id:Int) {
        productDao.updateColor(color, id)
    }

    suspend fun updateState(usb: String, id: Int) {
        productDao.updateState(usb, id)
    }

    suspend fun updateVisibility(visibility: Boolean, id: Int) {
        productDao.updateVisibility(visibility, id)
    }

    fun getProducts(category: String): Flow<List<Product>> {
        return productDao.getProducts(category)
    }

    fun getProduct(id: Int): Flow<Product> {
        return productDao.getProduct(id)
    }

    fun getTotalQuantityOut(id: Int): Flow<List<Int>> = productDao.getTotalQuantityOut(id)

    fun getQuantityInStock(id: Int): Flow<List<Int>> = productDao.getQuantityInStock(id)

    fun getTotalInThisTime(id: Int): Flow<List<Int>> = productDao.getTotalInThisTime(id)

    fun getTotalOutThisTime(id: Int): Flow<List<Int>> = productDao.getTotalOutThisTime(id)

    fun getTotalOutThisWeek(id: Int): Flow<List<Int>> = productDao.getTotalOutThisWeek(id)

    fun getTotalOutLastTime(id: Int): Flow<List<Int>> = productDao.getTotalOutLastTime(id)

    fun getTotalOutLastWeek(id: Int): Flow<List<Int>> = productDao.getTotalOutLastWeek(id)

    fun getTotalOutNextTime(id: Int): Flow<List<Int>> = productDao.getTotalOutNextTime(id)

    fun getTotalOutNextWeek(id: Int): Flow<List<Int>> = productDao.getTotalOutNextWeek(id)

    fun getProductNames(color: Int): Flow<List<String>> = productDao.getProductNames(color)

    fun getName(id: Int): Flow<List<String>> = productDao.getName(id)

    fun searchProducts(text: String): Flow<List<Product>> = productDao.searchProducts(text)

    fun searchDeletedProducts(text: String): Flow<List<Product>> =
        productDao.searchDeletedProducts(text)

    fun getQuantity(category: String): Flow<List<Int>> = productDao.getQuantity(category)

}