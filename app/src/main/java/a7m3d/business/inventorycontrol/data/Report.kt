package a7m3d.business.inventorycontrol.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Report(
    @ColumnInfo(name = "name") val productName: String,
    @ColumnInfo(name = "quantity") val quantityInStock: Int,
    @ColumnInfo(name = "quantityLastWeek") val stockLastWeek: Int,
    @ColumnInfo(name = "interval") val stocktakingInterval: String,
    @ColumnInfo(name = "first") val outFirstDay: Int,
    @ColumnInfo(name = "second") val outSecondDay: Int,
    @ColumnInfo(name = "third") val outThirdDay: Int,
    @ColumnInfo(name = "fourth") val outFourthDay: Int,
    @ColumnInfo(name = "fifth") val outFifthDay: Int,
    @ColumnInfo(name = "sixth") val outSixthDay: Int,
    @ColumnInfo(name = "seventh") val outSeventhDay: Int,
    @ColumnInfo(name = "weekend") val lastDayOfWeek: Int,
    @ColumnInfo(name = "timeDay") val dateDay: Int,
    @ColumnInfo(name = "timeMonth") val dateMonth: Int,
    @ColumnInfo(name = "timeYear") val dateYear: Int,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)
