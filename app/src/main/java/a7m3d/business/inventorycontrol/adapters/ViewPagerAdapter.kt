package a7m3d.business.inventorycontrol.adapters

import a7m3d.business.inventorycontrol.ui.help.HelpFragment
import a7m3d.business.inventorycontrol.ui.history.HistoryFragment
import a7m3d.business.inventorycontrol.ui.inventory.InventoryFragment
import a7m3d.business.inventorycontrol.ui.register.RegisterFragment
import a7m3d.business.inventorycontrol.ui.settings.SettingsFragment
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fa: FragmentActivity, startForResult: ActivityResultLauncher<Intent>):
    FragmentStateAdapter(fa) {
    private val registerFragment = RegisterFragment(startForResult)
    private val inventoryFragment = InventoryFragment(startForResult)
    private val historyFragment = HistoryFragment()
    private val helpFragment = HelpFragment()
    private val settingsFragment = SettingsFragment()

    override fun getItemCount(): Int = 5

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> registerFragment
            1 -> inventoryFragment
            2 -> historyFragment
            3 -> helpFragment
            else -> settingsFragment
        }
    }

}