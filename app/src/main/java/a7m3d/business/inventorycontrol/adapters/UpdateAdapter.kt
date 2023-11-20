package a7m3d.business.inventorycontrol.adapters

import a7m3d.business.inventorycontrol.Pro
import a7m3d.business.inventorycontrol.R
import a7m3d.business.inventorycontrol.data.Product
import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputEditText

class UpdateAdapter constructor (private val productList: List<Product>) :
    RecyclerView.Adapter<UpdateAdapter.ViewHolder>() {

    var onItemClick: ((String, String, String, String) -> Unit)? = null
    var onImageClick: (() -> Unit)? = null

    @SuppressLint("Range")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.update_view, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = productList[position]
        holder.updateName.setText(product.productName)
        holder.updateCount.setText(product.quantityInStock.toString())
        holder.updateCategory.setText(product.productCategory)
        holder.updateDetails.setText(product.userNote)

        val oImgPath = product.productImgPath

        if (Pro.nImgPath != "") {
            try {
                val bitmapFactory = BitmapFactory.decodeFile(Pro.nImgPath)
                if (bitmapFactory != null) {
                    holder.updateImage.setImageBitmap(bitmapFactory)
                }
            }
            catch (_: OutOfMemoryError) {
                Log.d("UpdateAdapter", "******** Out of memory")
            }
        }
        else if (oImgPath != "") {
            try {
                val bitmapFactory = BitmapFactory.decodeFile(oImgPath)
                if (bitmapFactory != null) {
                    holder.updateImage.setImageBitmap(bitmapFactory)
                }
            }
            catch (_: OutOfMemoryError) {
                Log.d("UpdateAdapter", "******** Out of memory")
            }
        }
    }

    override fun getItemCount(): Int = productList.size

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val updateName: TextInputEditText = itemView.findViewById(R.id.update_name)
        val updateCount: TextInputEditText = itemView.findViewById(R.id.update_count)
        val updateCategory: TextInputEditText = itemView.findViewById(R.id.update_category)
        val updateDetails: TextInputEditText = itemView.findViewById(R.id.update_details)
        val updateImage: ShapeableImageView = itemView.findViewById(R.id.update_image)
        private val updateAction: Button = itemView.findViewById(R.id.update_action)

        init {
            updateAction.setOnClickListener {
                onItemClick?.invoke(
                    updateName.text.toString(),
                    updateCount.text.toString(),
                    updateCategory.text.toString(),
                    updateDetails.text.toString()
                )
            }
            updateImage.setOnClickListener { onImageClick?.invoke() }
        }
    }
}