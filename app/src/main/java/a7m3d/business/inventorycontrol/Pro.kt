package a7m3d.business.inventorycontrol

import a7m3d.business.inventorycontrol.R.drawable.ic_notifications
import a7m3d.business.inventorycontrol.data.Report
import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.media.MediaScannerConnection
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.datastore.preferences.core.preferencesKey
import java.io.File
import java.io.FileOutputStream
import java.util.*
import kotlin.collections.ArrayList

object Pro {

    val PERIOD_CHOICE = preferencesKey<Int>("period_choice")// In Setting and service
    val PERIOD_COUNT = preferencesKey<Int>("period_count")
    val PERIOD_AVERAGE_OUT = preferencesKey<Int>("period_average_out")
    val PERIOD_TOTAL_AVERAGE_OUT = preferencesKey<Int>("period_total_average_out")
    val PERIOD_AVERAGE_IN = preferencesKey<Int>("period_average_in")
    val PERIOD_TOTAL_AVERAGE_IN = preferencesKey<Int>("period_total_average_in")
    val WEEK_COUNT = preferencesKey<Int>("week_count")
    val WEEK_AVERAGE_OUT = preferencesKey<Int>("week_average_out")
    val WEEK_TOTAL_AVERAGE_OUT = preferencesKey<Int>("week_total_average_out")
    val NOTIFY_COUNT = preferencesKey<Int>("notify_count")// In Setting and service
    fun keysList() = listOf(
        PERIOD_COUNT, PERIOD_AVERAGE_OUT, PERIOD_TOTAL_AVERAGE_OUT, PERIOD_AVERAGE_IN,
        PERIOD_TOTAL_AVERAGE_IN, WEEK_COUNT, WEEK_AVERAGE_OUT, WEEK_TOTAL_AVERAGE_OUT, NOTIFY_COUNT
    )
    var periodChoice = 30

    val LANGUAGE_CHOICE = preferencesKey<Int>("language_choice")
    private val languageChoice0 = listOf("en", "US")
    private val languageChoice1 = listOf("ar", "SD")
    val languageList = listOf(languageChoice0, languageChoice1)

    val NOTIFICATION_CHOICE = preferencesKey<Int>("notification_choice")
    var notificationChoice = 1

