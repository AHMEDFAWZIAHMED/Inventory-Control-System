package a7m3d.business.inventorycontrol.ui.history

import a7m3d.business.inventorycontrol.Pro
import a7m3d.business.inventorycontrol.ProductApplication
import a7m3d.business.inventorycontrol.R
import a7m3d.business.inventorycontrol.adapters.*
import a7m3d.business.inventorycontrol.data.Report
import a7m3d.business.inventorycontrol.databinding.FragmentHistoryBinding
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.MaterialToolbar

class HistoryFragment : Fragment(), SearchView.OnQueryTextListener {

    private var _binding: FragmentHistoryBinding? = null

    private val historyViewModel: HistoryViewModel by viewModels {
        HistoryViewModelFactory((activity?.application as ProductApplication).depository,
                                (activity?.application as ProductApplication).repository)
    }
    private lateinit var onBackPressed: OnBackPressedCallback
    private lateinit var recyclerView: RecyclerView
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var totalIn: TextView
    private lateinit var totalOut: TextView
    private lateinit var newReport: TextView// To make new pdf file of week report
    private lateinit var back: View

    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("FragmentBackPressedCallback")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        onBackPressed = object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

                if (Pro.hAdapterCount > 0) {
                    currentPosition(Pro.hAdapterCount-1)
                } else {
                    val viewP = requireActivity().findViewById<ViewPager2>(R.id.main_view_pager)
                    viewP.currentItem = 1
                    remove()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressed)

        val toolbar: MaterialToolbar = requireActivity().findViewById(R.id.topAppBar)
        back = toolbar[1]
        back.setOnClickListener { onBackPressed.handleOnBackPressed() }
        val search  = toolbar.menu.findItem(R.id.search)
        search.isVisible = true
        val searchView = search.actionView as SearchView
        searchView.isSubmitButtonEnabled = true
        searchView.setOnQueryTextListener(this@HistoryFragment)

        totalIn = binding.totalIn
        totalOut = binding.totalOut
        newReport = binding.newReport

        newReport.setOnClickListener { generateReport() }

        val linearLayoutManager = LinearLayoutManager(
            requireActivity().applicationContext, RecyclerView.VERTICAL,false)
        recyclerView = binding.historyRecyclerView
        recyclerView.layoutManager = linearLayoutManager

        historyAdapter = HistoryAdapter()
        historyViewModel.sumAdd.observe(viewLifecycleOwner) {
            var amount = 0
            val name = getText(R.string.stock_total_in)
            try {
                amount = it[0]
            }
            catch (e: Exception) {
                println("******** $e *******")// Do Toast?
            }
            val stockIn = "$name:\n$amount"
            totalIn.text = stockIn
        }
        historyViewModel.sumSubtract.observe(viewLifecycleOwner) {
            var amount = 0
            val name = getText(R.string.stock_total_out)
            try {
                amount = it[0]
            }
            catch (e: Exception) {
                println("******** $e *******")// Do Toast?
            }
            val stockOut = "$name:\n$amount"
            totalOut.text = stockOut
        }

