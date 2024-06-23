package com.andc4.terbangaja.presentation.ticketorder

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.andc4.terbangaja.R
import com.andc4.terbangaja.data.model.Filter
import com.andc4.terbangaja.data.model.Flight
import com.andc4.terbangaja.data.model.SeatClass
import com.andc4.terbangaja.data.model.Ticket
import com.andc4.terbangaja.data.model.UserTicket
import com.andc4.terbangaja.databinding.ActivityTicketOrderBinding
import com.andc4.terbangaja.databinding.ItemCalendarDateBinding
import com.andc4.terbangaja.databinding.LayoutSheetFilterBinding
import com.andc4.terbangaja.presentation.common.CommonFragment
import com.andc4.terbangaja.presentation.seat.SeatActivity
import com.andc4.terbangaja.presentation.ticketorder.adapter.OptionFilterAdapter
import com.andc4.terbangaja.presentation.ticketorder.adapter.TicketAdapter
import com.andc4.terbangaja.presentation.ticketorder.detailticket.DetailTicketBottomSheetFragment
import com.andc4.terbangaja.presentation.ticketorder.detailticket.DetailTicketBottomSheetListener
import com.andc4.terbangaja.utils.displayText
import com.andc4.terbangaja.utils.getColorCompat
import com.andc4.terbangaja.utils.getWeekPageTitle
import com.andc4.terbangaja.utils.proceedWhen
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.kizitonwose.calendar.core.WeekDay
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.view.ViewContainer
import com.kizitonwose.calendar.view.WeekDayBinder
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class TicketOrderActivity : AppCompatActivity(), DetailTicketBottomSheetListener {
    private val binding: ActivityTicketOrderBinding by lazy {
        ActivityTicketOrderBinding.inflate(layoutInflater)
    }
    private val viewModel: TicketOrderViewModel by viewModel {
        parametersOf(intent.extras)
    }

    /*private val ticketAdapter: TicketPagingAdapter by lazy {
        TicketPagingAdapter(viewModel.getData()!!.seatClass.name) {
            showBottomSheetDetailTicket(it, data!!)
        }
    }*/
    private val ticketAdapter: TicketAdapter by lazy {
        TicketAdapter(viewModel.getData()!!.seatClass.name) {
            showBottomSheetDetailTicket(it, data!!)
        }
    }

    private var data: Ticket? = null
    private var filterOption: Filter? = null
    private var selectedDate: LocalDate? = null
    private val dateFormatter = DateTimeFormatter.ofPattern("dd")
    private var isDeparture = true
    private var dataFlightDeparture: Flight? = null
    private var dataFlightReturn: Flight? = null

    companion object {
        const val EXTRAS_TICKET = "EXTRAS_TICKET"
        const val EXTRAS_IS_ROUND_TRIP = "EXTRAS_IS_ROUND_TRIP"

        fun startActivity(
            context: Context,
            ticket: Ticket,
            isRoundTrip: Boolean,
        ) {
            val intent = Intent(context, TicketOrderActivity::class.java)
            intent.putExtra(EXTRAS_TICKET, ticket)
            intent.putExtra(EXTRAS_IS_ROUND_TRIP, isRoundTrip)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setUpTicket()
        setData()
        setOnClick()
    }

    private fun setOnClick() {
        binding.cardFilterOption.setOnClickListener {
            showBottomSheetFilter()
        }
        binding.layoutHeader.ivBackHeader.setOnClickListener {
            onBackPressed()
        }
        binding.cardTicketType.setOnClickListener {
            val data = viewModel.isRoundTrip()
            if (data) {
                isDeparture = !isDeparture
                setUpRoundTrip(isDeparture)
            } else {
                Toast.makeText(this, "Anda hanya mencari tiket untuk pergi", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        binding.btnConfirm.setOnClickListener {
            confirmTicket()
        }
    }

    private fun confirmTicket() {
        if (viewModel.isLogin()) {
            if (viewModel.isRoundTrip()) {
                if (dataFlightDeparture != null && dataFlightReturn != null) {
                    if (isTicketAvailable(
                            dataFlightDeparture!!,
                            data!!.seatClass,
                        ) && isTicketAvailable(dataFlightReturn!!, data!!.seatClass)
                    ) {
                        navToSeat()
                    } else if (!isTicketAvailable(
                            dataFlightDeparture!!,
                            data!!.seatClass,
                        ) && isTicketAvailable(dataFlightReturn!!, data!!.seatClass)
                    ) {
                        Toast.makeText(
                            this,
                            "Tiket Pergi Yang Anda Pilih Kosong",
                            Toast.LENGTH_SHORT,
                        ).show()
                        showBottomSheetNoTicket()
                    } else if (isTicketAvailable(
                            dataFlightDeparture!!,
                            data!!.seatClass,
                        ) && !isTicketAvailable(dataFlightReturn!!, data!!.seatClass)
                    ) {
                        Toast.makeText(
                            this,
                            "Tiket Pulang Yang Anda Pilih Kosong",
                            Toast.LENGTH_SHORT,
                        ).show()
                        showBottomSheetNoTicket()
                    }
                } else if (dataFlightDeparture == null && dataFlightReturn != null) {
                    Toast.makeText(this, "Silahkan Pilih Tiket Pulang", Toast.LENGTH_SHORT)
                        .show()
                } else if (dataFlightDeparture != null && dataFlightReturn == null) {
                    Toast.makeText(this, "Silahkan Pilih Tiket Pergi", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(this, "Silahkan Pilih Tiket", Toast.LENGTH_SHORT).show()
                }
            } else {
                if (dataFlightDeparture != null) {
                    navToSeat()
                } else {
                    Toast.makeText(this, "Silahkan Pilih Tiket Pergi", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        } else {
            showBottomSheetNoLogin()
        }
    }

    private fun navToSeat() {
        val isRoundTrip = viewModel.isRoundTrip()
        if (!isRoundTrip) {
            val dataUserTicket: UserTicket =
                viewModel.changeToUserTicketDepartureOnly(dataFlightDeparture!!, data!!)
            SeatActivity.startActivity(this, dataUserTicket, false)
        } else {
            val dataUserTicket: UserTicket =
                viewModel.changeToUserTicketRoundTrip(
                    dataFlightDeparture!!,
                    dataFlightReturn!!,
                    data!!,
                )
            SeatActivity.startActivity(this, dataUserTicket, true)
        }
    }

    private fun showBottomSheetFilter() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val bottomSheetBinding = LayoutSheetFilterBinding.inflate(layoutInflater)
        val dataOption = viewModel.dataOption()
        var temp: Filter? = filterOption
        val optionFilterAdapter: OptionFilterAdapter by lazy {
            OptionFilterAdapter(dataOption) {
                temp = it
            }
        }
        bottomSheetBinding.apply {
            bottomSheetBinding.ivCross.setOnClickListener {
                bottomSheetDialog.dismiss()
            }
            bottomSheetBinding.rvFilterList.apply {
                adapter = optionFilterAdapter
                optionFilterAdapter.selectedPosition = findPosition(temp!!, dataOption)
            }
            bottomSheetBinding.btnSave.setOnClickListener {
                filterOption = temp
                setUpFilter()
                bottomSheetDialog.dismiss()
            }
        }
        bottomSheetDialog.setContentView(bottomSheetBinding.root)
        bottomSheetDialog.show()
    }

    private fun findPosition(
        temp: Filter,
        classOption: List<Filter>,
    ): Int {
        return classOption.indexOfFirst { it.value == temp.value }
    }

    private fun setUpFilter() {
        binding.tvFilterCondition.text = filterOption?.category + " - " + filterOption?.name
        if (isDeparture) {
            getFlightTicketDepart(data!!)
        } else {
            getFlightTicketReturn(data!!)
        }
    }

    private fun setUpTicket() {
        binding.rvTicket.apply {
            adapter = ticketAdapter
        }
    }

    private fun bindFlightTicket(flight: List<Flight>) {
        ticketAdapter.submitData(flight)
    }

    private fun setData() {
        data = viewModel.getData()
        selectedDate = data?.departureDate
        filterOption = viewModel.initialFilter()
        val isRoundTrip = viewModel.isRoundTrip()
        setUpFilter()
        setUpHeader()
        if (!isRoundTrip) {
            setUpRoundTrip(isDeparture)
        } else {
            setUpRoundTrip(isDeparture)
        }
    }

    private fun setUpHeader() {
        binding.layoutHeader.tvPassenger.text =
            getString(R.string.text_penumpang, totalPassenger(data!!).toString())
        binding.layoutHeader.tvTypeFlight.text = data?.seatClass?.name
    }

    private fun setUpRoundTrip(isDeparture: Boolean) {
        if (isDeparture) {
            binding.layoutHeader.tvDestination.text =
                data?.airportFrom?.city + " > " + data?.airportTo?.city
            binding.tvTicketType.text = "Departure"
            getFlightTicketDepart(data!!)
            selectedDate = data!!.departureDate
            setUpCalendar()
        } else {
            binding.layoutHeader.tvDestination.text =
                data?.airportTo?.city + " > " + data?.airportFrom?.city
            binding.tvTicketType.text = "Return"
            getFlightTicketReturn(data!!)
            selectedDate = data!!.returnDate
            setUpCalendar()
        }
    }

    private fun getFlightTicketDepart(
        data: Ticket,
        date: LocalDate? = null,
    ) {
        viewModel.getFLightTicket(
            data.airportFrom.city,
            data.airportTo.city,
            date?.toString() ?: data.departureDate.toString(),
            totalPassenger(data),
            data.seatClass.value,
            filterOption?.value,
        ).observe(this) {
            it.proceedWhen(
                doOnSuccess = {
                    binding.contentState.root.isVisible = false
                    binding.contentState.ivEmptyTicket.isVisible = false
                    binding.contentState.tvEmptyTicket.isVisible = false
                    binding.contentState.tvEmptyTicketSub.isVisible = false
                    binding.shimmerFrameLayoutFlightTicket.isVisible = false
                    binding.rvTicket.isVisible = true
                    it.payload?.let {
                        bindFlightTicket(it)
                    }
                },
                doOnEmpty = {
                    if (it.payload?.isEmpty() == true) {
                        binding.shimmerFrameLayoutFlightTicket.isVisible = false
                        binding.rvTicket.isVisible = false
                        binding.contentState.root.isVisible = true
                        binding.contentState.ivEmptyTicket.isVisible = true
                        binding.contentState.tvEmptyTicket.isVisible = true
                        binding.contentState.tvEmptyTicketSub.isVisible = true
                    }
                },
                doOnLoading = {
                    binding.shimmerFrameLayoutFlightTicket.isVisible = true
                    binding.shimmerFrameLayoutFlightTicket.startShimmer()
                    binding.rvTicket.isVisible = false
                    binding.contentState.root.isVisible = false
                    binding.contentState.ivEmptyTicket.isVisible = false
                    binding.contentState.tvEmptyTicket.isVisible = false
                    binding.contentState.tvEmptyTicketSub.isVisible = false
                },
            )
        }
        /*binding.rvTicket.apply {
            adapter = ticketAdapter
        }
        lifecycleScope.launch {
            viewModel.getFLightTicketPaging(
                data.airportFrom.city,
                data.airportTo.city,
                date?.toString() ?: data.departureDate.toString(),
                totalPassenger(data),
                data.seatClass.value,
                filterOption?.value,
            ).collectLatest {
                ticketAdapter.submitData(it)
            }
        }
        lifecycleScope.launch {
            ticketAdapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .collectLatest { loadStates ->
                    if (loadStates.refresh is LoadState.Loading) {
                        binding.rvTicket.isVisible = false
                        binding.shimmerFrameLayoutFlightTicket.isVisible = true
                        binding.shimmerFrameLayoutFlightTicket.startShimmer()
                    } else {
                        binding.rvTicket.isVisible = true
                        binding.shimmerFrameLayoutFlightTicket.isVisible = false
                        binding.shimmerFrameLayoutFlightTicket.stopShimmer()
                    }
                }
        }*/
    }

    private fun getFlightTicketReturn(
        data: Ticket,
        date: LocalDate? = null,
    ) {
        viewModel.getFLightTicket(
            data.airportTo.city,
            data.airportFrom.city,
            date?.toString() ?: data.returnDate.toString(),
            totalPassenger(data),
            data.seatClass.value,
            filterOption?.value,
        ).observe(this) {
            it.proceedWhen(
                doOnSuccess = {
                    binding.contentState.root.isVisible = false
                    binding.contentState.ivEmptyTicket.isVisible = false
                    binding.contentState.tvEmptyTicket.isVisible = false
                    binding.contentState.tvEmptyTicketSub.isVisible = false
                    binding.shimmerFrameLayoutFlightTicket.isVisible = false
                    binding.rvTicket.isVisible = true
                    it.payload?.let {
                        bindFlightTicket(it)
                    }
                },
                doOnEmpty = {
                    if (it.payload?.isEmpty() == true) {
                        binding.shimmerFrameLayoutFlightTicket.isVisible = false
                        binding.rvTicket.isVisible = false
                        binding.contentState.root.isVisible = true
                        binding.contentState.ivEmptyTicket.isVisible = true
                        binding.contentState.tvEmptyTicket.isVisible = true
                        binding.contentState.tvEmptyTicketSub.isVisible = true
                    }
                },
                doOnLoading = {
                    binding.shimmerFrameLayoutFlightTicket.isVisible = true
                    binding.shimmerFrameLayoutFlightTicket.startShimmer()
                    binding.rvTicket.isVisible = false
                    binding.contentState.root.isVisible = false
                    binding.contentState.ivEmptyTicket.isVisible = false
                    binding.contentState.tvEmptyTicket.isVisible = false
                    binding.contentState.tvEmptyTicketSub.isVisible = false
                },
            )
        }
    }

    private fun totalPassenger(data: Ticket): Int {
        return data.passenger.adult + data.passenger.children
    }

    private fun setUpCalendar() {
        class DayViewContainer(view: View) : ViewContainer(view) {
            val bind = ItemCalendarDateBinding.bind(view)
            lateinit var day: WeekDay

            init {
                view.setOnClickListener {
                    if (selectedDate != day.date) {
                        val oldDate = selectedDate
                        selectedDate = day.date
                        binding.exSevenCalendar.notifyDateChanged(day.date)
                        oldDate.let { binding.exSevenCalendar.notifyDateChanged(it!!) }
                        // doGetTicket
                        if (isDeparture)
                            {
                                getFlightTicketDepart(data!!, selectedDate)
                            } else
                            {
                                getFlightTicketReturn(data!!, selectedDate)
                            }
                    }
                }
            }

            fun bind(day: WeekDay) {
                this.day = day
                bind.exSevenDateText.text = dateFormatter.format(day.date)
                bind.exSevenDayText.text = day.date.dayOfWeek.displayText()

                val colorRes =
                    if (day.date == selectedDate) {
                        R.color.purple4
                    } else {
                        R.color.neutral5
                    }
                bind.exSevenDateText.setTextColor(view.context.getColorCompat(colorRes))
                bind.exSevenSelectedView.isVisible = day.date == selectedDate
            }
        }

        binding.exSevenCalendar.dayBinder =
            object : WeekDayBinder<DayViewContainer> {
                override fun create(view: View) = DayViewContainer(view)

                override fun bind(
                    container: DayViewContainer,
                    data: WeekDay,
                ) = container.bind(data)
            }

        binding.exSevenCalendar.weekScrollListener = { weekDays ->
            binding.exSevenToolbar.title = getWeekPageTitle(weekDays)
        }

        // Setup calendar only with selected dates range
        val startDate = selectedDate
        val endDate: LocalDate? =
            if (data?.returnDate == null) {
                startDate!!.plusDays(10)
            } else {
                data!!.returnDate!!.plusDays(10)
            }
        if (endDate == null) {
            val currentMonth = YearMonth.now()
            binding.exSevenCalendar.setup(
                startDate!!,
                currentMonth.plusMonths(5).atEndOfMonth(),
                firstDayOfWeekFromLocale(),
            )
        } else {
            binding.exSevenCalendar.setup(
                startDate!!,
                endDate,
                firstDayOfWeekFromLocale(),
            )
        }

        binding.exSevenCalendar.scrollToDate(startDate) // Scroll to start date
    }

    private fun showBottomSheetDetailTicket(
        flight: Flight,
        ticket: Ticket,
    ) {
        val detailBottomSheet = DetailTicketBottomSheetFragment()
        val args =
            Bundle().apply {
                if (isDeparture) {
                    putBoolean("isDeparture", true)
                    putParcelable("dataFlight", flight)
                    putParcelable("dataSeat", ticket.seatClass)
                } else {
                    putBoolean("isDeparture", false)
                    putParcelable("dataFlight", flight)
                    putParcelable("dataSeat", ticket.seatClass)
                }
            }
        detailBottomSheet.arguments = args
        detailBottomSheet.setDetailTicketBottomSheetListener(this)
        detailBottomSheet.show(supportFragmentManager, "DetailTicketBottomSheet")
    }

    private fun isTicketAvailable(
        dataFlight: Flight,
        seatClass: SeatClass,
    ): Boolean {
        return when (seatClass.name) {
            "Economy" -> dataFlight.numberOfEconomySeatsLeft > 1
            "Premium Economy" -> dataFlight.numberOfPremiumSeatsLeft > 1
            "Business" -> dataFlight.numberOfBusinessSeatsLeft > 1
            "First Class" -> dataFlight.numberOfFirstClassSeatsLeft > 1
            else -> false
        }
    }

    private fun showBottomSheetNoTicket() {
        val bottomSheet = CommonFragment()
        val args =
            Bundle().apply {
                putBoolean("isAvailable", false)
            }
        bottomSheet.arguments = args
        bottomSheet.show(supportFragmentManager, "CommonFragment")
    }

    private fun showBottomSheetNoLogin() {
        val bottomSheet = CommonFragment()
        val args =
            Bundle().apply {
                putBoolean("isAvailable", true)
                putBoolean("isLogin", false)
            }
        bottomSheet.arguments = args
        bottomSheet.show(supportFragmentManager, "CommonFragment")
    }

    override fun onFlightDepartureSelected(flight: Flight) {
        dataFlightDeparture = flight
    }

    override fun onFlightReturnSelected(flight: Flight) {
        dataFlightReturn = flight
    }
}
