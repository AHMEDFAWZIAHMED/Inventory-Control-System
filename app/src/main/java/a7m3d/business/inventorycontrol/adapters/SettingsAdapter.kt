package a7m3d.business.inventorycontrol.adapters

import a7m3d.business.inventorycontrol.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView

class SettingsAdapter : RecyclerView.Adapter<SettingsAdapter.ViewHolder>() {

    var onNameClick: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.settings_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {}

    override fun getItemCount(): Int = 1

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val stocktaking: AppCompatButton = itemView.findViewById(R.id.stocktaking)
        private val language: AppCompatButton = itemView.findViewById(R.id.language)
        private val recycleBin: AppCompatButton = itemView.findViewById(R.id.recycle_bin)
        private val notifications: AppCompatButton = itemView.findViewById(R.id.notifications)

        init {
            stocktaking.setOnClickListener { onNameClick?.invoke("sto") }
            language.setOnClickListener { onNameClick?.invoke("lan") }
            recycleBin.setOnClickListener { onNameClick?.invoke("bin") }
            notifications.setOnClickListener { onNameClick?.invoke("not") }
        }
    }
}