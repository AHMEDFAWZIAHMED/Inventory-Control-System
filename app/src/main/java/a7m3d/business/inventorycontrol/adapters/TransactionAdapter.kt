package a7m3d.business.inventorycontrol.adapters

import a7m3d.business.inventorycontrol.R
import a7m3d.business.inventorycontrol.data.Transaction
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView

class TransactionAdapter constructor (private val transactionList: List<Transaction>,
                                      private val fragmentActivity: FragmentActivity
) :
    RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    //var onItemClick: ((Transaction) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.transaction_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val deleted = fragmentActivity.getText(R.string.deleted)
        val restored = fragmentActivity.getText(R.string.restore)
        val tIn = fragmentActivity.getText(R.string.tIn)
        val tOut = fragmentActivity.getText(R.string.tOut)
        val changed = fragmentActivity.getText(R.string.changed)
        val tDate = fragmentActivity.getText(R.string.tDate)
        val titleName = fragmentActivity.getText(R.string.product_name)
        val titleImage = fragmentActivity.getText(R.string.product_picture)
        val titleCategory = fragmentActivity.getText(R.string.category2)
        val titleDetails = fragmentActivity.getText(R.string.details2)
        val titlePrice = fragmentActivity.getText(R.string.price)
        val emptyStock = fragmentActivity.getText(R.string.empty_stock)
        val deadStock = fragmentActivity.getText(R.string.dead_stock)
        val coldStock = fragmentActivity.getText(R.string.cold_stock)
        val veryColdStock = fragmentActivity.getText(R.string.very_cold_stock)
        val normal = fragmentActivity.getText(R.string.normal)
        val oneDay = fragmentActivity.getText(R.string.one_day)
        val twoDays = fragmentActivity.getText(R.string.two_days)
        val threeDays = fragmentActivity.getText(R.string.three_days)
        val oneWeek = fragmentActivity.getText(R.string.one_week)
        val emptyIn = fragmentActivity.getText(R.string.empty_in)
        //val color = fragmentActivity.getText(R.string.product_color)
        val red = fragmentActivity.getText(R.string.red)
        val orange = fragmentActivity.getText(R.string.orange)
        val yellow = fragmentActivity.getText(R.string.yellow)
        val green = fragmentActivity.getText(R.string.green)
        val blue = fragmentActivity.getText(R.string.blue)
        val lightBlue = fragmentActivity.getText(R.string.light_blue)
        val purple = fragmentActivity.getText(R.string.purple)
        val transaction = transactionList[position]
        val name = transaction.productName
        val changeText = when (transaction.productChange) {
            "Delete" -> "$name $deleted"
            "Restore" -> "$name $restored"
            "In" -> "$name: $tIn: ${transaction.productIn}"
            "Out" -> "$name: $tOut: ${transaction.productOut}"
            "Name" -> "$name: $titleName $changed"
            "Image" -> "$name: $titleImage $changed"
            "Category" -> "$name: $titleCategory $changed"
            "Note" -> "$name: $titleDetails $changed"
            "Price" -> "$name: $titlePrice $changed"
            "red" -> "$name: $red: $emptyStock"
            "orange 1" -> "$name: $orange: $emptyIn $oneDay"
            "orange 2" -> "$name: $orange: $emptyIn $twoDays"
            "orange 3" -> "$name: $orange: $emptyIn $threeDays"
            "yellow" -> "$name: $yellow: $emptyIn $oneWeek"
            "green" -> "$name: $green: $normal"
            "blue" -> "$name: $blue: $coldStock"
            "light_blue" -> "$name: $lightBlue: $veryColdStock"
            "purple" -> "$name: $purple: $deadStock"
            else -> ""
        }
        val day = transaction.tDateDay
        val month = transaction.tDateMonth
        val year = transaction.tDateYear
        val date = "$tDate: $day/$month/$year"
        val nImgPath = transaction.productImgPath
        holder.tDate.text = date
        holder.tChange.text = changeText
        if (nImgPath != "") {
            try {
                val bitmapFactory = BitmapFactory.decodeFile(nImgPath)
                if (bitmapFactory != null) {
                    holder.tImage.setImageBitmap(bitmapFactory)
                }
            }
            catch (_: OutOfMemoryError) {
                Log.d("HistoryAdapter", "******** Out of memory")
            }
        }
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val tChange: TextView = itemView.findViewById(R.id.tChange)
        val tImage: ShapeableImageView = itemView.findViewById(R.id.tImage)
        val tDate: TextView = itemView.findViewById(R.id.tDate)

    }
}