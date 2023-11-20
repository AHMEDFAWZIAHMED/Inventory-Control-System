package a7m3d.business.inventorycontrol.adapters

import a7m3d.business.inventorycontrol.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class CategoryAdapter constructor(private val categoryList: List<String>) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    var onNameClick: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.category_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.cName.text = categoryList[position]
    }

    override fun getItemCount(): Int = categoryList.size

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val cCard: CardView = itemView.findViewById(R.id.cCard)
        val cName: TextView = itemView.findViewById(R.id.cName)

        init {
            cCard.setOnClickListener { onNameClick?.invoke(categoryList[bindingAdapterPosition]) }
        }
    }
}