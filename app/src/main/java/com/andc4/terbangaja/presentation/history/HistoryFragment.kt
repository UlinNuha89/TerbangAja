package com.andc4.terbangaja.presentation.history

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.andc4.terbangaja.utils.proceedWhen
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private val viewModel: HistoryViewModel by viewModel()
    val groupAdapter = GroupAdapter<GroupieViewHolder>()
    private var startDate: LocalDate? = null
    private var endDate: LocalDate? = null
    private val recentSearchAdapter: RecentSearchHistoryAdapter by lazy {
        RecentSearchHistoryAdapter {
            viewModel.deleteRecentSearch(it)
        }
    }

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
        viewModel.history.observe(viewLifecycleOwner) {
            it.proceedWhen(
                doOnSuccess = {
                    updateRecyclerView(it.payload)
                    Toast.makeText(requireContext(), "Sukses", Toast.LENGTH_SHORT).show()
                },
                doOnError = {
                    Log.e("ErrorHistory", it.exception.toString())
                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                },
                doOnLoading = {
                    Toast.makeText(requireContext(), "loading fetch", Toast.LENGTH_SHORT).show()
                },
                doOnEmpty = {
                    binding.contentState.root.isVisible = true
                    binding.contentState.tvError.isVisible = true
                    binding.contentState.tvError.text = "Belum ada riwayat pesanan"
                    binding.rvHistoryList.isVisible = false
                    Toast.makeText(requireContext(), "Kosong", Toast.LENGTH_SHORT).show()
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
        if (viewModel.isLogin()) {
            binding.contentState.root.isVisible = false
            binding.contentState.tvError.isVisible = false
            binding.contentState.ivError.isVisible = false
            binding.contentState.btnError.isVisible = false
            binding.rvHistoryList.isVisible = true
        } else {
            binding.contentState.root.isVisible = true
            binding.contentState.tvError.isVisible = true
            binding.contentState.ivError.isVisible = true
            binding.contentState.btnError.isVisible = true
            binding.rvHistoryList.isVisible = false
            binding.llFilter.isVisible = false
            binding.contentState.tvError.text = getString(R.string.text_no_login)
            binding.contentState.ivError.setImageResource(R.drawable.img_nologin)
            binding.contentState.btnError.text = getString(R.string.text_btn_no_login)
            binding.contentState.btnError.setOnClickListener {
                navToLogin()
            }
        viewModel.isLogin().observe(viewLifecycleOwner) {
            it.proceedWhen(
                doOnLoading = {
                    binding.contentState.pbLoading.isVisible = true
                    binding.contentState.tvError.isVisible = false
                },
                doOnSuccess = {
                    binding.contentState.root.isVisible = false
                    binding.contentState.tvError.isVisible = false
                    binding.contentState.pbLoading.isVisible = false
                    binding.contentState.ivError.isVisible = false
                    binding.contentState.btnError.isVisible = false
                    binding.rvHistoryList.isVisible = true
                },
                doOnError = {
                    binding.contentState.root.isVisible = true
                    binding.contentState.tvError.isVisible = true
                    binding.contentState.pbLoading.isVisible = false
                    binding.contentState.ivError.isVisible = true
                    binding.rvHistoryList.isVisible = false
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
            )
        }
    }

    private fun navToLogin() {
        startActivity(Intent(requireContext(), LoginActivity::class.java))
    }

    private fun setClick() {
        binding.llFilter.setOnClickListener {
            Toast.makeText(requireContext(), "anjay", Toast.LENGTH_SHORT).show()
            showBottomSheetDialogDate()
        }
        binding.ivSearch.setOnClickListener {
            showBottomSheetSearch()
        }
    }

    private fun showBottomSheetDialogDate() {
        val calendarBottomSheet =
            CalendarBottomSheetFilterFragment().apply {
                arguments =
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
                setCalendarBottomSheetListener(
                    object : CalendarBottomSheetFilterListener {
                        override fun onStartDateSelected(selectedDate: LocalDate) {
                            startDate = selectedDate
                            fetchBookingHistories()
                        }

                        override fun onEndDateSelected(selectedDate: LocalDate) {
                            endDate = selectedDate
                            fetchBookingHistories()
                        }

                        override fun onBothDatesSelected(
                            startDate: LocalDate,
                            endDate: LocalDate,
                        ) {
                            this@HistoryFragment.startDate = startDate
                            this@HistoryFragment.endDate = endDate
                            fetchBookingHistories()
                        }
                    },
                )
            }
        calendarBottomSheet.show(childFragmentManager, "CalendarBottomSheet")
    }

    private fun fetchBookingHistories() {
        val startDateStr = startDate?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        val endDateStr = endDate?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        Log.d("tryTanggal", "fetchBookingHistories: $startDateStr sama $endDateStr")
        viewModel.getBookingHistories(startDateStr, endDateStr, null)
    }

    private fun showBottomSheetSearch() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val bottomSheetBinding = LayoutSheetDestinationBinding.inflate(layoutInflater)
        val searchAdapter: SearchHistoryAdapter by lazy {
            SearchHistoryAdapter {
                getData(it)
            }
        }
        bottomSheetBinding.apply {
            bottomSheetBinding.ivCross.setOnClickListener {
                bottomSheetDialog.dismiss()
            }
            bottomSheetBinding.rvRecentSearch.apply {
                adapter = recentSearchAdapter
            }
        }
        getDataRecentSearchHistory(bottomSheetBinding)
        val searchView = bottomSheetBinding.searchView
        searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(id: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(id: String?): Boolean {
                    if (id.isNullOrEmpty()) {
                        getDataRecentSearch(bottomSheetBinding)
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

    private fun getDataHistory(
        bottomSheetBinding: LayoutSheetDestinationBinding,
        code: String,
        searchAdapter: SearchHistoryAdapter,
    ) {
        viewModel.getBookingHistories(null, null, code)
        viewModel.history.observe(viewLifecycleOwner) {
            it.proceedWhen(
                doOnSuccess = {
                    bottomSheetBinding.clearRecentSearches.isVisible = false
                    bottomSheetBinding.tvTitleSearch.text = "Hasil Pencarian"
                    bottomSheetBinding.rvRecentSearch.isVisible = false
                    bottomSheetBinding.rvSearch.isVisible = true
                    bottomSheetBinding.tvError.isVisible = false
                    it.payload?.let {
                        searchAdapter.submitData(it.toSearchHistoryBookingList())
                    }
                },
                doOnError = {
                    bottomSheetBinding.clearRecentSearches.isVisible = false
                    bottomSheetBinding.rvRecentSearch.isVisible = false
                    bottomSheetBinding.rvSearch.isVisible = false
                    bottomSheetBinding.tvError.isVisible = true
                    bottomSheetBinding.tvError.text = it.exception?.cause?.message
                },
                doOnEmpty = {
                    bottomSheetBinding.clearRecentSearches.isVisible = false
                    bottomSheetBinding.rvRecentSearch.isVisible = false
                    bottomSheetBinding.rvSearch.isVisible = false
                    bottomSheetBinding.tvError.isVisible = true
                    bottomSheetBinding.tvError.text = it.exception?.cause?.message
                },
            )
        }
    }

    private fun getDataRecentSearch(bottomSheetBinding: LayoutSheetDestinationBinding) {
        viewModel.getRecentSearch().observe(viewLifecycleOwner) {
            it.proceedWhen(
                doOnSuccess = {
                    bottomSheetBinding.clearRecentSearches.isVisible = true
                    bottomSheetBinding.rvRecentSearch.isVisible = true
                    bottomSheetBinding.rvSearch.isVisible = false
                    bottomSheetBinding.tvTitleSearch.text = "Histori Pencarian"
                    bottomSheetBinding.tvError.isVisible = false
                    it.payload?.let {
                        recentSearchAdapter.submitData(it)
                    }
                },
                doOnError = {
                    bottomSheetBinding.clearRecentSearches.isVisible = false
                    bottomSheetBinding.rvRecentSearch.isVisible = false
                    bottomSheetBinding.rvSearch.isVisible = false
                    bottomSheetBinding.tvError.isVisible = true
                    bottomSheetBinding.tvError.text = it.exception?.cause?.message
                },
                doOnEmpty = {
                    bottomSheetBinding.clearRecentSearches.isVisible = false
                    bottomSheetBinding.rvRecentSearch.isVisible = false
                    bottomSheetBinding.rvSearch.isVisible = false
                    bottomSheetBinding.tvError.isVisible = true
                    bottomSheetBinding.tvError.text = it.exception?.cause?.message
                },
            )
        }
    }

    private fun getData(it: SearchHistory) {
    }

    private fun getDataRecentSearchHistory(bottomSheetBinding: LayoutSheetDestinationBinding) {
        viewModel.getRecentSearch().observe(viewLifecycleOwner) { it ->
            it.proceedWhen(
                doOnSuccess = { result ->
                    bottomSheetBinding.clearRecentSearches.isVisible = true
                    bottomSheetBinding.rvRecentSearch.isVisible = true
                    bottomSheetBinding.rvSearch.isVisible = false
                    bottomSheetBinding.tvTitleSearch.text = "Histori Pencarian"
                    bottomSheetBinding.tvError.isVisible = false
                    result.payload?.let {
                        recentSearchAdapter.submitData(it)
                    }
                },
                doOnError = {
                    bottomSheetBinding.clearRecentSearches.isVisible = false
                    bottomSheetBinding.rvRecentSearch.isVisible = false
                    bottomSheetBinding.rvSearch.isVisible = false
                    bottomSheetBinding.tvError.isVisible = true
                    bottomSheetBinding.tvError.text = it.exception?.cause?.message
                },
                doOnEmpty = {
                    bottomSheetBinding.clearRecentSearches.isVisible = false
                    bottomSheetBinding.rvRecentSearch.isVisible = false
                    bottomSheetBinding.rvSearch.isVisible = false
                    bottomSheetBinding.tvError.isVisible = true
                    bottomSheetBinding.tvError.text = it.exception?.cause?.message
                },
            )
        }
    }
}
