package a7m3d.business.inventorycontrol.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Transaction(
    //@PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "change") val productChange: String,
    @ColumnInfo(name = "productNo") val productId: Int,
    @ColumnInfo(name = "name") val productName: String,
    @ColumnInfo(name = "imagePath") val productImgPath: String,
    @ColumnInfo(name = "addProduct") val productIn: Int,
    @ColumnInfo(name = "subtractProduct") val productOut: Int,
    @ColumnInfo(name = "tTimeDay") val tDateDay: Int,
    @ColumnInfo(name = "tTimeMonth") val tDateMonth: Int,
    @ColumnInfo(name = "tTimeYear") val tDateYear: Int,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)
