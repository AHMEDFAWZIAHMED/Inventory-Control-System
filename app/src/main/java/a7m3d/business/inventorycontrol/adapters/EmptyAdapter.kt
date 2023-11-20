package a7m3d.business.inventorycontrol.adapters

import a7m3d.business.inventorycontrol.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EmptyAdapter constructor(private val msg: String) :
    RecyclerView.Adapter<EmptyAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.empty_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.empty.text = msg
    }

    override fun getItemCount(): Int = 1

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val empty: TextView = itemView.findViewById(R.id.empty)
    }
}