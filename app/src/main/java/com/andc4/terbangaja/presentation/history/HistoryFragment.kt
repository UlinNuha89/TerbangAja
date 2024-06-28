package com.andc4.terbangaja.presentation.history

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.andc4.terbangaja.R
import com.andc4.terbangaja.data.mapper.toSearchHistoryBookingList
import com.andc4.terbangaja.data.model.BookingHistoryModel
import com.andc4.terbangaja.data.model.SearchHistory
import com.andc4.terbangaja.databinding.FragmentHistoryBinding
import com.andc4.terbangaja.databinding.LayoutSheetDestinationBinding
import com.andc4.terbangaja.presentation.detailhistory.DetailHistoryActivity
import com.andc4.terbangaja.presentation.history.calendar.CalendarBottomSheetFilterFragment
import com.andc4.terbangaja.presentation.history.calendar.CalendarBottomSheetFilterListener
import com.andc4.terbangaja.presentation.history.search.RecentSearchHistoryAdapter
import com.andc4.terbangaja.presentation.history.search.SearchHistoryAdapter
import com.andc4.terbangaja.presentation.login.LoginActivity
import com.andc4.terbangaja.utils.DateUtils.formatIsoDate
import com.andc4.terbangaja.utils.proceedWhen
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class HistoryFragment : Fragment(), CalendarBottomSheetFilterListener {
    private lateinit var binding: FragmentHistoryBinding
    private val viewModel: HistoryViewModel by viewModel()
    val groupAdapter = GroupAdapter<GroupieViewHolder>()
    private var startDate: LocalDate? = null
    private var endDate: LocalDate? = null
//    private val recentSearchAdapter: RecentSearchHistoryAdapter by lazy {
//        RecentSearchHistoryAdapter {
//            viewModel.deleteRecentSearch(it)
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHistoryBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        isLogin()
        setRecyclerView()
        viewModel.getBookingHistories(null, null, null)
        observeData()
        setClick()
    }

    private fun observeData() {
        viewModel.history.observe(viewLifecycleOwner) { it ->
            it.proceedWhen(
                doOnSuccess = {
                    updateRecyclerView(it.payload)
                    binding.contentState.root.isVisible = false
                    binding.contentState.tvError.isVisible = false
                    binding.contentState.pbLoading.isVisible = false
                    binding.contentState.ivError.isVisible = false
                    binding.contentState.btnError.isVisible = false
                    binding.rvHistoryList.isVisible = true
                    binding.llFilter.isVisible = true
                    binding.ivSearch.isEnabled = true
                },
                doOnError = {
                    binding.ivSearch.isEnabled = false
                    binding.contentState.root.isVisible = true
                    binding.contentState.tvError.isVisible = true
                    binding.contentState.pbLoading.isVisible = false
                    binding.contentState.ivError.isVisible = true
                    binding.rvHistoryList.isVisible = false
                    binding.llFilter.isVisible = false
                    binding.contentState.tvError.text = it.exception?.cause?.message
                    when (it.exception?.cause?.message.toString()) {
                        "401" -> {
                            binding.contentState.btnError.isVisible = true
                            binding.contentState.tvError.text = getString(R.string.text_no_login)
                            binding.contentState.ivError.setImageResource(R.drawable.img_nologin)
                            binding.contentState.btnError.text =
                                getString(R.string.text_btn_no_login)
                            binding.contentState.btnError.setOnClickListener {
                                navToLogin()
                            }
                        }
                    }
                },
                doOnLoading = {
                    binding.contentState.pbLoading.isVisible = true
                    binding.contentState.tvError.isVisible = false
                    binding.rvHistoryList.isVisible = false
                    binding.ivSearch.isEnabled = false
                    binding.llFilter.isVisible = false
                },
                doOnEmpty = {
                    binding.ivSearch.isEnabled = true
                    binding.contentState.root.isVisible = true
                    binding.contentState.pbLoading.isVisible = false
                    binding.contentState.tvError.isVisible = true
                    binding.contentState.ivError.isVisible = true
                    binding.contentState.ivError.setImageResource(R.drawable.img_empty)
                    binding.contentState.tvError.text = getString(R.string.text_empty_history)
                    binding.rvHistoryList.isVisible = false
                    binding.llFilter.isVisible = false
                },
            )
        }
    }

    private fun updateRecyclerView(histories: List<BookingHistoryModel>?) {
        groupAdapter.clear()
        if (histories.isNullOrEmpty()) {
            return
        }
        val groupedByMonth = histories.groupBy { formatIsoDate(it.orderDate) }
        groupedByMonth.forEach { (month, bookings) ->
            groupAdapter.add(MonthHeaderItem(month))
            bookings.forEach { booking ->
                groupAdapter.add(
                    HistoryItem(booking) { selectedHistory ->
                        showHistoryDetail(selectedHistory)
                    },
                )
            }
        }
    }

    private fun showHistoryDetail(selectedHistory: BookingHistoryModel) {
        val intent = DetailHistoryActivity.createIntent(requireContext(), selectedHistory)
        startActivity(intent)
    }

    private fun setRecyclerView() {
        binding.rvHistoryList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = groupAdapter
        }
    }

    private fun isLogin() {
        viewModel.isLogin().observe(viewLifecycleOwner) {
            it.proceedWhen(
                doOnLoading = {
                    binding.contentState.pbLoading.isVisible = true
                    binding.contentState.tvError.isVisible = false
                    binding.llFilter.isVisible = false
                    binding.ivSearch.isVisible = false
                },
                doOnSuccess = {
                    binding.contentState.root.isVisible = false
                    binding.contentState.tvError.isVisible = false
                    binding.contentState.pbLoading.isVisible = false
                    binding.contentState.ivError.isVisible = false
                    binding.llFilter.isVisible = true
                    binding.ivSearch.isVisible = true
                    binding.contentState.btnError.isVisible = false
                    binding.rvHistoryList.isVisible = true
                },
                doOnError = {
                    binding.contentState.root.isVisible = true
                    binding.contentState.tvError.isVisible = true
                    binding.contentState.pbLoading.isVisible = false
                    binding.contentState.ivError.isVisible = true
                    binding.ivSearch.isVisible = false
                    binding.rvHistoryList.isVisible = false
                    binding.llFilter.isVisible = false
                    binding.contentState.tvError.text = it.exception?.cause?.message
                    when (it.exception?.cause?.message.toString()) {
                        "401" -> {
                            binding.contentState.btnError.isVisible = true
                            binding.contentState.tvError.text = getString(R.string.text_no_login)
                            binding.contentState.ivError.setImageResource(R.drawable.img_nologin)
                            binding.contentState.btnError.text =
                                getString(R.string.text_btn_no_login)
                            binding.contentState.btnError.setOnClickListener {
                                navToLogin()
                            }
                        }
                    }
                },
                doOnEmpty = {
                    binding.ivSearch.isEnabled = true
                    binding.contentState.root.isVisible = true
                    binding.contentState.pbLoading.isVisible = false
                    binding.contentState.tvError.isVisible = true
                    binding.contentState.ivError.isVisible = true
                    binding.contentState.ivError.setImageResource(R.drawable.img_empty)
                    binding.contentState.tvError.text = getString(R.string.text_empty_history)
                    binding.rvHistoryList.isVisible = false
                    binding.llFilter.isVisible = false
                },
            )
        }
    }

    private fun navToLogin() {
        startActivity(Intent(requireContext(), LoginActivity::class.java))
    }

    private fun setClick() {
        binding.llFilter.setOnClickListener {
            showBottomSheetDialogDate()
        }
        binding.ivSearch.setOnClickListener {
            showBottomSheetSearch()
        }
    }

    private fun showBottomSheetDialogDate() {
        val calendarBottomSheet =
            CalendarBottomSheetFilterFragment()
        val args =
            Bundle().apply {
                putBoolean("isStartDate", true)
                putString(
                    "startDate",
                    startDate?.format(DateTimeFormatter.ofPattern(getString(R.string.format_date))),
                )
                putString(
                    "endDate",
                    endDate?.format(DateTimeFormatter.ofPattern(getString(R.string.format_date))),
                )
            }
        calendarBottomSheet.arguments = args
        calendarBottomSheet.setCalendarBottomSheetListener(this)
        calendarBottomSheet.show(childFragmentManager, "CalendarFilterBottomSheet")
    }

    private fun fetchBookingHistories(
        startDateStr: String?,
        endDateStr: String?,
    ) {
        viewModel.getBookingHistories(startDateStr, endDateStr, null)
    }

    private fun searchHistory(it: SearchHistory) {
        viewModel.getBookingHistories(null, null, it.code)
    }

    private fun showBottomSheetSearch() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val bottomSheetBinding = LayoutSheetDestinationBinding.inflate(layoutInflater)
        val searchAdapter: SearchHistoryAdapter by lazy {
            SearchHistoryAdapter {
                getData(it)
                addDataRecentSearch(it)
                bottomSheetDialog.dismiss()
            }
        }
        val recentSearchHistoryAdapter: RecentSearchHistoryAdapter by lazy {
            RecentSearchHistoryAdapter(
                onDeleteClick = {
                    deleteRecentSearch(it)
                },
                onClick = {
                    searchHistory(it)
                    bottomSheetDialog.dismiss()
                },
            )
        }
        getDataRecentSearch(bottomSheetBinding, recentSearchHistoryAdapter)
        bottomSheetBinding.apply {
            bottomSheetBinding.ivCross.setOnClickListener {
                bottomSheetDialog.dismiss()
            }
            bottomSheetBinding.rvRecentSearch.apply {
                adapter = recentSearchHistoryAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }
            bottomSheetBinding.rvSearch.apply {
                adapter = searchAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }
        }
        getDataRecentSearch(bottomSheetBinding, recentSearchHistoryAdapter)
        val searchView = bottomSheetBinding.searchView
        searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(id: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(id: String?): Boolean {
                    if (id.isNullOrEmpty()) {
                        getDataRecentSearch(bottomSheetBinding, recentSearchHistoryAdapter)
                    } else {
                        getDataHistory(bottomSheetBinding, id, searchAdapter)
                    }
                    return true
                }
            },
        )
        bottomSheetDialog.setContentView(bottomSheetBinding.root)
        bottomSheetDialog.show()
    }

    private fun deleteRecentSearch(item: SearchHistory) {
        viewModel.deleteRecentSearch(item).observe(viewLifecycleOwner) {}
    }

    private fun addDataRecentSearch(item: SearchHistory) {
        viewModel.insertRecentSearch(item).observe(viewLifecycleOwner) {
            it.proceedWhen()
        }
    }

    private fun getDataHistory(
        bottomSheetBinding: LayoutSheetDestinationBinding,
        code: String,
        searchAdapter: SearchHistoryAdapter,
    ) {
        viewModel.getBookingHistories(null, null, code)
        viewModel.history.observe(viewLifecycleOwner) {
            it.proceedWhen(
                doOnSuccess = {
                    bottomSheetBinding.tvTitleSearch.text = getString(R.string.hasil_pencarian)
                    bottomSheetBinding.tvTitleSearch.isVisible = true
                    bottomSheetBinding.rvRecentSearch.isVisible = false
                    bottomSheetBinding.rvSearch.isVisible = true
                    bottomSheetBinding.pbLoading.isVisible = false
                    bottomSheetBinding.tvError.isVisible = false
                    it.payload?.let {
                        searchAdapter.submitData(it.toSearchHistoryBookingList())
                    }
                },
                doOnError = {
                    bottomSheetBinding.rvRecentSearch.isVisible = false
                    bottomSheetBinding.rvSearch.isVisible = false
                    bottomSheetBinding.tvTitleSearch.isVisible = false
                    bottomSheetBinding.tvError.isVisible = true
                    bottomSheetBinding.tvError.text = it.exception?.cause?.message
                },
                doOnEmpty = {
                    it.payload?.let {
                        bottomSheetBinding.rvRecentSearch.isVisible = false
                        bottomSheetBinding.rvSearch.isVisible = false
                        bottomSheetBinding.tvTitleSearch.isVisible = false
                        bottomSheetBinding.tvError.isVisible = true
                        bottomSheetBinding.pbLoading.isVisible = false
                        bottomSheetBinding.tvError.text =
                            getString(R.string.history_yang_anda_cari_tidak_ada)
                    }
                },
                doOnLoading = {
                    bottomSheetBinding.rvRecentSearch.isVisible = false
                    bottomSheetBinding.rvSearch.isVisible = false
                    bottomSheetBinding.tvTitleSearch.isVisible = false
                    bottomSheetBinding.tvError.isVisible = false
                    bottomSheetBinding.pbLoading.isVisible = true
                },
            )
        }
    }

    private fun getDataRecentSearch(
        bottomSheetBinding: LayoutSheetDestinationBinding,
        adapter: RecentSearchHistoryAdapter,
    ) {
        viewModel.getRecentSearch().observe(viewLifecycleOwner) {
            it.proceedWhen(
                doOnSuccess = {
                    bottomSheetBinding.rvRecentSearch.isVisible = true
                    bottomSheetBinding.rvSearch.isVisible = false
                    bottomSheetBinding.tvTitleSearch.text = getString(R.string.histori_pencarian)
                    bottomSheetBinding.tvError.isVisible = false
                    bottomSheetBinding.pbLoading.isVisible = false
                    bottomSheetBinding.tvTitleSearch.isVisible = true
                    it.payload?.let {
                        adapter.submitData(it)
                    }
                },
                doOnError = {
                    bottomSheetBinding.rvRecentSearch.isVisible = false
                    bottomSheetBinding.rvSearch.isVisible = false
                    bottomSheetBinding.tvError.isVisible = true
                    bottomSheetBinding.tvTitleSearch.isVisible = false
                    bottomSheetBinding.pbLoading.isVisible = false
                    bottomSheetBinding.tvError.text = it.exception?.cause?.message
                },
                doOnEmpty = {
                    it.payload?.let {
                        bottomSheetBinding.rvRecentSearch.isVisible = false
                        bottomSheetBinding.rvSearch.isVisible = false
                        bottomSheetBinding.tvTitleSearch.isVisible = false
                        bottomSheetBinding.tvError.isVisible = true
                        bottomSheetBinding.pbLoading.isVisible = false
                        bottomSheetBinding.tvError.text =
                            getString(R.string.silahkan_cari_riwayat_pemesanan)
                    }
                },
                doOnLoading = {
                    bottomSheetBinding.rvRecentSearch.isVisible = false
                    bottomSheetBinding.rvSearch.isVisible = false
                    bottomSheetBinding.tvTitleSearch.isVisible = false
                    bottomSheetBinding.tvError.isVisible = false
                    bottomSheetBinding.pbLoading.isVisible = true
                },
            )
        }
    }

    private fun getData(it: SearchHistory) {
        viewModel.getBookingHistories(null, null, it.code)
    }

    override fun onStartDateSelected(selectedDate: LocalDate) {
        this@HistoryFragment.startDate = selectedDate
        fetchBookingHistories(startDate.toString(), endDate?.toString())
        Log.e("EndDate", "onEndDateSelected: $startDate, $endDate ")
    }

    override fun onEndDateSelected(selectedDate: LocalDate) {
        this@HistoryFragment.endDate = selectedDate
        fetchBookingHistories(startDate.toString(), endDate?.toString())
    }

    override fun onBothDatesSelected(
        startDate: LocalDate,
        endDate: LocalDate,
    ) {
        this@HistoryFragment.startDate = startDate
        this@HistoryFragment.endDate = endDate
        fetchBookingHistories(startDate.toString(), endDate.toString())
    }
}
