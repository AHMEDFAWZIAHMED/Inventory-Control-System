package a7m3d.business.inventorycontrol.adapters

import a7m3d.business.inventorycontrol.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class YearAdapter constructor(private val yearList: List<Int>) :
    RecyclerView.Adapter<YearAdapter.ViewHolder>() {

    var onNameClick: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.year_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.yNumber.text = yearList[position].toString()
    }

    override fun getItemCount(): Int = yearList.size

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val yCard: CardView = itemView.findViewById(R.id.yCard)
        val yNumber: TextView = itemView.findViewById(R.id.yNumber)

        init {
            yCard.setOnClickListener { onNameClick?.invoke(yearList[bindingAdapterPosition]) }
        }
    }
}