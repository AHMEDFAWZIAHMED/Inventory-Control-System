package a7m3d.business.inventorycontrol.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import kotlinx.coroutines.flow.Flow

@Dao
interface ReportDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(report: Report)

    @Query("SELECT * FROM report")
    fun getReports(): Flow<List<Report>>

    @Query("SELECT * FROM report WHERE timeDay = :day AND timeMonth = :month AND timeYear = :year")
    fun getReport(day: Int, month: Int, year: Int): Flow<List<Report>>

    @Query("SELECT timeYear FROM report")
    fun getReportYears(): Flow<List<Int>>

    @Query("SELECT timeMonth FROM report WHERE timeYear = :year")
    fun getReportMonths(year: Int): Flow<List<Int>>

    @Query("SELECT timeDay FROM report WHERE timeMonth = :month AND timeYear = :year")
    fun getReportDays(month: Int, year: Int): Flow<List<Int>>
}