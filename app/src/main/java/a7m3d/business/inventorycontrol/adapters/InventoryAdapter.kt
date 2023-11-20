package a7m3d.business.inventorycontrol.adapters

import a7m3d.business.inventorycontrol.R
import a7m3d.business.inventorycontrol.data.Product
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView

class InventoryAdapter constructor (private val productList: List<Product>,
                                    private val msg: CharSequence) :
    RecyclerView.Adapter<InventoryAdapter.ViewHolder>() {

    var onItemClick: ((Product) -> Unit)? = null
    //val recList = productList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.inventory_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pName = productList[position].productName
        val qStock = productList[position].quantityInStock
        val quantity = "$msg: $qStock"
        val nImgPath = productList[position].productImgPath
        holder.vName.text = pName
        holder.vQuantity.text = quantity
        holder.pCard.setCardBackgroundColor(productList[position].statusColor)
        if (nImgPath != "") {
            try {
                val bitmapFactory = BitmapFactory.decodeFile(nImgPath)
                if (bitmapFactory != null) {
                    holder.vImage.setImageBitmap(bitmapFactory)
                }
            }
            catch (_: OutOfMemoryError) {
                Log.d("InventoryAdapter", "******** Out of memory")
            }
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val vName: TextView = itemView.findViewById(R.id.pName)
        val vImage: ShapeableImageView = itemView.findViewById(R.id.pImage)
        val vQuantity: TextView = itemView.findViewById(R.id.pQuantity)
        val pCard: CardView = itemView.findViewById(R.id.pCard)

        init {
            pCard.setOnClickListener { onItemClick?.invoke(productList[bindingAdapterPosition]) }
        }
    }

}