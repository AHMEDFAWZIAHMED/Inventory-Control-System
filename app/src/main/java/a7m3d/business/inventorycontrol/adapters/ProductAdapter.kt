package a7m3d.business.inventorycontrol.adapters

import a7m3d.business.inventorycontrol.R
import a7m3d.business.inventorycontrol.data.Product
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView

class ProductAdapter constructor (private val productList: List<Product>) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    var onItemClick: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = productList[position]
        val year = product.dateYear
        val month = product.dateMonth
        val day = product.dateDay
        val date = "$day/$month/$year"
        val nImgPath = product.productImgPath

        holder.productName.text = product.productName
        holder.productCategory.text = product.productCategory
        holder.productQuantity.text = product.quantityInStock.toString()
        holder.productIn.text = product.totalQuantityIn.toString()
        holder.productOut.text = product.totalQuantityOut.toString()
        holder.usbNote.text = product.usbNote
        holder.userNote.text = product.userNote
        holder.productPrice.text = product.productPrice.toString()
        holder.totalPrice.text = (product.productPrice * product.quantityInStock).toString()
        holder.productDate.text = date

        if (nImgPath != "") {
            try {
                val bitmapFactory = BitmapFactory.decodeFile(nImgPath)
                if (bitmapFactory != null) {
                    holder.productImage.setImageBitmap(bitmapFactory)
                }
            }
            catch (_: OutOfMemoryError) {
                Log.d("ProductAdapter", "******** Out of memory")
            }
        }
    }

    override fun getItemCount(): Int = productList.size

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val productImage: ShapeableImageView = itemView.findViewById(R.id.product_image)
        val productName: TextView = itemView.findViewById(R.id.product_name)
        val productCategory: TextView = itemView.findViewById(R.id.product_category)
        val productQuantity: TextView = itemView.findViewById(R.id.product_quantity)
        val productIn: TextView = itemView.findViewById(R.id.product_in)
        val productOut: TextView = itemView.findViewById(R.id.product_out)
        val productDate: TextView = itemView.findViewById(R.id.product_date)
        val usbNote: TextView = itemView.findViewById(R.id.usb_note)
        val userNote: TextView = itemView.findViewById(R.id.user_note)
        val productPrice: TextView = itemView.findViewById(R.id.product_price)
        val totalPrice: TextView = itemView.findViewById(R.id.total_price)

        private val cardImage: CardView = itemView.findViewById(R.id.card_image)
        private val cardName: CardView = itemView.findViewById(R.id.card_name)
        private val cardIn: CardView = itemView.findViewById(R.id.card_in)
        private val cardOut: CardView = itemView.findViewById(R.id.card_out)
        private val cardCategory: CardView = itemView.findViewById(R.id.card_category)
        private val cardDetails: CardView = itemView.findViewById(R.id.card_details)
        private val cardHistory: CardView = itemView.findViewById(R.id.got_to_history)
        private val cardPrice: CardView = itemView.findViewById(R.id.card_price)

        private val productDelete: Button = itemView.findViewById(R.id.delete_product)

        init {
            cardImage.setOnClickListener { onItemClick?.invoke("img") }
            cardName.setOnClickListener { onItemClick?.invoke("nam") }
            cardIn.setOnClickListener { onItemClick?.invoke("in") }
            cardOut.setOnClickListener { onItemClick?.invoke("out") }
            cardCategory.setOnClickListener { onItemClick?.invoke("cat") }
            cardDetails.setOnClickListener { onItemClick?.invoke("det") }
            cardHistory.setOnClickListener { onItemClick?.invoke("his") }
            cardPrice.setOnClickListener { onItemClick?.invoke("pri") }

            productDelete.setOnClickListener { onItemClick?.invoke("del") }
        }
    }
}