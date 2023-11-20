
package a7m3d.business.inventorycontrol

import a7m3d.business.inventorycontrol.data.*
import android.app.Service
import android.content.Intent
import android.graphics.*
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.io.File
import java.io.FileOutputStream
import java.util.*

class StockService : Service() {

    private val date = Pro.getTodayDate()
    private val thisWeek = Pro.getLastDate(5, date)
    private val thisPeriod = Pro.getLastDate(0, date)
    private lateinit var productRepository: ProductRepository
    private lateinit var transactionDepository: TransactionDepository
    private lateinit var timer: DataStore<Preferences>
    private lateinit var allIdObserver: Observer<List<Int>>
    private lateinit var products: LiveData<List<Product>>
    private lateinit var allProductsId: LiveData<List<Int>>

    override fun onBind(intent: Intent): IBinder {
        throw UnsupportedOperationException("Not yet implemented")
    }

    @OptIn(DelicateCoroutinesApi::class)
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        /*
        In Pro.valuesList:
        periodCount = index 0
        periodAverageOut = index 1
        periodTotalAverageOut = index 2
        periodAverageIn = index 3
        periodTotalAverageIn = index 4
        weekCount = index 5
        weekAverageOut = index 6
        weekTotalAverageOut = index 7
        notificationCount = index 8
        */

        productRepository = (application as ProductApplication).repository
        timer = (application as ProductApplication).dataStore
        products = productRepository.allProduct.asLiveData()
        transactionDepository = (application as ProductApplication).depository
        allProductsId = productRepository.getAllId.asLiveData()


        GlobalScope.launch {
            checkKeysExisting()
            updateValues()
            if (isTransactionsNotEmpty()) {
                updatePeriods()
                updateValues()
            }
            this.coroutineContext.job.cancel()
        }

        allIdObserver = Observer<List<Int>> {
            GlobalScope.launch {
                if (Pro.valuesList[0] == Pro.periodChoice) calculateTotalPeriod(it)// Deactivate notification
                else if (Pro.valuesList[5] == 7) calculateTotalWeek(it)// Deactivate notification

                else {
                    updateColorsAndStates(it, 0)
                }

                this.coroutineContext.job.cancel()
            }
        }
        allProductsId.observeForever(allIdObserver)

