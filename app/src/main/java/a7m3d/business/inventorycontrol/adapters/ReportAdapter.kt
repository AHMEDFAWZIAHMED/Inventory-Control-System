package a7m3d.business.inventorycontrol.adapters

import a7m3d.business.inventorycontrol.Pro
import a7m3d.business.inventorycontrol.R
import a7m3d.business.inventorycontrol.data.Report
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView

class ReportAdapter constructor(private val reportList: List<List<Report>>) :
    RecyclerView.Adapter<ReportAdapter.ViewHolder>(){

    val ids1 = sequenceOf(
        R.id.first_day, R.id.second_day, R.id.third_day, R.id.forth_day, R.id.fifth_day,
                        R.id.sixth_day, R.id.seventh_day
    )
    val ids2 = sequenceOf(
        R.id.name_1_current, R.id.name_2_current, R.id.name_3_current, R.id.name_4_current,
                             R.id.name_5_current
    )
    val ids3 = sequenceOf(
        R.id.new_1_current, R.id.new_2_current, R.id.new_3_current, R.id.new_4_current,
                            R.id.new_5_current
    )
    val ids4 = sequenceOf(
        R.id.first_1_out, R.id.second_1_out, R.id.third_1_out, R.id.forth_1_out, R.id.fifth_1_out,
                          R.id.sixth_1_out, R.id.seventh_1_out,
        R.id.first_2_out, R.id.second_2_out, R.id.third_2_out, R.id.forth_2_out, R.id.fifth_2_out,
                          R.id.sixth_2_out, R.id.seventh_2_out,
        R.id.first_3_out, R.id.second_3_out, R.id.third_3_out, R.id.forth_3_out, R.id.fifth_3_out,
                          R.id.sixth_3_out, R.id.seventh_3_out,
        R.id.first_4_out, R.id.second_4_out, R.id.third_4_out, R.id.forth_4_out, R.id.fifth_4_out,
                          R.id.sixth_4_out, R.id.seventh_4_out,
        R.id.first_5_out, R.id.second_5_out, R.id.third_5_out, R.id.forth_5_out, R.id.fifth_5_out,
                          R.id.sixth_5_out, R.id.seventh_5_out
    )
    val ids5 = sequenceOf(
        R.id.week_1_out, R.id.week_2_out, R.id.week_3_out, R.id.week_4_out, R.id.week_5_out
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.report_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val reportsList = reportList[position]// reportsList size must be equal or less than 5
        val lastList = position == reportList.size-1
        val names = reportsList.asSequence().map { it.productName }
        val stockNow = reportsList.asSequence().map { it.quantityInStock }
        val stockBefore = reportsList.asSequence().map { it.stockLastWeek }
        var allOut = sequenceOf<Int>()
        reportsList.forEachIndexed { i, r ->
            allOut += sequenceOf(
                r.outFirstDay, r.outSecondDay, r.outThirdDay, r.outFourthDay, r.outFifthDay,
                r.outSixthDay, r.outSeventhDay
            )
            val group = mutableListOf<Int>()
            for (j in i*7 until (i+1)*7) {
                group.add(j%7, allOut.elementAt(j))
            }
            holder.totalOutSequence.elementAt(i).text = group.sum().toString()
            group.clear()
        }

        val firstReport = reportsList[0]
        val namesOfDays = Pro.getDaysNames(firstReport.lastDayOfWeek)
        val day = firstReport.dateDay
        val month = firstReport.dateMonth
        val year = firstReport.dateYear
        val pageNumber = getPageNumber(reportList.size)
        val dateAndPage = "$day/$month/$year: $pageNumber"

        names.forEachIndexed { i, productName ->
            val nameAndStock = "$productName\n${stockBefore.elementAt(i)}"
            holder.nameAndStockLastWeekSequence.elementAt(i).text = nameAndStock
            holder.currentSequence.elementAt(i).text = stockNow.elementAt(i).toString()
        }
        namesOfDays.forEachIndexed { i, dayName ->
            holder.daysSequence.elementAt(i).text = dayName
        }

        holder.date.text = dateAndPage

        if (position == 0) {
            Pro.weekTotalOut = 0
            Pro.weekTotalInHand = 0
        }
        Pro.weekTotalOut += allOut.sum()
        Pro.weekTotalInHand += stockNow.sum()
        if (lastList) {
            holder.totalOut.text = "TOTAL WEEK OUT:\n${Pro.weekTotalOut}"
            holder.totalCurrent.text = "TOTAL WEEK STOCK:\n${Pro.weekTotalInHand }"
        }
        allOut.forEachIndexed { i, v ->
            holder.outSequence.elementAt(i).text = v.toString()
        }
    }

    override fun getItemCount(): Int = reportList.size

    private fun getPageNumber(size: Int) : Int = if (size % 5 > 0) (size / 5) + 1 else size / 5

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val date: AppCompatTextView = itemView.findViewById(R.id.rDate)
        val totalOut: AppCompatTextView = itemView.findViewById(R.id.rTotal_out)
        val totalCurrent: AppCompatTextView = itemView.findViewById(R.id.rTotal_current)

        var daysSequence = sequenceOf<AppCompatTextView>()
        var nameAndStockLastWeekSequence = sequenceOf<AppCompatTextView>()
        var currentSequence = sequenceOf<AppCompatTextView>()
        var outSequence = sequenceOf<AppCompatTextView>()
        var totalOutSequence = sequenceOf<AppCompatTextView>()

        init { addToLists() }

        private fun addToLists() {
            ids1.forEach { daysSequence += itemView.findViewById(it) as AppCompatTextView }
            ids2.forEach {
                nameAndStockLastWeekSequence += itemView.findViewById(it) as AppCompatTextView
            }
            ids3.forEach { currentSequence += itemView.findViewById(it) as AppCompatTextView }
            ids4.forEach { outSequence += itemView.findViewById(it) as AppCompatTextView }
            ids5.forEach { totalOutSequence += itemView.findViewById(it) as AppCompatTextView }
        }
    }
}