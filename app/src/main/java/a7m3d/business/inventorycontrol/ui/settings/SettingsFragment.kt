package a7m3d.business.inventorycontrol.ui.settings

import a7m3d.business.inventorycontrol.Pro
import a7m3d.business.inventorycontrol.ProductApplication
import a7m3d.business.inventorycontrol.R
import a7m3d.business.inventorycontrol.adapters.EmptyAdapter
import a7m3d.business.inventorycontrol.adapters.InventoryAdapter
import a7m3d.business.inventorycontrol.adapters.SettingsAdapter
import a7m3d.business.inventorycontrol.data.Product
import android.os.Bundle
import androidx.fragment.app.Fragment
import a7m3d.business.inventorycontrol.databinding.FragmentSettingsBinding
import android.app.Dialog
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.SearchView
import androidx.core.view.get
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputLayout
import java.io.File
import java.util.Locale

class SettingsFragment : Fragment(), SearchView.OnQueryTextListener {

    private var _binding: FragmentSettingsBinding? = null

    private val settingsViewModel: SettingsViewModel by viewModels {
        SettingsViewModelFactory((activity?.application as ProductApplication).dataStore,
            (activity?.application as ProductApplication).repository,
            (activity?.application as ProductApplication).depository)
    }
    private lateinit var onBackPressed: OnBackPressedCallback
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var recyclerView: RecyclerView
    private lateinit var toolbar: MaterialToolbar
    private lateinit var lDialog: Dialog
    private lateinit var back: View
    private lateinit var search: MenuItem

    private lateinit var nDialog: Dialog
    private lateinit var oneDay: RadioButton
    private lateinit var twoDays: RadioButton
    private lateinit var threeDays: RadioButton
    private lateinit var fourDays: RadioButton
    private lateinit var fiveDays: RadioButton
    private lateinit var sixDays: RadioButton
    private lateinit var sevenDays: RadioButton

    private lateinit var cDialog: Dialog
    private lateinit var twoWeeks: RadioButton
    private lateinit var month: RadioButton
    private lateinit var twoMonths: RadioButton
    private lateinit var threeMonths: RadioButton

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onBackPressed = object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (Pro.sAdapterCount == 1) showSettings()
                else {
                    val viewP = requireActivity().findViewById<ViewPager2>(R.id.main_view_pager)
                    viewP.currentItem = 3
                    remove()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressed)

        toolbar = requireActivity().findViewById(R.id.topAppBar)
        back = toolbar[1]
        back.visibility  = View.VISIBLE
        back.setOnClickListener { onBackPressed.handleOnBackPressed() }
        search  = toolbar.menu.findItem(R.id.search)
        search.isVisible = false

        linearLayoutManager = LinearLayoutManager(
            requireActivity().applicationContext, RecyclerView.VERTICAL,false)
        recyclerView = binding.settingsRecyclerView
        recyclerView.layoutManager = linearLayoutManager

