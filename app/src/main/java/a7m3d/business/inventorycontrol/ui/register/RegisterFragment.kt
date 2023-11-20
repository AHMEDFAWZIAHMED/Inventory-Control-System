package a7m3d.business.inventorycontrol.ui.register

import a7m3d.business.inventorycontrol.Pro
import a7m3d.business.inventorycontrol.ProductApplication
import a7m3d.business.inventorycontrol.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import a7m3d.business.inventorycontrol.databinding.FragmentRegisterBinding
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.*
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.core.view.get
import androidx.fragment.app.viewModels
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputEditText

class RegisterFragment(private val startForResult: ActivityResultLauncher<Intent>) : Fragment() {

    private var _binding: FragmentRegisterBinding? = null

    private val registerViewModel: RegisterViewModel by viewModels {
        RegisterViewModelFactory((activity?.application as ProductApplication).repository)
    }
    private lateinit var picture: ShapeableImageView
    private lateinit var imgTxt: TextView
    private lateinit var itemName: TextInputEditText
    private lateinit var itemCount: TextInputEditText
    private lateinit var itemPrice: TextInputEditText
    private lateinit var itemCategory: TextInputEditText
    private lateinit var itemDetails: TextInputEditText
    private lateinit var saveAction: Button
    private lateinit var search: MenuItem
    private lateinit var back: View
    private lateinit var category: String
    private lateinit var details: String
    private var quantity = 0
    private var price = 0.0


    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("FragmentBackPressedCallback", "Range")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner, object: OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val pressAgain = getText(R.string.press_again).toString()
                    Toast.makeText(requireContext(),
                        pressAgain, Toast.LENGTH_SHORT).show()
                    remove()
                }
            })

        val toolbar: MaterialToolbar = requireActivity().findViewById(R.id.topAppBar)
        back = toolbar[1]
        back.visibility = View.INVISIBLE
        search  = toolbar.menu.findItem(R.id.search)
        search.isVisible = false

        picture = binding.picture
        imgTxt = binding.imgTxt
        if (Pro.nImgPath != "" && Pro.rGetPhoto) {
            try {
                val bitmapFactory = BitmapFactory.decodeFile(Pro.nImgPath)
                if (bitmapFactory != null) {
                    picture.setImageBitmap(bitmapFactory)
                    imgTxt.visibility = View.INVISIBLE
                    Pro.rGetPhoto = false
                }
            }
            catch (_: OutOfMemoryError) {
                Log.d("RegisterFragment", "******** Out of memory")
            }
        }
        picture.setOnClickListener { getPhoto() }
        itemName = binding.itemName
        itemCount = binding.itemCount
        itemCategory = binding.itemCategory
        itemDetails = binding.itemDetails
        itemPrice = binding.itemPrice
        saveAction = binding.saveAction
        saveAction.setOnClickListener { saveProduct() }

        category = getText(R.string.general_category).toString()
        details = getText(R.string.no_details).toString()
    }

    override fun onResume() {
        back.visibility = View.INVISIBLE
        search.isVisible = false
        super.onResume()
    }

    private fun saveProduct() {
        val productName = getText(R.string.product_name)
        val productQuantity = getText(R.string.product_quantity)
        val empty = getText(R.string.empty)
        val empty2 = getText(R.string.empty2)

        if (itemName.text?.isBlank() == true) {
            Toast.makeText(requireContext(), "$productName $empty!", Toast.LENGTH_SHORT)
                .show()
            return
        }
        if (itemCount.text?.isBlank() == true) {
            Toast.makeText(requireContext(), "$productQuantity $empty2!", Toast.LENGTH_SHORT)
                .show()
            return
        }
        try {
            quantity = itemCount.text.toString().toInt()
        }
        catch (_: NumberFormatException) {
            Toast.makeText(requireContext(), "Something went wrong!", Toast.LENGTH_SHORT)
                .show()
        }

        if (quantity == 0) {
            Toast.makeText(requireContext(), "Something went wrong!", Toast.LENGTH_SHORT)
                .show()
            return
        }

        if (itemCategory.text?.isBlank() == false) category = itemCategory.text.toString()
        if (itemDetails.text?.isBlank() == false) details = itemDetails.text.toString()
        if (itemPrice.text?.isBlank() == false) {
            try {
                price = itemPrice.text.toString().toDouble()
            }
            catch (_: NumberFormatException) {
                Toast.makeText(requireContext(), "Something went wrong!", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        val normal = getText(R.string.normal).toString()
        registerViewModel.insert(itemName.text.toString(),
            quantity, category, Pro.nImgPath, details, price, normal)

        itemName.text?.clear()
        itemCount.text?.clear()
        itemCategory.text?.clear()
        itemDetails.text?.clear()
        itemPrice.text?.clear()
        picture.setImageResource(R.drawable.ic_empty_photo)
        imgTxt.visibility = View.VISIBLE
        Pro.nImgPath = ""
        category = getText(R.string.general_category).toString()
        details = getText(R.string.no_details).toString()
        quantity = 0
        price = 0.0
        saveAction.hideKeyboard()
    }

    private fun getPhoto() {
        val productPicture = getText(R.string.product_picture2).toString()
        val galIn = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        val camIn = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val chooser = Intent.createChooser(galIn, productPicture)
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(camIn))
        Pro.rGetPhoto = true
        startForResult.launch(chooser)
    }

    private fun View.hideKeyboard() {
        val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}