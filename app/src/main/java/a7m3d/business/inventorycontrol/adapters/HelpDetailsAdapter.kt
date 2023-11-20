package a7m3d.business.inventorycontrol.adapters

import a7m3d.business.inventorycontrol.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView

class HelpDetailsAdapter constructor(private val heading: String,
     private val fragmentActivity: FragmentActivity) :
     RecyclerView.Adapter<HelpDetailsAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.help_details_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (heading) {
            "abt" -> {
                holder.firstText.text = fragmentActivity.getText(R.string.about)
                holder.secondText.text = fragmentActivity.getText(R.string.about_more_details)
            }
            "str" -> {
                holder.firstText.text = fragmentActivity.getText(R.string.how_to_start)
                holder.secondText.text = fragmentActivity.getText(R.string.start_more_details)
            }
            "reg" -> {
                holder.firstText.text = fragmentActivity.getText(R.string.menu_register)
                holder.secondText.text = fragmentActivity.getText(R.string.register_more_details)
            }
            "inv" -> {
                holder.firstText.text = fragmentActivity.getText(R.string.menu_inventory)
                holder.secondText.text = fragmentActivity.getText(R.string.inventory_more_details)
            }
            "his"-> {
                holder.firstText.text = fragmentActivity.getText(R.string.menu_history)
                holder.secondText.text = fragmentActivity.getText(R.string.history_more_details)
            }
            "ntf" -> {
                holder.firstText.text = fragmentActivity.getText(R.string.notifications)
                holder.secondText.text = fragmentActivity.getText(R.string.notifications_more_details)
            }
            "stg" -> {
                holder.firstText.text = fragmentActivity.getText(R.string.menu_settings)
                holder.secondText.text = fragmentActivity.getText(R.string.settings_more_details)
            }
        }
    }

    override fun getItemCount(): Int = 1

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val firstText: TextView = itemView.findViewById(R.id.first_text)
        val secondText: TextView = itemView.findViewById(R.id.second_text)
    }
}