        showSettings()
        Handler(Looper.getMainLooper()).post { setNotificationAndStocktakingDialogs() }
    }

    override fun onResume() {
        if (Pro.sAdapterCount == 0) { showSettings() }
        else { showRecycleBin() }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressed)
        back.visibility  = View.VISIBLE
        back.setOnClickListener { onBackPressed.handleOnBackPressed() }
        super.onResume()
    }

    override fun onStop() {
        onBackPressed.remove()
        super.onStop()
    }

    private fun showSettings() {
        Pro.sAdapterCount = 0
        search.isVisible = false
        val settingsAdapter = SettingsAdapter()
        recyclerView.adapter = settingsAdapter
        settingsAdapter.onNameClick = {
            when (it) {
                "sto" -> stocktakingInterval()
                "lan" -> saveLanguage()
                "bin" -> showRecycleBin()
                "not" -> notificationInterval()
            }
        }
    }

    private fun setNotificationAndStocktakingDialogs() {
        nDialog = Dialog(requireContext())
        nDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        nDialog.setContentView(R.layout.notification_dialog)
        nDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        oneDay = nDialog.findViewById(R.id.one_day)
        twoDays = nDialog.findViewById(R.id.two_days)
        threeDays = nDialog.findViewById(R.id.three_days)
        fourDays = nDialog.findViewById(R.id.four_days)
        fiveDays = nDialog.findViewById(R.id.five_days)
        sixDays = nDialog.findViewById(R.id.six_days)
        sevenDays = nDialog.findViewById(R.id.seven_days)

        oneDay.setOnClickListener { settingsViewModel.updateNotificationChoice(1) }
        twoDays.setOnClickListener { settingsViewModel.updateNotificationChoice(2) }
        threeDays.setOnClickListener { settingsViewModel.updateNotificationChoice(3) }
        fourDays.setOnClickListener { settingsViewModel.updateNotificationChoice(4) }
        fiveDays.setOnClickListener { settingsViewModel.updateNotificationChoice(5) }
        sixDays.setOnClickListener { settingsViewModel.updateNotificationChoice(6) }
        sevenDays.setOnClickListener { settingsViewModel.updateNotificationChoice(7) }

        cDialog = Dialog(requireContext())
        cDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        cDialog.setContentView(R.layout.choosing_dailog)
        cDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        twoWeeks = cDialog.findViewById(R.id.two_weeks)
        month = cDialog.findViewById(R.id.month)
        twoMonths = cDialog.findViewById(R.id.two_months)
        threeMonths = cDialog.findViewById(R.id.three_months)

        twoWeeks.setOnClickListener { settingsViewModel.updatePeriodChoice(15) }
        month.setOnClickListener { settingsViewModel.updatePeriodChoice(30) }
        twoMonths.setOnClickListener { settingsViewModel.updatePeriodChoice(60) }
        threeMonths.setOnClickListener { settingsViewModel.updatePeriodChoice(90) }
    }

    private fun notificationInterval() {
        settingsViewModel.checkNotificationChoice()
        when (Pro.notificationChoice) {
            1 -> oneDay.isChecked = true
            2 -> twoDays.isChecked = true
            3 -> threeDays.isChecked = true
            4 -> fourDays.isChecked = true
            5 -> fiveDays.isChecked = true
            6 -> sixDays.isChecked = true
            7 -> sevenDays.isChecked = true
        }
        nDialog.show()
    }

    private fun showRecycleBin() {
        Pro.sAdapterCount = 1
        search.isVisible = true
        settingsViewModel.getDeletedProducts.observe(viewLifecycleOwner) { products ->
            products?.let {
                showProducts(it)
                val searchView = search.actionView as SearchView
                searchView.isSubmitButtonEnabled = true
                searchView.setOnQueryTextListener(this@SettingsFragment)
            }

        }
    }

    private fun showProducts(products: List<Product>) {
        if (products.isNotEmpty()) {
            val msg = getText(R.string.current)
            val inventoryAdapter = InventoryAdapter(products, msg)
            recyclerView.adapter = inventoryAdapter
            inventoryAdapter.onItemClick = {
                showRestoreOrDeleteDialog(it)
            }
        }
        else {
            val name = getText(R.string.recycle_bin)
            val empty = getText(R.string.empty2)
            recyclerView.adapter = EmptyAdapter("$name $empty")
        }
    }

    private fun showRestoreOrDeleteDialog(product: Product) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.custom_dailog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val textLayout: TextInputLayout = dialog.findViewById(R.id.item_update_label)
        val updateButton: AppCompatButton = dialog.findViewById(R.id.update_button)
        val cancelButton: AppCompatButton = dialog.findViewById(R.id.cancel_button)

        textLayout.removeAllViews()
        updateButton.text = getText(R.string.restore)
        cancelButton.text = getText(R.string.delete)
        cancelButton.setOnClickListener {
            settingsViewModel.deleteProduct(product)
            deletePhoto(product.productImgPath)
            dialog.dismiss()
        }
        updateButton.setOnClickListener {
            settingsViewModel.updateVisibility(true, product)
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun deletePhoto(path: String) {
        if (path != "") {
            try {
                val file = File(path)
                if (file.exists()) file.delete()
            }
            catch (_: Exception) {
                Toast.makeText(requireContext(), "Something went wrong!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun stocktakingInterval() {
        settingsViewModel.checkPeriodChoice()
        when (Pro.periodChoice) {
            15 -> twoWeeks.isChecked = true
            30 -> month.isChecked = true
            60 -> twoMonths.isChecked = true
            90 -> threeMonths.isChecked = true
        }
        cDialog.show()
    }

    private fun saveLanguage() {
        lDialog = Dialog(requireContext())
        lDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        lDialog.setContentView(R.layout.language_dialog)
        lDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val en: AppCompatButton = lDialog.findViewById(R.id.en)
        val ar: AppCompatButton = lDialog.findViewById(R.id.ar)
        en.setOnClickListener { updateLanguage(0) }
        ar.setOnClickListener { updateLanguage(1) }

        lDialog.show()
    }

    private fun updateLanguage(lang: Int) {
        settingsViewModel.updateLanguageChoice(lang)
        val choice = Pro.languageList[lang]
        val locale = Locale(choice[0], choice[1])
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            val config = requireActivity().baseContext.resources.configuration
            config.setLocale(locale)
            requireActivity().baseContext.createConfigurationContext(config).resources
        }
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N) {
            val config1 = Configuration()
            config1.locale = locale
            requireActivity().baseContext.resources.updateConfiguration(config1,
                requireActivity().baseContext.resources.displayMetrics)
        }
        lDialog.dismiss()
        requireActivity().recreate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null) {
            settingsViewModel.searchDeletedProducts("%$newText%")
                .observe(viewLifecycleOwner) { products ->
                    products?.let { showProducts(it) }
            }
        }
        return true
    }

}