    val valuesList = mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0)// 9 values, 8 indexes
    private val xLocationsOfText = listOf(37.5f, 137.68f, 240.2f, 342.72f, 445.24f, 547.76f)
    private val yLocationsOfText = listOf(210.57f, 276f, 341.43f, 406.86f,
                                    472.29f, 537.72f, 603.15f, 668.58f)
    val dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
    fun getDaysNames(lastDay: Int): ArrayList<String> {
        val daysNames = arrayListOf<String>()
        val daysOfWeek = listOf("SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT")
        var day = lastDay-1
        for (i in 0..6) {
            if (day < 0) day = 6
            daysNames += daysOfWeek[day]
            day--
        }
        daysNames.reverse()
        return daysNames
    }
    private fun nameParts(name: String): ArrayList<String> {
        if (name.length > 11) {
            var name1 = name.slice(0..10)
            if (name[10] != ' ' && name[11] != ' ') name1 += '-'
            val nameLength = if (name.length > 22) 22 else name.length
            val name2 = name.slice(11 until nameLength)
            if (name2[0] == ' ') name2.removeRange(0..0)
            if (name2[name2.length-1] == ' ') {
                name2.removeRange(name2.length-1 until name2.length)
            }
            return arrayListOf(name1, name2)
        }
        return  arrayListOf(name, "")
    }

    fun numberOfPages(listSize: Int, namesInOnePage: Int): Int {
        var pages = listSize / namesInOnePage
        if (listSize % namesInOnePage > 0) pages += 1
        return pages
    }

    fun lastIndex(page: Int, pages: Int, size: Int, namesInOnePage: Int): Int {
        return if (page == pages) size else page * namesInOnePage
    }

    private val pdfLines = floatArrayOf(
                                85.42f, 100f, 85.42f, 743f,/*  vertical 1 */
                                187.94f, 100f, 187.94f, 743f,/*vertical 2 */
                                290.44f, 100f, 290.44f, 743f,/*vertical 3 */
                                392.96f, 100f, 392.96f, 743f,/*vertical 4 */
                                495.48f, 100f, 495.48f, 743f,/*vertical 5 */
                                10f, 170.85f, 588f, 170.85f,/*horizontal 1 */
                                10f, 236.28f, 588f, 236.28f,/*horizontal 2 */
                                10f, 301.71f, 588f, 301.71f,/*horizontal 3 */
                                10f, 367.14f, 588f, 367.14f,/*horizontal 4 */
                                10f, 432.57f, 588f, 432.57f,/*horizontal 5 */
                                10f, 498f, 588f, 498f,/*      horizontal 6 */
                                10f, 563.43f, 588f, 563.43f,/*horizontal 7 */
                                10f, 628.86f, 588f, 628.86f,/*horizontal 8 */
                                10f, 694.29f, 588f, 694.29f/* horizontal 9 */)

    var weekTotalOut = 0
    var weekTotalInHand = 0

    var prodId = 1
    var category = ""
    var iAdapterCount = 0
    var iYear = 0
    var iMonth = 0

    var hAdapterCount = 0
    var hYearT = 2023
    var hMonthT = 8
    var hYearR = 2023
    var hMonthR = 8
    var hDayR = 26
    var hMenu = 0
    fun hDate() = listOf(hDayR, hMonthR, hYearR)

    var sAdapterCount = 0

    var heAdapterCount = 0

    var nImgPath = ""
    var iGetPhoto = false
    var rGetPhoto = false
    var pagerCount = 0

    val red = Color.argb(255, 233, 30, 99)// From 0 to 255
    val orange = Color.argb(255, 255, 87, 34)
    val yellow = Color.argb(255, 255, 235, 59)
    val green = Color.argb(255, 76, 175, 80)
    val blue = Color.argb(255, 33, 150, 243)
    val lBlue = Color.argb(255, 128, 216, 255)
    val purple = Color.argb(255, 103, 58, 183)

    val black = Color.argb(255, 0, 0, 0)
    val white = Color.argb(255, 255, 255, 255)
    private val gray = Color.argb(255, 50, 50, 50)

    private val monthsDays = mutableListOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)

    fun getTodayDate(): ArrayList<Int> {
        val dateNow = ArrayList<Int>()
        val calender = Calendar.getInstance()

        val day = calender.get(5)
        dateNow += day
        val month = calender.get(2)+1
        dateNow += month
        val year = calender.get(1)
        dateNow += year
        return dateNow
    }

    fun getLastDate(index: Int, date: ArrayList<Int>): ArrayList<ArrayList<Int>> {
        val lastDate = ArrayList<ArrayList<Int>>()
        if ((date[2] - 2020) % 4 == 0) monthsDays[1] = 29
        var dayCount = date[0]
        for (d in 0..valuesList[index]) {
            lastDate += if (dayCount >= 1) {
                arrayListOf(date[0]-d, date[1], date[2])
            } else {
                arrayListOf(monthsDays[date[1]]+dayCount, date[1]-1, date[2])
            }
            dayCount--
        }

        return lastDate
    }

    var paint: Paint? = null
    var canvas: Canvas? = null
    var newDirectory: File? = null
    val date = getTodayDate()

    fun drawPeriodReport(nameList: java.util.ArrayList<String>, currentList: java.util.ArrayList<Int>,
                                 addedList: java.util.ArrayList<Int>, subtractedList: java.util.ArrayList<Int>,
                                 totalQuantity: Int, totalSubtract: Int, totalAdd: Int, page: Int,
                                 date: String, lastPage: Boolean) {
        paint!!.style = Paint.Style.STROKE
        paint!!.strokeJoin = Paint.Join.ROUND
        paint!!.strokeCap = Paint.Cap.ROUND
        paint!!.strokeWidth = 3f
        paint!!.color = gray
        canvas!!.drawColor(white)
        val lines = pdfLines.copyOfRange(4, pdfLines.size)
        canvas!!.drawLines(lines, paint!!)

        paint!!.typeface = Typeface.MONOSPACE
        paint!!.textAlign = Paint.Align.CENTER
        paint!!.style = Paint.Style.FILL_AND_STROKE
        paint!!.textSize = 14f
        paint!!.color = black
        paint!!.strokeWidth = 2f

        canvas!!.drawText("$date : $page", 299f, 50f, paint!!)
        if (lastPage) {
            canvas!!.drawText("TOTAL IN: $totalAdd", 149.5f, 793f, paint!!)
            canvas!!.drawText("TOTAL OUT: $totalSubtract", 299f, 793f, paint!!)
            canvas!!.drawText("TOTAL IN HAND: $totalQuantity", 448.5f, 793f, paint!!)
        }

        canvas!!.drawText("Stock", xLocationsOfText[2], 145.14f, paint!!)
        canvas!!.drawText("Import", xLocationsOfText[3], 145.14f, paint!!)
        canvas!!.drawText("Export", xLocationsOfText[4], 145.14f, paint!!)

        for (n in nameList.indices) {
            canvas!!.drawText(nameList[n], 93.97f, yLocationsOfText[n], paint!!)

            canvas!!.drawText("${currentList[n]}", xLocationsOfText[2],
                yLocationsOfText[n], paint!!)
            canvas!!.drawText("${addedList[n]}", xLocationsOfText[3],
                yLocationsOfText[n], paint!!)
            canvas!!.drawText("${subtractedList[n]}", xLocationsOfText[4],
                yLocationsOfText[n], paint!!)
        }
        canvas!!.drawText("${currentList.sum()}", xLocationsOfText[2], 734.01f, paint!!)
        canvas!!.drawText("${addedList.sum()}", xLocationsOfText[3], 734.01f, paint!!)
        canvas!!.drawText("${subtractedList.sum()}", xLocationsOfText[4], 734.01f, paint!!)
        //Total stock and total import and total export
    }

    fun saveWeekReportToFile(reports: List<Report>, context: Context) {
        if (reports.isEmpty()) return
        paint = Paint()
        try {
            newDirectory = File("/storage/emulated/0/Inventory reports")
            if (!newDirectory!!.exists()) newDirectory!!.mkdir()
        }
        catch (_: Exception) {
            Log.d("StockService", "Exception in saveReport")
        }
        val pages = numberOfPages(reports.size, 5)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) makePdfFile(pages, reports)
        else makePngFile(pages, reports, context)
    }

    private fun makePngFile(pages: Int, reports: List<Report>, context: Context) {
        try {
            val nameList = arrayListOf<String>()
            val currentList = arrayListOf<Int>()
            val stockLastWeekList = arrayListOf<Int>()
            val lastDay = reports[0].lastDayOfWeek
            val reportDate = "${reports[0].dateDay}/${reports[0].dateMonth}" +
                    "/${reports[0].dateYear}"
            val outListOfList = arrayListOf(listOf<Int>())
            val totalOutList = arrayListOf<Int>()

            for (page in 1..pages) {
                for (indx in ((page-1)*5) until lastIndex(page, pages, reports.size, 5)) {
                    nameList += reports[indx].productName
                    currentList += reports[indx].quantityInStock
                    stockLastWeekList += reports[indx].stockLastWeek
                    val outList = listOf(reports[indx].outFirstDay, reports[indx].outSecondDay,
                        reports[indx].outThirdDay, reports[indx].outFourthDay,
                        reports[indx].outFifthDay, reports[indx].outSixthDay,
                        reports[indx].outSeventhDay)
                    outListOfList.add(indx % 5, outList)
                    totalOutList += outList.sum()
                }

                val file = File(newDirectory, "Week-report-${date[0]}-${date[1]}-${date[2]}-$page.png")
                file.setReadable(true)
                val fileOutputStream = FileOutputStream(file.absolutePath)
                val bitmap = Bitmap.createBitmap(598, 843, Bitmap.Config.ARGB_8888)
                canvas = Canvas(bitmap)

                drawWeekReport(nameList, currentList, stockLastWeekList, lastDay,
                    reportDate, outListOfList, totalOutList, page)

                bitmap.compress(Bitmap.CompressFormat.PNG, 80, fileOutputStream)
                bitmap.recycle()
                fileOutputStream.flush()
                fileOutputStream.close()
                MediaScannerConnection.scanFile(context, arrayOf(file.absolutePath),
                    null, null)

                nameList.clear()
                currentList.clear()
                stockLastWeekList.clear()
                totalOutList.clear()
                outListOfList.clear()
            }
        }
        catch(e: Exception) {
            Log.d("StockService", "$e ********** Exception in makePngFile **********")
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun makePdfFile(pages: Int, reports: List<Report>) {
        try {
            val document = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(598, 843, 1).create()

            val nameList = arrayListOf<String>()
            val currentList = arrayListOf<Int>()
            val stockLastWeekList = arrayListOf<Int>()
            val lastDay = reports[0].lastDayOfWeek
            val reportDate = "${reports[0].dateDay}/${reports[0].dateMonth}" +
                    "/${reports[0].dateYear}"
            val outListOfList = arrayListOf(listOf<Int>())
            val totalOutList = arrayListOf<Int>()

            for (page in 1..pages) {
                for (indx in ((page-1)*5) until lastIndex(page, pages, reports.size, 5)) {
                    nameList += reports[indx].productName
                    currentList += reports[indx].quantityInStock
                    stockLastWeekList += reports[indx].stockLastWeek
                    val outList = listOf(reports[indx].outFirstDay, reports[indx].outSecondDay,
                        reports[indx].outThirdDay, reports[indx].outFourthDay,
                        reports[indx].outFifthDay, reports[indx].outSixthDay,
                        reports[indx].outSeventhDay)
                    outListOfList.add(indx % 5, outList)
                    totalOutList += outList.sum()
                }
                val dPage = document.startPage(pageInfo)
                canvas = dPage.canvas

                drawWeekReport(nameList, currentList, stockLastWeekList, lastDay,
                    reportDate, outListOfList, totalOutList, page)

                nameList.clear()
                currentList.clear()
                stockLastWeekList.clear()
                totalOutList.clear()
                outListOfList.clear()
                document.finishPage(dPage)
            }

            val file = File(newDirectory, "Week-report-${date[0]}-${date[1]}-${date[2]}.pdf")
            document.writeTo(FileOutputStream(file))
            document.close()
        }
        catch(_: Exception) {
            Log.d("StockService", "Exception in makePdfFile")
        }

    }

    private fun drawWeekReport(nameList: java.util.ArrayList<String>, currentList: java.util.ArrayList<Int>,
                               stockLastWeekList: java.util.ArrayList<Int>, lastDay: Int,
                               date: String, outListOfList: java.util.ArrayList<List<Int>>,
                               totalOutList: java.util.ArrayList<Int>, page: Int) {
        paint!!.style = Paint.Style.STROKE
        paint!!.strokeJoin = Paint.Join.ROUND
        paint!!.strokeCap = Paint.Cap.ROUND
        paint!!.strokeWidth = 3f
        paint!!.color = gray
        canvas!!.drawColor(white)
        canvas!!.drawLines(pdfLines, paint!!)

        paint!!.typeface = Typeface.create("Arial", Typeface.ITALIC)
        paint!!.textAlign = Paint.Align.CENTER
        paint!!.style = Paint.Style.FILL_AND_STROKE
        paint!!.textSize = 16f
        paint!!.color = black
        paint!!.strokeWidth = 2f

        canvas!!.drawText("$date : $page", 320f, 50f, paint!!)
        canvas!!.drawText("Total out: ${totalOutList.sum()}", 199.33f, 793f, paint!!)
        canvas!!.drawText("Total current: ${currentList.sum()}", 398.66f, 793f, paint!!)

        // Full page contain five products

        for (i in 1..nameList.size) {
            val twoNames = nameParts(nameList[i-1])
            canvas!!.drawText(twoNames[0], xLocationsOfText[i], 125.41f, paint!!)//**** old y: 105.68f
            canvas!!.drawText(twoNames[1], xLocationsOfText[i], 145.14f, paint!!)//**** old y: 125.41f
            canvas!!.drawText("${stockLastWeekList[i-1]}", xLocationsOfText[i], 164.87f, paint!!)// Stock last week old y: 145.14f
            canvas!!.drawText("${currentList[i-1]}", xLocationsOfText[i], 734.01f, paint!!)// Current
            //**** Max text size = 11 char. ******** Product name = 22 char
        }

        for (x in 0..nameList.size) {
            if (xLocationsOfText[x] == 37.5f) {
                for (i in 0..6) {
                    canvas!!.drawText(getDaysNames(lastDay)[i], 37.5f,
                        yLocationsOfText[i], paint!!)
                    // From 0 to 6 is the day of the week
                }
                canvas!!.drawText("Week out", 37.5f, yLocationsOfText[7], paint!!)//7 is total out in this week
            }
            else {
                for (y in yLocationsOfText.indices) {
                    if (y == 7) {
                        canvas!!.drawText("${totalOutList[x-1]}", xLocationsOfText[x],
                            yLocationsOfText[y], paint!!)
                        // Total out
                    }
                    else {
                        canvas!!.drawText("${outListOfList[x-1][y]}", xLocationsOfText[x],
                            yLocationsOfText[y], paint!!)
                        // Every y in x is out per product per day
                    }
                }
            }
        }
    }

    private lateinit var builder: Notification.Builder

    @SuppressLint("UnspecifiedImmutableFlag")
    @RequiresApi(Build.VERSION_CODES.M)
    fun notifyMe(context: Context, title: String, txt: String) {
        pagerCount = 2
        val notificationManager1 = context.getSystemService(
            Service.NOTIFICATION_SERVICE
        ) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                "a7m3d.business.inventorycontrol", "Test notification",
                NotificationManager.IMPORTANCE_DEFAULT)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(false)
            notificationManager1.createNotificationChannel(notificationChannel)

            builder = Notification.Builder(context, "a7m3d.business.inventorycontrol")
                .setContentTitle(title)
                .setContentText(txt)
                .setContentIntent(PendingIntent.getActivity(context, 0,
                    Intent(context, MainActivity::class.java), 0))
                .setSmallIcon(ic_notifications)
        } else {

            builder = Notification.Builder(context)
                .setContentTitle(title)
                .setContentText(txt)
                .setContentIntent(PendingIntent.getActivity(context, 0,
                    Intent(context, MainActivity::class.java), 0))
                .setSmallIcon(ic_notifications)
        }
        notificationManager1.notify(1234, builder.build())
    }
}