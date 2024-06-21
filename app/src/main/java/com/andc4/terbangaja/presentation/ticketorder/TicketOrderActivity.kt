package com.andc4.terbangaja.presentation.ticketorder

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.andc4.terbangaja.R
import com.andc4.terbangaja.data.model.Filter
import com.andc4.terbangaja.data.model.Flight
import com.andc4.terbangaja.data.model.SeatClass
import com.andc4.terbangaja.data.model.Ticket
import com.andc4.terbangaja.databinding.ActivityTicketOrderBinding
import com.andc4.terbangaja.databinding.ItemCalendarDateBinding
import com.andc4.terbangaja.databinding.LayoutSheetFilterBinding
import com.andc4.terbangaja.presentation.detailticket.DetailTicketActivity
import com.andc4.terbangaja.presentation.ticketorder.adapter.OptionFilterAdapter
import com.andc4.terbangaja.presentation.ticketorder.adapter.TicketAdapter
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

class TicketOrderActivity : AppCompatActivity() {
    private val binding: ActivityTicketOrderBinding by lazy {
        ActivityTicketOrderBinding.inflate(layoutInflater)
    }
    private val viewModel: TicketOrderViewModel by viewModel {
        parametersOf(intent.extras)
    }
    private val ticketAdapter: TicketAdapter by lazy {
        TicketAdapter(viewModel.getData()!!.seatClass.name) {
            navToDetailTicket(it, viewModel.getData()!!.seatClass)
        }
    }

    private var data: Ticket? = null
    private var filterOption: Filter? = null
    private var selectedDate: LocalDate? = null
    private val dateFormatter = DateTimeFormatter.ofPattern("dd")

    companion object {
        const val EXTRAS_ITEM = "EXTRAS_ITEM"

        fun startActivity(
            context: Context,
            ticket: Ticket,
        ) {
            val intent = Intent(context, TicketOrderActivity::class.java)
            intent.putExtra(EXTRAS_ITEM, ticket)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setUpTicket()
        setData()
        getFlightTicket(viewModel.getData()!!)
        setOnClick()
        setUpCalendar()
    }

    private fun setOnClick() {
        binding.cardFilterOption.setOnClickListener {
            showBottomSheetFilter()
        }
        binding.layoutHeader.ivBackFlight.setOnClickListener {
            onBackPressed()
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
        getFlightTicket(data!!)
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
        setUpHeader()
        setUpFilter()
    }

    private fun setUpHeader() {
        binding.layoutHeader.tvDestination.text =
            data?.airportFrom?.city + " > " + data?.airportTo?.city
        binding.layoutHeader.tvPassenger.text =
            getString(R.string.text_penumpang, totalPassenger(data!!).toString())
        binding.layoutHeader.tvTypeFlight.text = data?.seatClass?.name
    }

    private fun getFlightTicket(data: Ticket) {
        viewModel.getFLightTicket(
            data.airportFrom.city,
            data.airportTo.city,
            selectedDate.toString(),
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
                        getFlightTicket(data!!)
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
                data?.returnDate
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

    private fun navToDetailTicket(
        flight: Flight,
        seatClass: SeatClass,
    ) {
        DetailTicketActivity.startActivity(this, flight, seatClass)
    }
}
