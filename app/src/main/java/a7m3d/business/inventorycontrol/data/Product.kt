package a7m3d.business.inventorycontrol.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Product(
    //@PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "name") val productName: String,
    @ColumnInfo(name = "category") val productCategory: String,
    @ColumnInfo(name = "imagePath") val productImgPath: String,
    @ColumnInfo(name = "user") val userNote: String,
    @ColumnInfo(name = "usb") val usbNote: String,
    @ColumnInfo(name = "price") val productPrice: Double,
    @ColumnInfo(name = "quantity") val quantityInStock: Int,
    @ColumnInfo(name = "addedQuantity") val totalQuantityIn: Int,
    @ColumnInfo(name = "subtractedQuantity") val totalQuantityOut: Int,
    @ColumnInfo(name = "addedThisTime") val totalInThisTime: Int,
    @ColumnInfo(name = "subtractedThisTime") val totalOutThisTime: Int,
    @ColumnInfo(name = "subtractedThisWeek") val totalOutThisWeek: Int,
    @ColumnInfo(name = "addedLastTime") val totalInLastTime: Int,
    @ColumnInfo(name = "subtractedLastTime") val totalOutLastTime: Int,
    @ColumnInfo(name = "subtractedLastWeek") val totalOutLastWeek: Int,
    @ColumnInfo(name = "subtractedNextTime") val totalOutNextTime: Int,
    @ColumnInfo(name = "subtractedNextWeek") val totalOutNextWeek: Int,
    @ColumnInfo(name = "color") val statusColor: Int,
    @ColumnInfo(name = "timeDay") val dateDay: Int,
    @ColumnInfo(name = "timeMonth") val dateMonth: Int,
    @ColumnInfo(name = "timeYear") val dateYear: Int,
    @ColumnInfo(name = "active") val isActive: Boolean,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)
