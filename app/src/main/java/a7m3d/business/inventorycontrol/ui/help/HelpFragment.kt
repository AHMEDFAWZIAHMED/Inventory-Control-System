package a7m3d.business.inventorycontrol.ui.help

import a7m3d.business.inventorycontrol.Pro
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import a7m3d.business.inventorycontrol.R
import a7m3d.business.inventorycontrol.adapters.HelpAdapter
import a7m3d.business.inventorycontrol.adapters.HelpDetailsAdapter
import a7m3d.business.inventorycontrol.databinding.FragmentHelpBinding
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.MaterialToolbar

class HelpFragment : Fragment() {

    private var _binding: FragmentHelpBinding? = null

    private lateinit var onBackPressed: OnBackPressedCallback
    private lateinit var recyclerView: RecyclerView
    private lateinit var back: View
    private lateinit var search: MenuItem
    private lateinit var frontLines: List<String>
    private lateinit var briefs: List<String>

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHelpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Handler(Looper.getMainLooper()).post {
            onBackPressed = object: OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (Pro.heAdapterCount == 1) showFrontLines()
                    else {
                        val viewP = requireActivity().findViewById<ViewPager2>(R.id.main_view_pager)
                        viewP.currentItem = 2
                        //isEnabled = false
                        //super.setEnabled(false)
                        remove()
                    }
                }
            }
            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressed)

            val toolbar: MaterialToolbar = requireActivity().findViewById(R.id.topAppBar)
            back = toolbar[1]
            back.visibility  = View.VISIBLE
            back.setOnClickListener { onBackPressed.handleOnBackPressed() }
            search  = toolbar.menu.findItem(R.id.search)
            search.isVisible = false
        }

        val linearLayoutManager = LinearLayoutManager(
            requireActivity().applicationContext, RecyclerView.VERTICAL,false)
        //linearLayoutManager.stackFromEnd = true
        recyclerView =binding.helpRecyclerView
        recyclerView.layoutManager = linearLayoutManager
        //val help = getString(R.string.menu_help)
        //recyclerView.adapter = EmptyAdapter(help)

        showFrontLines()

        /*Handler(Looper.getMainLooper()).postDelayed({
            Handler(Looper.getMainLooper()).post { showFrontLines() }
        }, 1000)*/
    }

    private fun showFrontLines() {
        Pro.heAdapterCount = 0
        frontLines = resources.getStringArray(R.array.front_lines).toList()
        briefs = resources.getStringArray(R.array.briefs).toList()
        val helpAdapter = HelpAdapter(frontLines, briefs)
        recyclerView.adapter = helpAdapter
        helpAdapter.onNameClick = {
            showDetails(it)
        }
    }

    private fun showDetails(heading: String) {
        Pro.heAdapterCount = 1
        recyclerView.adapter = HelpDetailsAdapter(heading, requireActivity())
    }

    override fun onResume() {
        //frontLines = resources.getStringArray(R.array.front_lines).toList()
        //briefs = resources.getStringArray(R.array.briefs).toList()
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressed)
        back.visibility  = View.VISIBLE
        back.setOnClickListener { onBackPressed.handleOnBackPressed() }
        search.isVisible = false
        super.onResume()
    }

    override fun onStop() {
        onBackPressed.remove()
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}