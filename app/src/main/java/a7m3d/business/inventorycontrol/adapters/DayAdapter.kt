package a7m3d.business.inventorycontrol.adapters

import a7m3d.business.inventorycontrol.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class DayAdapter constructor(private val dayList: List<Int>) :
    RecyclerView.Adapter<DayAdapter.ViewHolder>(){

    var onNameClick: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.day_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.dNumber.text = dayList[position].toString()
    }

    override fun getItemCount(): Int = dayList.size

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val dCard: CardView = itemView.findViewById(R.id.dCard)
        val dNumber: TextView = itemView.findViewById(R.id.dNumber)

        init {
            dCard.setOnClickListener { onNameClick?.invoke(dayList[bindingAdapterPosition]) }
        }
    }
}