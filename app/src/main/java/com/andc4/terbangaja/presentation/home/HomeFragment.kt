package com.andc4.terbangaja.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.andc4.terbangaja.R
import com.andc4.terbangaja.data.model.Airport
import com.andc4.terbangaja.data.model.Flight
import com.andc4.terbangaja.data.model.Passenger
import com.andc4.terbangaja.data.model.SeatClass
import com.andc4.terbangaja.data.model.Ticket
import com.andc4.terbangaja.databinding.FragmentHomeBinding
import com.andc4.terbangaja.databinding.LayoutSheetDestinationBinding
import com.andc4.terbangaja.databinding.LayoutSheetFlightTypeBinding
import com.andc4.terbangaja.databinding.LayoutSheetPassengerCountBinding
import com.andc4.terbangaja.presentation.detailfavourite.DetailFavouriteActivity
import com.andc4.terbangaja.presentation.home.adapter.FavouriteDestinationAdapter
import com.andc4.terbangaja.presentation.home.adapter.OptionClassAdapter
import com.andc4.terbangaja.presentation.home.adapter.RecentSearchAdapter
import com.andc4.terbangaja.presentation.home.adapter.SearchAdapter
import com.andc4.terbangaja.presentation.home.calendar.CalendarBottomSheetFragment
import com.andc4.terbangaja.presentation.home.calendar.CalendarBottomSheetListener
import com.andc4.terbangaja.presentation.ticketorder.TicketOrderActivity
import com.andc4.terbangaja.utils.proceedWhen
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class HomeFragment : Fragment(), CalendarBottomSheetListener {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModel()

    /*
    private val recentSearchAdapter: RecentSearchAdapter by lazy {
        RecentSearchAdapter {
            viewModel.deleteRecentSearch(it)
        }
    }*/
    private var dataDestinationFrom: Airport? = null
    private var dataDestinationTo: Airport? = null

    private var dataPassenger: Passenger? = null
    private var dataClass: SeatClass? = null

    private var dataDateDeparture: LocalDate? = LocalDate.now()
    private var dataDateReturn: LocalDate? = LocalDate.now()
    private var isReturn: Boolean = false

    private fun insertData(
        id: Int,
        airport: Airport,
    ) {
        when (id) {
            1 -> {
                binding.layoutHeader.layoutDestination.tvDestinationFrom.text = airport.city
                dataDestinationFrom = airport
            }

            2 -> {
                binding.layoutHeader.layoutDestination.tvDestinationTo.text = airport.city
                dataDestinationTo = airport
            }
        }
    }

    private val favouriteDestinationAdapter: FavouriteDestinationAdapter by lazy {
        FavouriteDestinationAdapter {
            DetailFavouriteActivity.startActivity(requireContext(), it)
        }
    }

    private fun bindFavouriteDestination(flight: List<Flight>) {
        favouriteDestinationAdapter.submitData(flight)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setUpData()
        setUpHeader()
        setDateReturn(false)
        getDataFlight()
        setUpDataFLight()
        setOnClick()
    }

    private fun setUpData() {
        dataPassenger = viewModel.initialPassenger()
        dataClass = viewModel.initialOption()
    }

    private fun setUpHeader() {
        binding.layoutHeader.tvDepartureDate.text =
            dataDateDeparture?.format(
                DateTimeFormatter.ofPattern(
                    getString(
                        R.string.format_date,
                    ),
                ),
            )
        binding.layoutHeader.tvPassengersCount.text = getPassenger()
        binding.layoutHeader.tvSeatClassType.text = dataClass?.name
    }

    private fun getPassenger(): String {
        return getString(
            R.string.text_penumpang,
            (dataPassenger!!.children + dataPassenger!!.adult).toString(),
        )
    }

    private fun setUpDataFLight() {
        binding.rvDestinationList.apply {
            adapter = favouriteDestinationAdapter
        }
    }

    private fun setOnClick() {
        binding.layoutHeader.layoutDestination.tvDestinationFrom.setOnClickListener {
            showBottomSheetDestinationFrom()
        }
        binding.layoutHeader.layoutDestination.tvDestinationTo.setOnClickListener {
            showBottomSheetDestinationTo()
        }
        binding.layoutHeader.tvPassengersCount.setOnClickListener {
            showBottomSheetPassengerCount()
        }
        binding.layoutHeader.tvSeatClassType.setOnClickListener {
            showBottomSheetPassengerClass()
        }
        binding.layoutHeader.btnSearch.setOnClickListener {
            validateData()
        }
        binding.layoutHeader.layoutDestination.icSwitch.setOnClickListener {
            switchDestination()
        }
        binding.layoutHeader.tvDepartureDate.setOnClickListener {
            showBottomSheetCalendarDeparture()
        }
        binding.layoutHeader.swRoundTrip.setOnCheckedChangeListener { _, checkedId ->
            setDateReturn(checkedId)
        }
    }

    private fun validateData() {
        if (dataDestinationFrom != null && dataDestinationTo != null) {
            navToTicketOrder()
        } else if (dataDestinationFrom != null && dataDestinationTo == null) {
            Toast.makeText(
                requireContext(),
                getString(R.string.harap_isi_destinasi_tujuan_anda),
                Toast.LENGTH_SHORT,
            ).show()
        } else if (dataDestinationFrom == null && dataDestinationTo != null) {
            Toast.makeText(
                requireContext(),
                getString(R.string.harap_isi_lokasi_anda),
                Toast.LENGTH_SHORT,
            ).show()
        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.harap_isi_lokasi_dan_tujuan_anda),
                Toast.LENGTH_SHORT,
            )
                .show()
        }
    }

    private fun navToTicketOrder() {
        if (isReturn) {
            TicketOrderActivity.startActivity(
                requireContext(),
                Ticket(
                    dataDestinationFrom!!,
                    dataDestinationTo!!,
                    dataDateDeparture!!,
                    dataDateReturn,
                    dataPassenger!!,
                    dataClass!!,
                ),
                true,
            )
        } else {
            TicketOrderActivity.startActivity(
                requireContext(),
                Ticket(
                    dataDestinationFrom!!,
                    dataDestinationTo!!,
                    dataDateDeparture!!,
                    null,
                    dataPassenger!!,
                    dataClass!!,
                ),
                false,
            )
        }
    }

    private fun setDateReturn(checkedId: Boolean) {
        if (checkedId) {
            isReturn = true
            binding.layoutHeader.tvReturnDate.text =
                dataDateReturn?.format(
                    DateTimeFormatter.ofPattern(
                        getString(
                            R.string.format_date,
                        ),
                    ),
                )
            binding.layoutHeader.tvReturnDate.setOnClickListener {
                showBottomSheetCalendarReturn()
            }
        } else {
            isReturn = false
            binding.layoutHeader.tvReturnDate.text = ""
        }
    }

    private fun switchDestination() {
        if (dataDestinationFrom != null && dataDestinationTo != null) {
            val data = dataDestinationTo
            dataDestinationTo = dataDestinationFrom
            binding.layoutHeader.layoutDestination.tvDestinationTo.text = dataDestinationFrom?.city
            dataDestinationFrom = data
            binding.layoutHeader.layoutDestination.tvDestinationFrom.text =
                dataDestinationFrom?.city
        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.isi_data_terlebih_dahulu),
                Toast.LENGTH_SHORT,
            ).show()
        }
    }

    private fun getDataFlight() {
        viewModel.getFLight().observe(viewLifecycleOwner) {
            it.proceedWhen(
                doOnLoading = {
                    binding.contentState.root.isVisible = false
                    binding.contentState.ivEmptyTicket.isVisible = false
                    binding.contentState.tvEmptyTicket.isVisible = false
                    binding.contentState.tvEmptyTicketSub.isVisible = false
                    binding.rvDestinationList.isVisible = false
                    binding.favouriteDestinationShimmer.isVisible = true
                    binding.shimmerFrameLayoutFavouriteDestination.startShimmer()
                },
                doOnEmpty = {
                    if (it.payload?.isEmpty() == true) {
                        binding.contentState.root.isVisible = true
                        binding.contentState.ivEmptyTicket.isVisible = true
                        binding.contentState.tvEmptyTicket.isVisible = true
                        binding.contentState.tvEmptyTicketSub.isVisible = false
                        binding.contentState.tvEmptyTicket.text = it.exception?.cause?.message
                        binding.contentState.ivEmptyTicket.setImageResource(R.drawable.img_empty)
                        binding.rvDestinationList.isVisible = false
                        binding.favouriteDestinationShimmer.isVisible = false
                    }
                },
                doOnSuccess = {
                    binding.contentState.root.isVisible = false
                    binding.contentState.ivEmptyTicket.isVisible = false
                    binding.contentState.tvEmptyTicket.isVisible = false
                    binding.contentState.tvEmptyTicketSub.isVisible = false
                    binding.rvDestinationList.isVisible = true
                    binding.shimmerFrameLayoutFavouriteDestination.isVisible = false
                    it.payload?.let {
                        bindFavouriteDestination(it)
                    }
                },
            )
        }
    }

    fun getDataAirport(
        bottomSheetBinding: LayoutSheetDestinationBinding,
        data: String?,
        adapter: SearchAdapter,
    ) {
        viewModel.getAirport(data).observe(viewLifecycleOwner) {
            it.proceedWhen(
                doOnSuccess = {
                    bottomSheetBinding.tvTitleSearch.isVisible = true
                    bottomSheetBinding.tvTitleSearch.text = getString(R.string.hasil_pencarian)
                    bottomSheetBinding.rvRecentSearch.isVisible = false
                    bottomSheetBinding.rvSearch.isVisible = true
                    bottomSheetBinding.pbLoading.isVisible = false
                    bottomSheetBinding.tvError.isVisible = false
                    it.payload?.let {
                        adapter.submitData(it)
                    }
                },
                doOnError = {
                    bottomSheetBinding.rvRecentSearch.isVisible = false
                    bottomSheetBinding.tvTitleSearch.isVisible = false
                    bottomSheetBinding.rvSearch.isVisible = false
                    bottomSheetBinding.pbLoading.isVisible = false
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
                        bottomSheetBinding.tvError.text = getString(R.string.lokasi_yang_anda_cari_tidak_ada)
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

    fun getDataRecentSearchAirport(
        bottomSheetBinding: LayoutSheetDestinationBinding,
        adapter: RecentSearchAdapter,
    ) {
        viewModel.getRecentSearch().observe(viewLifecycleOwner) {
            it.proceedWhen(
                doOnSuccess = {
                    bottomSheetBinding.rvRecentSearch.isVisible = true
                    bottomSheetBinding.rvSearch.isVisible = false
                    bottomSheetBinding.pbLoading.isVisible = false
                    bottomSheetBinding.tvTitleSearch.isVisible = true
                    bottomSheetBinding.tvTitleSearch.text = getString(R.string.histori_pencarian)
                    bottomSheetBinding.tvError.isVisible = false
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
                            getString(R.string.silahkan_cari_lokasi_penerbangan)
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

    private fun showBottomSheetDestinationFrom() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val bottomSheetBinding = LayoutSheetDestinationBinding.inflate(layoutInflater)
        val searchAdapterFrom: SearchAdapter by lazy {
            SearchAdapter {
                insertData(1, it)
                addDataRecentSearch(it)
                bottomSheetDialog.dismiss()
            }
        }
        val recentSearchAdapter: RecentSearchAdapter by lazy {
            RecentSearchAdapter(
                onDeleteClick = {
                    deleteRecentSearch(it)
                },
                onClick = {
                    insertData(1, it)
                    bottomSheetDialog.dismiss()
                },
            )
        }
        bottomSheetBinding.apply {
            bottomSheetBinding.ivCross.setOnClickListener {
                bottomSheetDialog.dismiss()
            }
            bottomSheetBinding.rvRecentSearch.apply {
                adapter = recentSearchAdapter
            }
            bottomSheetBinding.rvSearch.apply {
                adapter = searchAdapterFrom
            }
        }
        getDataRecentSearchAirport(bottomSheetBinding, recentSearchAdapter)
        val searchView = bottomSheetBinding.searchView
        searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(id: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(id: String?): Boolean {
                    if (id.isNullOrEmpty()) {
                        getDataRecentSearchAirport(bottomSheetBinding, recentSearchAdapter)
                    } else {
                        getDataAirport(bottomSheetBinding, id, searchAdapterFrom)
                    }
                    return true
                }
            },
        )
        bottomSheetDialog.setContentView(bottomSheetBinding.root)
        bottomSheetDialog.show()
    }

    private fun showBottomSheetDestinationTo() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val bottomSheetBinding = LayoutSheetDestinationBinding.inflate(layoutInflater)
        val searchAdapterTo: SearchAdapter by lazy {
            SearchAdapter {
                insertData(2, it)
                addDataRecentSearch(it)
                bottomSheetDialog.dismiss()
            }
        }
        val recentSearchAdapter: RecentSearchAdapter by lazy {
            RecentSearchAdapter(
                onDeleteClick = {
                    deleteRecentSearch(it)
                },
                onClick = {
                    insertData(2, it)
                    bottomSheetDialog.dismiss()
                },
            )
        }
        getDataRecentSearchAirport(bottomSheetBinding, recentSearchAdapter)
        bottomSheetBinding.apply {
            bottomSheetBinding.ivCross.setOnClickListener {
                bottomSheetDialog.dismiss()
            }
            bottomSheetBinding.rvSearch.apply {
                adapter = searchAdapterTo
            }
            bottomSheetBinding.rvRecentSearch.apply {
                adapter = recentSearchAdapter
            }
        }
        val searchView = bottomSheetBinding.searchView
        searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(id: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(id: String?): Boolean {
                    if (id.isNullOrEmpty()) {
                        getDataRecentSearchAirport(bottomSheetBinding, recentSearchAdapter)
                    } else {
                        getDataAirport(bottomSheetBinding, id, searchAdapterTo)
                    }
                    return true
                }
            },
        )
        bottomSheetDialog.setContentView(bottomSheetBinding.root)
        bottomSheetDialog.show()
    }

    private fun deleteRecentSearch(airport: Airport) {
        viewModel.deleteRecentSearch(airport).observe(viewLifecycleOwner) {}
    }

    private fun addDataRecentSearch(airport: Airport) {
        viewModel.insertRecentSearch(airport).observe(viewLifecycleOwner) {
            it.proceedWhen()
        }
    }

    private fun showBottomSheetPassengerCount() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val bottomSheetBinding = LayoutSheetPassengerCountBinding.inflate(layoutInflater)
        val temp = dataPassenger!!
        setUpPassengerData(bottomSheetBinding, temp)
        bottomSheetBinding.apply {
            bottomSheetBinding.ivCross.setOnClickListener {
                bottomSheetDialog.dismiss()
            }
            bottomSheetBinding.ivAdultPlus.setOnClickListener {
                temp.adult++
                setUpPassengerData(bottomSheetBinding, temp)
            }
            bottomSheetBinding.ivAdultMinus.setOnClickListener {
                if (temp.adult <= 1) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.minimal_1_orang_dewasa),
                        Toast.LENGTH_SHORT,
                    )
                        .show()
                } else {
                    temp.adult--
                    setUpPassengerData(bottomSheetBinding, temp)
                }
            }
            bottomSheetBinding.ivChildrenPlus.setOnClickListener {
                temp.children++
                setUpPassengerData(bottomSheetBinding, temp)
            }
            bottomSheetBinding.ivChildrenMinus.setOnClickListener {
                if (temp.children > 0) {
                    temp.children--
                    setUpPassengerData(bottomSheetBinding, temp)
                }
            }
            bottomSheetBinding.ivBabyPlus.setOnClickListener {
                temp.baby++
                setUpPassengerData(bottomSheetBinding, temp)
            }
            bottomSheetBinding.ivBabyMinus.setOnClickListener {
                if (temp.baby > 0) {
                    temp.baby--
                    setUpPassengerData(bottomSheetBinding, temp)
                }
            }
            bottomSheetBinding.btnSave.setOnClickListener {
                dataPassenger = temp
                setUpHeader()
                bottomSheetDialog.dismiss()
            }
        }
        bottomSheetDialog.setContentView(bottomSheetBinding.root)
        bottomSheetDialog.show()
    }

    private fun setUpPassengerData(
        bottomSheetBinding: LayoutSheetPassengerCountBinding,
        temp: Passenger,
    ) {
        bottomSheetBinding.tvBabyCount.text = temp.baby.toString()
        bottomSheetBinding.tvChildrenCount.text = temp.children.toString()
        bottomSheetBinding.tvAdultCount.text = temp.adult.toString()
    }

    private fun showBottomSheetPassengerClass() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val bottomSheetBinding = LayoutSheetFlightTypeBinding.inflate(layoutInflater)
        val classOption = viewModel.getOption()
        var temp: SeatClass = dataClass!!
        val passengerClassAdapter: OptionClassAdapter by lazy {
            OptionClassAdapter(classOption) {
                setUpHeader()
                temp = it
            }
        }
        bottomSheetBinding.apply {
            bottomSheetBinding.ivCross.setOnClickListener {
                bottomSheetDialog.dismiss()
            }
            bottomSheetBinding.rvClassList.apply {
                adapter = passengerClassAdapter
                passengerClassAdapter.selectedPosition = findPosition(temp, classOption)
            }
            bottomSheetBinding.btnSave.setOnClickListener {
                dataClass = temp
                setUpHeader()
                bottomSheetDialog.dismiss()
            }
        }
        bottomSheetDialog.setContentView(bottomSheetBinding.root)
        bottomSheetDialog.show()
    }

    private fun findPosition(
        temp: SeatClass,
        classOption: List<SeatClass>,
    ): Int {
        return classOption.indexOfFirst { it.name == temp.name }
    }

    private fun showBottomSheetCalendarDeparture() {
        val calendarBottomSheet = CalendarBottomSheetFragment()
        val args =
            Bundle().apply {
                if (isReturn) {
                    putBoolean("isDeparture", true)
                    putBoolean("isReturn", true)
                    putString("selectedDate", binding.layoutHeader.tvDepartureDate.text.toString())
                    putString("returnDate", binding.layoutHeader.tvReturnDate.text.toString())
                } else {
                    putBoolean("isDeparture", true)
                    putBoolean("isReturn", false)
                    putString("selectedDate", binding.layoutHeader.tvDepartureDate.text.toString())
                    putString("returnDate", binding.layoutHeader.tvDepartureDate.text.toString())
                }
            }
        calendarBottomSheet.arguments = args
        calendarBottomSheet.setCalendarBottomSheetListener(this)
        calendarBottomSheet.show(childFragmentManager, "CalendarBottomSheet")
    }

    private fun showBottomSheetCalendarReturn() {
        val calendarBottomSheet = CalendarBottomSheetFragment()
        val args =
            Bundle().apply {
                putBoolean("isDeparture", false)
                putString("selectedDate", binding.layoutHeader.tvReturnDate.text.toString())
                putString("departureDate", binding.layoutHeader.tvDepartureDate.text.toString())
            }
        calendarBottomSheet.arguments = args
        calendarBottomSheet.setCalendarBottomSheetListener(this)
        calendarBottomSheet.show(childFragmentManager, "CalendarBottomSheet")
    }

    override fun onDateDepartureSelected(selectedDate: LocalDate) {
        dataDateDeparture = selectedDate
        binding.layoutHeader.tvDepartureDate.text =
            selectedDate.format(DateTimeFormatter.ofPattern(getString(R.string.format_date)))
    }

    override fun onDateReturnSelected(selectedDate: LocalDate) {
        dataDateReturn = selectedDate
        binding.layoutHeader.tvReturnDate.isSelected = true
        binding.layoutHeader.tvReturnDate.text =
            selectedDate.format(DateTimeFormatter.ofPattern(getString(R.string.format_date)))
    }
}
