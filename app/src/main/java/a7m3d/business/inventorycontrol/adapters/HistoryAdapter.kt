package a7m3d.business.inventorycontrol.adapters

import a7m3d.business.inventorycontrol.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    var onNameClick: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.history_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {}

    override fun getItemCount(): Int = 1

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val transactionsCard: CardView = itemView.findViewById(R.id.transaction_card)
        private val reportsCard: CardView = itemView.findViewById(R.id.report_card)

        init {
            transactionsCard.setOnClickListener { onNameClick?.invoke("transaction") }
            reportsCard.setOnClickListener { onNameClick?.invoke("report") }
        }
    }
}