        return START_STICKY
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private suspend fun calculateTotalWeek(idList: List<Int>) {
        for (id in idList) {
            val totalSubtract = mutableListOf<Int>()
            val daysSubtract = mutableListOf<Int>()
            val current = productRepository.getProduct(id).first().quantityInStock
            val name = productRepository.getName(id).first()[0]

            for (weekDate in thisWeek) {
                val daySingleSubtract = transactionDepository.getProductDaySubtract(
                    id, weekDate[0], weekDate[1], weekDate[2]
                ).first()
                for (subtract in daySingleSubtract) totalSubtract += subtract
                daysSubtract += daySingleSubtract.sum()
            }
            updateAverageAndTotal(
                totalSubtract, "week", "product", id)

            saveReportToDatabase(name, daysSubtract, current)
        }

        val allSubtract = productRepository.getAllTotalOutThisWeek.first().toMutableList()
        updateAverageAndTotal(allSubtract, "week", "timer")
        weekReport()
        updateColorsAndStates(idList, 6)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private suspend fun calculateTotalPeriod(idList: List<Int>) {
        for (id in idList) {
            val totalSubtract = mutableListOf<Int>()
            val totalAdd = mutableListOf<Int>()
            for (period in thisPeriod) {
                val daySingleSubtract = transactionDepository.getProductDaySubtract(
                    id, period[0], period[1], period[2]
                ).first()
                for (subtract in daySingleSubtract) totalSubtract += subtract

                val daySingleAdd = transactionDepository.getProductDayAdd(
                    id, period[0], period[1], period[2]
                ).first()
                for (add in daySingleAdd) totalAdd += add
            }
            updateAverageAndTotal(
                totalSubtract, "periodOut", "product", id)
            updateAverageAndTotal(
                totalAdd, "periodIn", "product", id)
        }
        val allSubtract = productRepository.getAllTotalOutThisTime.first().toMutableList()
        updateAverageAndTotal(allSubtract, "periodIn", "timer")
        val allAdd = productRepository.getAllTotalInThisTime.first().toMutableList()
        updateAverageAndTotal(allAdd, "periodOut", "timer")
        savePeriodReportToFile(idList)
        updateColorsAndStates(idList, 1)
    }

    private suspend fun updateAverageAndTotal(transactionList: MutableList<Int>,
                                      countName: String, updateType: String, id: Int = 0)
    {
        when (updateType) {
            "product" -> {
                when (countName) {
                    "week" -> {
                        val outThisWeek = productRepository.getTotalOutThisWeek(id).first()[0]
                        val outLastWeek = productRepository.getTotalOutLastWeek(id).first()[0]
                        val average = listOf(outThisWeek, outLastWeek).average().toInt()
                        productRepository.updateTotalOutLastWeek(outThisWeek, id)
                        productRepository.updateTotalOutThisWeek(transactionList.sum(), id)
                        productRepository.updateTotalOutNextWeek(average,id)
                    }
                    "periodIn" -> {
                        productRepository.updateTotalInLastTime(
                            productRepository.getTotalInThisTime(id).first()[0], id)
                        productRepository.updateTotalInThisTime(transactionList.sum(), id)
                    }
                    "periodOut" -> {
                        val outThisTime = productRepository.getTotalOutThisTime(id).first()[0]
                        val outLastTime = productRepository.getTotalOutLastTime(id).first()[0]
                        val average = listOf(outThisTime, outLastTime).average().toInt()
                        productRepository.updateTotalOutLastTime(outThisTime, id)
                        productRepository.updateTotalOutThisTime(transactionList.sum(), id)
                        productRepository.updateTotalOutNextTime(average, id)
                    }
                }
            }
            "timer" -> {
                timer.edit {
                    when (countName) {
                        "week" -> {
                            Pro.valuesList[7] = it[Pro.WEEK_TOTAL_AVERAGE_OUT]?: 0
                            Pro.valuesList[6] = transactionList.average().toInt()
                            it[Pro.WEEK_AVERAGE_OUT] = Pro.valuesList[6]
                            it[Pro.WEEK_TOTAL_AVERAGE_OUT] =
                                listOf(Pro.valuesList[6], Pro.valuesList[7]).average().toInt()
                        }
                        "periodIn" -> {
                            Pro.valuesList[4] = it[Pro.PERIOD_TOTAL_AVERAGE_IN]?: 0
                            Pro.valuesList[3] = transactionList.average().toInt()
                            it[Pro.PERIOD_AVERAGE_IN] = Pro.valuesList[3]
                            it[Pro.PERIOD_TOTAL_AVERAGE_IN] =
                                listOf(Pro.valuesList[3], Pro.valuesList[4]).average().toInt()
                        }
                        "periodOut" -> {
                            Pro.valuesList[2] = it[Pro.PERIOD_TOTAL_AVERAGE_OUT]?: 0
                            Pro.valuesList[1] = transactionList.average().toInt()
                            it[Pro.PERIOD_AVERAGE_OUT] = Pro.valuesList[1]
                            it[Pro.PERIOD_TOTAL_AVERAGE_OUT] =
                                listOf(Pro.valuesList[1], Pro.valuesList[2]).average().toInt()
                        }
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private suspend fun updateColorsAndStates(idList: List<Int>, vIndx: Int) {
        if (idList.isEmpty()) return
        val totalAverage = Pro.valuesList[vIndx]// periodAverageOut = [1] or weekAverageOut = [6]
        val emptyStock = getString(R.string.empty_stock)
        val deadStock = getString(R.string.dead_stock)
        val coldStock = getString(R.string.cold_stock)
        val veryColdStock = getString(R.string.very_cold_stock)
        val normal = getString(R.string.normal)
        val oneWeek = getText(R.string.one_week)
        val emptyIn = getText(R.string.empty_in)
        for (id in idList) {
            val totalQuantityOut = productRepository.getTotalQuantityOut(id).first()[0]
            val emptyDays = emptyNextDays(id, vIndx)
            val product = productRepository.getProduct(id).first()
            if (productRepository.getQuantityInStock(id).first()[0] == 0) {
                if (product.statusColor != Pro.red) {
                    productRepository.updateColor(Pro.red, id)
                    productRepository.updateState(emptyStock, id)
                    // History here
                    val transaction = Transaction("red",id,
                        product.productName, product.productImgPath, 0,
                        0, date[0], date[1], date[2])
                    transactionDepository.insert(transaction)
                }
            }
            else if (totalQuantityOut == 0) {
                if (product.statusColor != Pro.purple) {
                    productRepository.updateColor(Pro.purple, id)
                    productRepository.updateState(deadStock, id)
                    // History here
                    val transaction = Transaction("purple",id,
                        product.productName, product.productImgPath, 0,
                        0, date[0], date[1], date[2])
                    transactionDepository.insert(transaction)
                }
            }
            else if (emptyDays != 0) {
                val numberOfDay = when (emptyDays) {
                    1 -> getText(R.string.one_day)
                    2 -> getText(R.string.two_days)
                    else -> getText(R.string.three_days)
                }
                when (emptyDays) {
                    7 -> {
                        if (product.statusColor != Pro.yellow) {
                            productRepository.updateColor(Pro.yellow, id)
                            productRepository.updateState("$emptyIn $oneWeek", id)
                            // History here
                            val transaction = Transaction("yellow",id,
                                product.productName, product.productImgPath, 0,
                                0, date[0], date[1], date[2])
                            transactionDepository.insert(transaction)
                        }
                    }
                    else -> {
                        val usbNote = "$emptyIn $emptyDays $numberOfDay"
                        if (product.usbNote != usbNote) {
                            productRepository.updateState("$emptyIn $emptyDays $numberOfDay", id)
                            if (product.statusColor != Pro.orange) {
                                productRepository.updateColor(Pro.orange, id)
                            }
                            // History here
                            val transaction = Transaction("orange $emptyDays",id,
                                product.productName, product.productImgPath, 0,
                                0, date[0], date[1], date[2])
                            transactionDepository.insert(transaction)
                        }
                    }
                }
            }
            else if (totalQuantityOut > (totalAverage/2) && totalQuantityOut < totalAverage) {
                if (product.statusColor != Pro.blue) {
                    productRepository.updateColor(Pro.blue, id)
                    productRepository.updateState(coldStock, id)
                    // History here
                    val transaction = Transaction("blue",id,
                        product.productName, product.productImgPath, 0,
                        0, date[0], date[1], date[2])
                    transactionDepository.insert(transaction)
                }
            }
            else if (totalQuantityOut < (totalAverage/2)) {
                if (product.statusColor != Pro.lBlue) {
                    productRepository.updateColor(Pro.lBlue, id)
                    productRepository.updateState(veryColdStock, id)
                    // History here
                    val transaction = Transaction("light_blue",id,
                        product.productName, product.productImgPath, 0,
                        0, date[0], date[1], date[2])
                    transactionDepository.insert(transaction)
                }
            }
            else {
                if (product.statusColor != Pro.green) {
                    productRepository.updateColor(Pro.green, id)
                    productRepository.updateState(normal, id)
                    // History here
                    val transaction = Transaction("green",id,
                        product.productName, product.productImgPath, 0,
                        0, date[0], date[1], date[2])
                    transactionDepository.insert(transaction)
                }
            }
            if (Pro.valuesList[8] == Pro.notificationChoice) {
                findWarning()
            }
            else {
                Handler(Looper.getMainLooper()).postDelayed({
                    allProductsId.removeObserver(allIdObserver)
                    stopSelf()
                }, 1000)
            }
        }
    } // vIndx = total average week or period

    private suspend fun emptyNextDays(id: Int, vIndx: Int): Int {
        val quantity = productRepository.getQuantityInStock(id).first()[0].toDouble()
        when (vIndx) {
            1 -> {
                val outPeriod = productRepository.getTotalOutNextTime(id).first()[0].toDouble()
                if (outPeriod > 0) {
                    val outVelocity: Double = outPeriod/quantity
                    if (outVelocity >= 4) return 7//Will be empty in one week or less
                }
            }
            else -> {
                val outWeek = productRepository.getTotalOutNextWeek(id).first()[0].toDouble()
                if (outWeek > 0) {
                    val outVelocity: Double = outWeek/quantity
                    if (outVelocity <= 1) return 7
                    if (outVelocity > 1 && outVelocity < 3) return 3
                    if (outVelocity > 3 && outVelocity < 4) return 2
                    if (outVelocity >= 4) return 1
                }
            }
        }
        return 0
    }


    @RequiresApi(Build.VERSION_CODES.M)
    private suspend fun findWarning() {
        val redProduct = productRepository.getProductNames(Pro.red).first()
        val orangeProduct = productRepository.getProductNames(Pro.orange).first()
        val lBlueProduct = productRepository.getProductNames(Pro.lBlue).first()
        val purpleProduct = productRepository.getProductNames(Pro.purple).first()
        if (redProduct.isEmpty() && orangeProduct.isEmpty() &&
            lBlueProduct.isEmpty() && purpleProduct.isEmpty()) {
            return
        }
        val warning = getText(R.string.warning)
        val red = getText(R.string.red)
        val orange = getText(R.string.orange)
        val lightBlue = getText(R.string.light_blue)
        val purple = getText(R.string.purple)
        var colorsCount = 0
        val countList = mutableListOf(0, 0, 0, 0)
        val colorsList = listOf(redProduct, orangeProduct, lBlueProduct, purpleProduct)
        for (c in colorsList.indices) {
            if (colorsList[c].isNotEmpty()) {
                colorsCount += colorsList[c].size
                countList[c] = colorsList[c].size
            }
        }
        if (colorsCount == 1) {
            for (count in countList.indices) {
                if (countList[count] > 0) {
                    showNotification("1 $warning", colorsList[count][0])
                    break
                }
            }
        }
        else if (colorsCount > 1) {
            val details = "$red:${countList[0]} $orange:${countList[1]}" +
                    " $lightBlue:${countList[2]} $purple:${countList[3]}"
            showNotification("$colorsCount $warning", details)
        }
    }

    private suspend fun updatePeriods() {
        timer.edit { t ->
            if (Pro.valuesList[8] < Pro.notificationChoice) {
                Pro.valuesList[8]++
                t[Pro.NOTIFY_COUNT] = Pro.valuesList[8]
            }
            else {
                t[Pro.NOTIFY_COUNT] = 0
                Pro.valuesList[8] = 0
            }
            if (Pro.valuesList[5] < 7) {
                Pro.valuesList[5]++
                t[Pro.WEEK_COUNT] = Pro.valuesList[5]
            }
            else {
                t[Pro.WEEK_COUNT] = 0
                Pro.valuesList[5] = 0
            }
            if (Pro.valuesList[0] < Pro.periodChoice) {
                Pro.valuesList[0]++
                t[Pro.PERIOD_COUNT] = Pro.valuesList[0]
            }
            else {
                t[Pro.PERIOD_COUNT] = 0
                Pro.valuesList[0] = 0
            }
        }
    }

    private suspend fun checkKeysExisting() {
        for (k in Pro.keysList()) {
            if (isKeyNotStored(k)) {
                timer.edit {
                    it[k] = 0
                }
            }
        }
        if (isKeyNotStored(Pro.PERIOD_CHOICE)) {
            timer.edit { it[Pro.PERIOD_CHOICE] = 30 }
        }
    }

    private suspend fun updateValues() {
        for (keyIndx in Pro.keysList().indices) {
            Pro.valuesList[keyIndx] = timer.data.map {
                it[Pro.keysList()[keyIndx]]?: 0
            }.first()
        }
        Pro.periodChoice = timer.data.map {
            it[Pro.PERIOD_CHOICE]?: 30
        }.first()
        Pro.notificationChoice =timer.data.map {
            it[Pro.NOTIFICATION_CHOICE]?: 1
        }.first()
    }

    private suspend fun isKeyNotStored(key: Preferences.Key<Int>): Boolean {
        return timer.data.map {
            it.contains(key)
        }.firstOrNull()?.not()?: true
    }

    private suspend fun isTransactionsNotEmpty(): Boolean {
        return transactionDepository.allTransaction.firstOrNull()?.isNotEmpty()?: false
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun showNotification(title: String, txt: String) {
        allProductsId.removeObserver(allIdObserver)
        Pro.notifyMe(applicationContext, title, txt)
        //Pro.valuesList.. week out[7] period in[4] period out[2] in notifyMe txt
        stopSelf()// End of all processes in this service
    }

    private suspend fun saveReportToDatabase(name: String, daysSubtract: MutableList<Int>, current: Int) {
        if (daysSubtract.size > 7) return
        val stockLastWeek = current + daysSubtract.sum()
        val report = Report(name, current, stockLastWeek, "week", daysSubtract[0],
            daysSubtract[1], daysSubtract[2], daysSubtract[3], daysSubtract[4], daysSubtract[5],
            daysSubtract[6], Pro.dayOfWeek, date[0], date[1], date[2])
        transactionDepository.insertReport(report)
    }

    private suspend fun weekReport() {
        val reports = transactionDepository.getReport(date[0], date[1], date[2]).first()
        Pro.saveWeekReportToFile(reports, applicationContext)
    }

    private suspend fun savePeriodReportToFile(idList: List<Int>) {
        if (idList.isEmpty()) return
        Pro.paint = Paint()
        try {
            Pro.newDirectory = File("/storage/emulated/0/Inventory reports")
            if (!Pro.newDirectory!!.exists()) Pro.newDirectory!!.mkdir()
        }
        catch (_: Exception) {
            Log.d("StockService", "Exception in saveReport")
        }
        val pages = Pro.numberOfPages(idList.size, 8)
        fun lastPage(page: Int): Boolean = page == pages
        val allQuantity = productRepository.getAllQuantity.first()[0]
        val allSubtract = productRepository.getAllTotalOutThisTime.first().sum()
        val allAdd = productRepository.getAllTotalInThisTime.first().sum()

        val allNames = arrayListOf<String>()
        val allCurrent = arrayListOf<Int>()
        val allAdded = arrayListOf<Int>()
        val allSubtracted = arrayListOf<Int>()
        val product = productRepository.getProduct(idList[0]).first()
        val reportDate = "${product.dateDay}/${product.dateMonth}" +
                "/${product.dateYear}"

        for (page in 1..pages) {
            for (indx in ((page-1)*8) until Pro.lastIndex(page, pages, idList.size, 8)) {
                allNames += productRepository.getName(idList[indx]).first()
                allCurrent += productRepository.getQuantityInStock(idList[indx]).first()
                allAdded += productRepository.getTotalInThisTime(idList[indx]).first()[0]
                allSubtracted += productRepository.getTotalOutThisTime(idList[indx]).first()[0]
            }
            try {
                val file = File(Pro.newDirectory, "Total-report-${Pro.date[0]}-${Pro.date[1]}-${Pro.date[2]}-$page.png")
                file.setReadable(true)
                val fileOutputStream =
                    withContext(Dispatchers.IO) {
                        FileOutputStream(file.absolutePath)
                    }
                val bitmap = Bitmap.createBitmap(598, 843, Bitmap.Config.ARGB_8888)
                Pro.canvas = Canvas(bitmap)

                Pro.drawPeriodReport(
                    allNames, allCurrent, allAdded, allSubtracted, allQuantity,
                    allSubtract, allAdd, page, reportDate, lastPage(page)
                )

                bitmap.compress(Bitmap.CompressFormat.PNG, 80, fileOutputStream)
                bitmap.recycle()
                withContext(Dispatchers.IO) {
                    fileOutputStream.flush()
                }
                withContext(Dispatchers.IO) {
                    fileOutputStream.close()
                }
                MediaScannerConnection.scanFile(applicationContext, arrayOf(file.absolutePath),
                    null, null)
            }
            catch(e: Exception) {
                Log.d("StockService", "$e ********** Exception in savePeriodReportToFile **********")
            }

            allNames.clear()
            allCurrent.clear()
            allAdded.clear()
            allSubtracted.clear()
        }
    }
}