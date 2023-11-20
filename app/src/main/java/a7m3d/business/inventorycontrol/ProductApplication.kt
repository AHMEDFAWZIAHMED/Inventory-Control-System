package a7m3d.business.inventorycontrol

import a7m3d.business.inventorycontrol.data.ProductRepository
import a7m3d.business.inventorycontrol.data.ProductRoomDatabase
import a7m3d.business.inventorycontrol.data.TransactionDepository
import android.app.Application
import android.content.res.Configuration
import android.os.Build
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.*

class ProductApplication : Application() {

    private val database by lazy { ProductRoomDatabase.getDatabase(this) }
    val dataStore by lazy { createDataStore("Timer") }
    val repository by lazy { ProductRepository(database.productDao()) }
    val depository by lazy { TransactionDepository(database.transactionDao(), database.reportDao()) }

    override fun onCreate() {
        runBlocking {
            val languageChoice = dataStore.data.map {
                it[Pro.LANGUAGE_CHOICE]?: 0
            }.first()
            val choice = Pro.languageList[languageChoice]
            val locale = Locale(choice[0], choice[1])
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
                val config = applicationContext.resources.configuration
                config.setLocale(locale)
                applicationContext.createConfigurationContext(config).resources
            }
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N) {
                val config1 = Configuration()
                config1.locale = locale
                applicationContext.resources.updateConfiguration(config1,
                    applicationContext.resources.displayMetrics)
            }
        }
        super.onCreate()
    }
    
}
