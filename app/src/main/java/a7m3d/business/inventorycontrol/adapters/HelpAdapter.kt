package a7m3d.business.inventorycontrol.adapters

import a7m3d.business.inventorycontrol.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class HelpAdapter(private val frontLines: List<String>,
              private val briefs: List<String>) : RecyclerView.Adapter<HelpAdapter.ViewHolder>() {

    var onNameClick: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.help_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.frontLine.text = frontLines[position]
        holder.brief.text = briefs[position]
    }

    override fun getItemCount(): Int = frontLines.size

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val helpCardView: CardView = itemView.findViewById(R.id.help_card)
        val frontLine: TextView = itemView.findViewById(R.id.front_line)
        val brief: TextView = itemView.findViewById(R.id.brief)

        init {
            helpCardView.setOnClickListener { onNameClick?.invoke(heading(bindingAdapterPosition)) }
        }
        private fun heading(position: Int): String {
            return when(position) {
                0 -> "abt"
                1 -> "str"
                2 -> "reg"
                3 -> "inv"
                4 -> "his"
                5 -> "ntf"
                else -> "stg"
            }
        }
    }
}