        currentPosition(Pro.hAdapterCount)
    }

    private fun generateReport() {
        historyViewModel.makePdfReport(requireContext().applicationContext)
    }

    override fun onResume() {
        currentPosition(Pro.hAdapterCount)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressed)
        back.visibility  = View.VISIBLE
        back.setOnClickListener { onBackPressed.handleOnBackPressed() }
        when (Pro.hMenu) {
            0 -> newReport.visibility =View.INVISIBLE
            else -> newReport.visibility =View.VISIBLE
        }
        super.onResume()
    }

    override fun onStop() {
        onBackPressed.remove()
        super.onStop()
    }

    private fun currentPosition(count: Int) {
        when (count) {
            0 -> showMenu()
            1  -> {
                when (Pro.hMenu) {
                    0 -> showYears()
                    else -> showReportYears()
                }
            }
            2 -> {
                when (Pro.hMenu) {
                    0 -> showMonths(Pro.hYearT)
                    else -> showReportMonths(Pro.hYearR)
                }
            }
            3 -> {
                when (Pro.hMenu) {
                    0 -> showTransactions(Pro.hMonthT)
                    else -> showReportDays(Pro.hDate()[1], Pro.hDate()[2])
                }
            }
            4 -> showReports(Pro.hDate()[0], Pro.hDate()[1], Pro.hDate()[2])
        }
    }

    private fun showMenu() {
        Pro.hAdapterCount = 0
        newReport.visibility =View.INVISIBLE
        recyclerView.adapter = historyAdapter
        historyAdapter.onNameClick = {
            when (it) {
                "transaction" -> {
                    Pro.hMenu = 0
                    newReport.visibility =View.INVISIBLE
                    showYears()
                }
                "report" -> {
                    Pro.hMenu = 1
                    newReport.visibility =View.VISIBLE
                    showReportYears()
                }
            }
        }
    }

    private fun showYears() {
        Pro.hAdapterCount = 1
        historyViewModel.getYear.observe(viewLifecycleOwner) { years ->
            if (years.isNotEmpty()) {
                val uniqueYears = years.toSet().toList().sorted()
                val yearAdapter = YearAdapter(uniqueYears)
                recyclerView.adapter = yearAdapter
                back.visibility = View.VISIBLE
                yearAdapter.onNameClick = {
                    Pro.hYearT = it
                    showMonths(it)
                }
            }
            else {
                val name = getText(R.string.transactions)
                val empty = getText(R.string.empty)
                recyclerView.adapter = EmptyAdapter("$name $empty")
            }
        }
    }

    private fun showMonths(year: Int) {
        Pro.hAdapterCount = 2
        historyViewModel.getMonth(year).observe(viewLifecycleOwner) { months ->
            val uniqueMonths = months.toSet().toList().sorted()
            val monthAdapter = MonthAdapter(uniqueMonths)
            recyclerView.adapter = monthAdapter
            back.visibility = View.VISIBLE
            monthAdapter.onNameClick = {
                Pro.hMonthT = it
                showTransactions(it)
            }
        }
    }

    private fun showTransactions(month: Int) {
        Pro.hAdapterCount = 3
        historyViewModel.getTransactions(month).observe(viewLifecycleOwner) {
            recyclerView.adapter = TransactionAdapter(it, requireActivity())
        }
    }

    private fun showReportYears() {
        Pro.hAdapterCount = 1
        historyViewModel.reportYears.observe(viewLifecycleOwner) { years ->
            if (years.isNotEmpty()) {
                val uniqueYears = years.toSet().toList().sorted()
                val yearAdapter = YearAdapter(uniqueYears)
                recyclerView.adapter = yearAdapter
                back.visibility = View.VISIBLE
                yearAdapter.onNameClick = {
                    Pro.hYearR = it
                    showReportMonths(it)
                }
            }
            else {
                val name = getText(R.string.reports)
                val empty = getText(R.string.empty)
                recyclerView.adapter = EmptyAdapter("$name $empty")
            }
        }
    }

    private fun showReportMonths(year: Int) {
        Pro.hAdapterCount = 2
        historyViewModel.getReportMonths(year).observe(viewLifecycleOwner) { months ->
            val uniqueMonths = months.toSet().toList().sorted()
            val monthAdapter = MonthAdapter(uniqueMonths)
            recyclerView.adapter = monthAdapter
            back.visibility = View.VISIBLE
            monthAdapter.onNameClick = {
                Pro.hMonthR = it
                showReportDays(it, Pro.hYearR)
            }
        }
    }

    private fun showReportDays(month: Int, year: Int) {
        Pro.hAdapterCount = 3
        historyViewModel.getReportDays(month, year).observe(viewLifecycleOwner) { days ->
            val uniqueDays = days.toSet().toList().sorted()
            val dayAdapter = DayAdapter(uniqueDays)
            recyclerView.adapter = dayAdapter
            back.visibility = View.VISIBLE
            dayAdapter.onNameClick = {
                Pro.hDayR = it
                showReports(it, month, Pro.hDate()[2])
            }
        }
    }

    private fun showReports(day: Int, month: Int, year: Int) {
        Pro.hAdapterCount = 4
        historyViewModel.report(day, month, year).observe(viewLifecycleOwner) {
            val pages = Pro.numberOfPages(it.size, 5)
            val listOfReportsLists = mutableListOf<List<Report>>()
            for (page in 1..pages) {
                var reportSequence = sequenceOf<Report>()
                for (indx in ((page-1)*5) until Pro.lastIndex(page, pages, it.size,
                                                                    5)) {
                    reportSequence += it[indx]
                }
                listOfReportsLists.add(page-1, reportSequence.toList())
            }
            recyclerView.adapter = ReportAdapter(listOfReportsLists)
        }
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
            historyViewModel.searchTransactions("%$newText%")
                .observe(viewLifecycleOwner) {
                    recyclerView.adapter = TransactionAdapter(it, requireActivity())
                }
        }
        return true
    }
}
