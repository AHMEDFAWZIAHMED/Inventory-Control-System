package a7m3d.business.inventorycontrol.ui.inventory

import a7m3d.business.inventorycontrol.Pro
import a7m3d.business.inventorycontrol.ProductApplication
import a7m3d.business.inventorycontrol.R
import a7m3d.business.inventorycontrol.adapters.*
import a7m3d.business.inventorycontrol.data.Product
import android.os.Bundle
import androidx.fragment.app.Fragment
import a7m3d.business.inventorycontrol.databinding.FragmentInventoryBinding
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.provider.MediaStore
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.SearchView
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputLayout

class InventoryFragment(private val startForResult: ActivityResultLauncher<Intent>): Fragment(),
    SearchView.OnQueryTextListener {

    private var _binding: FragmentInventoryBinding? = null

    private val inventoryViewModel: InventoryViewModel by viewModels {
        InventoryViewModelFactory((requireActivity().application as ProductApplication).repository,
            (requireActivity().application as ProductApplication).depository)
    }

    private lateinit var onBackPressed: OnBackPressedCallback
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var recyclerView: RecyclerView
    private lateinit var back: View
    private lateinit var search: MenuItem
    private lateinit var searchView: SearchView
    private lateinit var msg: CharSequence
    private lateinit var stockMsg: CharSequence
    private lateinit var productMsg: CharSequence
    private lateinit var totalStock: TextView
    private lateinit var totalProduct: TextView

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInventoryBinding.inflate(inflater, container, false)
        //val root: View =  binding.root
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        onBackPressed = object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (Pro.iAdapterCount > 0) {
                    currentPosition(Pro.iAdapterCount-1)
                } else {
                    val viewP = requireActivity().findViewById<ViewPager2>(R.id.main_view_pager)
                    viewP.currentItem = 0
                    //isEnabled = false
                    //super.setEnabled(false)
                    remove()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressed)

        val toolbar: MaterialToolbar = requireActivity().findViewById(R.id.topAppBar)
        back = toolbar[1]
        back.visibility = View.VISIBLE
        back.setOnClickListener { onBackPressed.handleOnBackPressed() }
        search  = toolbar.menu.findItem(R.id.search)
        search.isVisible = true
        searchView = search.actionView as SearchView
        searchView.isSubmitButtonEnabled = true
        searchView.setOnQueryTextListener(this@InventoryFragment)

        totalStock = binding.totalStock
        totalProduct = binding.totalProduct

        msg = getText(R.string.current)
        stockMsg = getText(R.string.total_stock)
        productMsg = getText(R.string.total_product)

        //parentFragmentManager.beginTransaction().setMaxLifecycle(this, Lifecycle.State.STARTED)

        //back = requireActivity().findViewById(R.id.back)
        //back.setOnClickListener { onBackPressed.handleOnBackPressed() }

        linearLayoutManager = LinearLayoutManager(
            requireActivity().applicationContext, RecyclerView.VERTICAL,false)
        recyclerView = binding.inventoryRecyclerView
        recyclerView.layoutManager = linearLayoutManager

        currentPosition(Pro.iAdapterCount)
    }

    /*override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val back = item.icon
        return true
    }*/

    /*override fun onPause() {
        super.onPause()
        if (Pro.iGetPhoto) {
            parentFragmentManager.beginTransaction().remove(this).commit()
        }
    }*/

    override fun onResume() {
        msg = getText(R.string.current)
        stockMsg = getText(R.string.total_stock)
        productMsg = getText(R.string.total_product)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressed)
        back.visibility = View.VISIBLE
        back.setOnClickListener { onBackPressed.handleOnBackPressed() }
        search.isVisible = true
        searchView.setOnQueryTextListener(this@InventoryFragment)
        currentPosition(Pro.iAdapterCount)
        super.onResume()
    }

    override fun onStop() {
        onBackPressed.remove()
        super.onStop()
    }

    private fun currentPosition(count: Int) {
        recyclerView.recycledViewPool.clear()
        viewLifecycleOwnerLiveData.removeObservers(viewLifecycleOwner)
        when (count) {
            0 -> showCategories()
            1 -> showProducts(Pro.category)
            2 -> showProduct()
            3 -> showYears()
            4 -> showMonths(Pro.iYear)
            5 -> showTransactions(Pro.iMonth)
            //6 -> showUpdates()
        }
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun showCategories() {
        Pro.iAdapterCount = 0
        //recyclerView.recycledViewPool.clear()
        //viewLifecycleOwnerLiveData.removeObservers(viewLifecycleOwner)
        //lifecycleScope
        //totalStock.text = "${inventoryViewModel.allQuantity.value?.get(0) ?: 0}"
        inventoryViewModel.allQuantity.observe(viewLifecycleOwner) { totalList ->
            if (!totalList.isNullOrEmpty()) {
                var total = "$stockMsg: 0"
                if (totalList[0] != null) {
                    totalList[0].let {
                        total = "$stockMsg: $it"
                        totalStock.text = total
                    }
                }
                else totalStock.text = total
            }
        }

        inventoryViewModel.getAllId.observe(viewLifecycleOwner) {
            var total = "$productMsg: 0"
            if (it.isNotEmpty()) {
                total = "$productMsg: ${it.size}"
                totalProduct.text = total
            }
            else totalProduct.text = total
        }

        inventoryViewModel.allCategory.observe(viewLifecycleOwner) { categories ->
            if (categories.isNotEmpty()) {
                val uniqueCategories = categories.toSet().toList()//.sortedDescending()
                val categoryAdapter = CategoryAdapter(uniqueCategories)
                recyclerView.adapter = categoryAdapter
                categoryAdapter.onNameClick = {
                    Pro.category = it
                    showProducts(it)
                }
            }
            else {
                val fragmentName = getText(R.string.menu_inventory)
                val empty = getText(R.string.empty)
                recyclerView.adapter = EmptyAdapter("$fragmentName $empty")
            }
            /*(view?.parent as? ViewGroup)?.doOnPreDraw {
                startPostponedEnterTransition()
            }*/
        }
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun showProducts(category: String) {
        Pro.iAdapterCount = 1
        //recyclerView.recycledViewPool.clear()
        //viewLifecycleOwnerLiveData.removeObservers(viewLifecycleOwner)
        inventoryViewModel.getQuantity(category).observe(viewLifecycleOwner) { totalList ->
            if (!totalList.isNullOrEmpty()) {
                var total = "$stockMsg: 0"
                if (totalList[0] != null) {
                    totalList[0].let {
                        total = "$stockMsg: $it"
                        totalStock.text = total
                    }
                }
                else totalStock.text = total
            }
        }

        inventoryViewModel.getProducts(category).observe(viewLifecycleOwner) { products ->
            var total = "$productMsg: 0"
            if (products.isNotEmpty()) {
                total = "$productMsg: ${products.size}"
                totalProduct.text = total
            }
            else {
                totalProduct.text = total
            }
            val inventoryAdapter = InventoryAdapter(products, msg)
            recyclerView.adapter = inventoryAdapter
            inventoryAdapter.onItemClick = {
                Pro.prodId = it.id
                //transactionDialog(it)
                showProduct()
            }
        }
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun showProduct() {
        Pro.iAdapterCount = 2
        //recyclerView.recycledViewPool.clear()
        //viewLifecycleOwnerLiveData.removeObservers(viewLifecycleOwner)
        inventoryViewModel.getProduct(Pro.prodId).observe(viewLifecycleOwner) { product ->
            if (Pro.nImgPath != "" && Pro.iGetPhoto) {
                //importFile(Pro.nImgPath)
                updatePhoto(product)
            }
            product?.let {
                val productAdapter = ProductAdapter(listOf(it))
                recyclerView.adapter = productAdapter
                productAdapter.onItemClick = { which ->
                    manageProduct(which, product)
                }
            }
        }
    }

    private fun manageProduct(which: String, product: Product) {
        val quantityIn = getText(R.string.quantity_in).toString()
        val quantityOut = getText(R.string.quantity_out).toString()
        val changeCategory = getText(R.string.change_category).toString()
        val addDetails = getText(R.string.add_details).toString()
        val changeName = getText(R.string.change_name).toString()
        val changePrice = getText(R.string.change_price).toString()
        when(which) {
            "del" -> {
                inventoryViewModel.delete(product)// Make dialog before this
                //recyclerView.Recycler().clear()
                recyclerView.recycledViewPool.clear()
                viewLifecycleOwnerLiveData.removeObservers(viewLifecycleOwner)
                showCategories()
            }
            "his" -> showYears()
            "img" -> getPhoto()
            "in" -> showUpdateDialog(quantityIn, 0, product)
            "out" -> showUpdateDialog(quantityOut, 1, product)
            "cat" -> showUpdateDialog(changeCategory, 2, product)
            "det" -> showUpdateDialog(addDetails, 3, product)
            "nam" -> showUpdateDialog(changeName, 4, product)
            "pri" -> showUpdateDialog(changePrice, 5, product)
        }
    }

    private fun showUpdateDialog(transactionName: String, which: Int, product: Product) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_dailog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val textLayout: TextInputLayout = dialog.findViewById(R.id.item_update_label)
        val editText: EditText = dialog.findViewById(R.id.item_update)
        val updateButton: AppCompatButton = dialog.findViewById(R.id.update_button)
        val cancelButton: AppCompatButton = dialog.findViewById(R.id.cancel_button)

        textLayout.hint = transactionName
        if (which == 3) editText.setText(product.userNote)
        cancelButton.setOnClickListener {
            it.hideKeyboard()
            dialog.dismiss()
        }
        updateButton.setOnClickListener {
            if (editText.text.isNotBlank()) {
                val mustNumber = getText(R.string.must_number).toString()
                val lowQuantity = getText(R.string.low_quantity).toString()
                when (which) {
                    0 -> {
                        try {
                            val quantity = editText.text.toString().toInt()
                            inventoryViewModel.updateTotalIn(quantity, product)
                            showProduct()
                        }
                        catch (_: NumberFormatException) {
                            Toast.makeText(requireContext(),
                                mustNumber, Toast.LENGTH_SHORT).show()
                        }
                    }
                    1 -> {
                        try {
                            val quantity = editText.text.toString().toInt()
                            if (quantity > product.quantityInStock) {
                                Toast.makeText(requireContext(),
                                    lowQuantity, Toast.LENGTH_SHORT).show()
                            }
                            else {
                                inventoryViewModel.updateTotalOut(quantity, product)
                                showProduct()
                            }
                        }
                        catch (_: NumberFormatException) {
                            Toast.makeText(requireContext(),
                                mustNumber, Toast.LENGTH_SHORT).show()
                        }
                    }
                    2 -> {
                        inventoryViewModel.updateCategory(editText.text.toString(), product)
                        showProduct()
                    }
                    3 -> {
                        inventoryViewModel.updateUserNote(editText.text.toString(), product)
                        showProduct()
                    }
                    4 -> {
                        inventoryViewModel.updateName(editText.text.toString(), product)
                        showProduct()
                    }
                    5 -> {
                        try {
                            val price = editText.text.toString().toDouble()
                            inventoryViewModel.updatePrice(price, product)
                            showProduct()
                        }
                        catch (_: NumberFormatException) {
                            Toast.makeText(requireContext(),
                                mustNumber, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                it.hideKeyboard()
                showProduct()
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    private fun View.hideKeyboard() {
        val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun showYears() {
        Pro.iAdapterCount = 3
        //recyclerView.recycledViewPool.clear()
        //viewLifecycleOwnerLiveData.removeObservers(viewLifecycleOwner)
        inventoryViewModel.getTransactionYear(Pro.prodId).observe(viewLifecycleOwner) { years ->
            val uniqueYears = years.toSet().toList().sorted()
            val yearAdapter = YearAdapter(uniqueYears)
            recyclerView.adapter = yearAdapter
            yearAdapter.onNameClick = {
                Pro.iYear = it
                showMonths(it)
            }
        }
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun showMonths(year: Int) {
        Pro.iAdapterCount = 4
        //recyclerView.recycledViewPool.clear()
        //viewLifecycleOwnerLiveData.removeObservers(viewLifecycleOwner)
        inventoryViewModel.getTransactionMonth(Pro.prodId, year).observe(viewLifecycleOwner)
        { months ->
            val uniqueMonths = months.toSet().toList().sorted()
            val monthAdapter = MonthAdapter(uniqueMonths)
            recyclerView.adapter = monthAdapter
            monthAdapter.onNameClick = {
                Pro.iMonth = it
                showTransactions(it)
            }
        }
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun showTransactions(month: Int) {
        Pro.iAdapterCount = 5
        //recyclerView.recycledViewPool.clear()
        //viewLifecycleOwnerLiveData.removeObservers(viewLifecycleOwner)
        inventoryViewModel.getTransaction(Pro.prodId, month).observe(viewLifecycleOwner) {
            recyclerView.adapter = TransactionAdapter(it, requireActivity())
        }
    }

    private fun getPhoto() {
        val galIn = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        val camIn = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val productPicture = getText(R.string.product_picture).toString()
        val chooser = Intent.createChooser(galIn, productPicture)
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(camIn))
        Pro.iGetPhoto = true
        startForResult.launch(chooser)
        //parentFragmentManager.beginTransaction().remove(this).commit()
    }

    /*private fun importFile(path: String) {
        val file = activity?.applicationContext?.filesDir
        val newF = File(file, File(path).name)
        if (!newF.exists()) newF.mkdir()
        File(path).copyRecursively(newF, true)
        nImgPath = newF.absolutePath

    }*/

    @SuppressLint("FragmentLiveDataObserve")
    private fun updatePhoto(product: Product) {
        //inventoryViewModel.updatePic(product, Pro.nImgPath)
        inventoryViewModel.updateImage(Pro.nImgPath, product)
        Pro.nImgPath = ""
        Pro.iGetPhoto = false
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
            inventoryViewModel.searchProducts("%$newText%")
                .observe(viewLifecycleOwner) { products ->
                val inventoryAdapter = InventoryAdapter(products, msg)
                recyclerView.adapter = inventoryAdapter
                //back.visibility = View.VISIBLE
                inventoryAdapter.onItemClick = {
                    Pro.prodId = it.id
                    //transactionDialog(it)
                    showProduct()
                }
            }
        }
        return true
    }
}