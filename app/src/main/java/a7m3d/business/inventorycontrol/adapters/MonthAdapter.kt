package a7m3d.business.inventorycontrol.adapters

import a7m3d.business.inventorycontrol.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class MonthAdapter constructor(private val monthList: List<Int>) :
    RecyclerView.Adapter<MonthAdapter.ViewHolder>() {

    var onNameClick: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.month_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mNumber.text = monthList[position].toString()
    }

    override fun getItemCount(): Int = monthList.size

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val mCard: CardView = itemView.findViewById(R.id.mCard)
        val mNumber: TextView = itemView.findViewById(R.id.mNumber)

        init {
            mCard.setOnClickListener { onNameClick?.invoke(monthList[bindingAdapterPosition]) }
        }
    }
}