package a7m3d.business.inventorycontrol

import a7m3d.business.inventorycontrol.adapters.ViewPagerAdapter
import androidx.appcompat.app.AppCompatActivity
import a7m3d.business.inventorycontrol.databinding.ActivityMainBinding
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_NO_CREATE
import android.content.Context
import android.content.Intent
import android.os.*
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.File
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavView: BottomNavigationView
    private lateinit var viewPager: ViewPager2
    private lateinit var pagerAdapter: ViewPagerAdapter
    private lateinit var topAppbar: MaterialToolbar
    private lateinit var startForResult: ActivityResultLauncher<Intent>

    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startForResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) { result ->
            val path: String
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val daStPath = MediaStore.Images.Media.DATA
                val filePathColumn: Array<String> = arrayOf(daStPath)
                val cursor = result.data!!.data!!.let { contentResolver.query(
                    it, filePathColumn, null, null, null) }!!
                cursor.moveToFirst()
                path = cursor.getString(cursor.getColumnIndex(filePathColumn[0]))
                cursor.close()
                importFile(path)
                viewPager.adapter = pagerAdapter
                viewPager.currentItem = Pro.pagerCount
            }
        }

        stopService(Intent(this.applicationContext, StockService::class.java))

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottomNavView = binding.navView
        viewPager = binding.mainViewPager
        topAppbar = binding.topAppBar

        showViewPager2()
    }

    override fun onResume() {
        viewPager.currentItem = Pro.pagerCount
        super.onResume()
    }

    private fun showBottomNavigationView() {

        bottomNavView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.nav_register -> {
                    viewPager.currentItem = 0
                    true
                }
                R.id.nav_inventory -> {
                    viewPager.currentItem = 1
                    true
                }
                R.id.nav_history -> {
                    viewPager.currentItem = 2
                    true
                }
                R.id.nav_help -> {
                    viewPager.currentItem = 3
                    true
                }
                else -> {
                    viewPager.currentItem = 4
                    true
                }
            }
        }
        startAlarm()
    }

    private fun showViewPager2() {

        pagerAdapter = ViewPagerAdapter(this, startForResult)
        viewPager.adapter = pagerAdapter
        viewPager.currentItem = Pro.pagerCount

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                bottomNavView.menu.getItem(position).isChecked = true
                Pro.pagerCount = position
                when (position) {
                    0 -> topAppbar.title = getText(R.string.menu_register)
                    1 -> topAppbar.title = getText(R.string.menu_inventory)
                    2 -> topAppbar.title = getText(R.string.menu_history)
                    3 -> topAppbar.title = getText(R.string.menu_help)
                    4 -> topAppbar.title = getText(R.string.menu_settings)
                }
            }
        })
        showBottomNavigationView()
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun startAlarm() {

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val noticeIntent = Intent(this.applicationContext, StockService::class.java)
        val alarmNotRunning = (PendingIntent.getService(
            this.applicationContext, 0, noticeIntent, FLAG_NO_CREATE) == null)
        if (alarmNotRunning) {
            val pendingIntent = PendingIntent.getService(
                this.applicationContext, 0, noticeIntent, 0)

            alarmManager.setRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime()+60000*60*24,
                60000*60*24,
                pendingIntent)// Trigger:60000*60*24=1day Interval:60000*60*24=1day *DO NOT CHANGE!
        }
    }

    private fun importFile(path: String) {
        val file = applicationContext?.filesDir
        val newF = File(file, File(path).name)
        if (!newF.exists()) newF.mkdir()
        File(path).copyRecursively(newF, true)
        Pro.nImgPath = newF.absolutePath

    }
}
