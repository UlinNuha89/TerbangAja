package com.andc4.terbangaja.presentation.ticketorder

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.andc4.terbangaja.R
import com.andc4.terbangaja.data.model.Ticket
import com.andc4.terbangaja.databinding.ActivityTicketOrderBinding
import com.andc4.terbangaja.databinding.ItemCalendarDateBinding
import com.andc4.terbangaja.utils.displayText
import com.andc4.terbangaja.utils.getColorCompat
import com.andc4.terbangaja.utils.getWeekPageTitle
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
    private var selectedDate: LocalDate = LocalDate.now()
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

        setData()
        setUpCalendar()
    }

    private fun setData() {
        val data = viewModel.getData()!!
        binding.tvEmptyTicket.isVisible = true
        binding.tvEmptyTicket.text =
            data.airportFrom.city + " " + data.airportTo.city + " \n" + data.departureDate.toString() + " " + data.returnDate.toString() + " " + data.passenger.adult + " " + data.seatClass.name.lowercase()
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
                        oldDate.let { binding.exSevenCalendar.notifyDateChanged(it) }
                        // doGetTicket
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
        val startDate = LocalDate.now()
        val endDate: LocalDate? = LocalDate.now().plusDays(9)
        if (endDate == null) {
            val currentMonth = YearMonth.now()
            binding.exSevenCalendar.setup(
                startDate,
                currentMonth.plusMonths(5).atEndOfMonth(),
                firstDayOfWeekFromLocale(),
            )
        } else {
            binding.exSevenCalendar.setup(
                startDate,
                endDate!!,
                firstDayOfWeekFromLocale(),
            )
        }

        binding.exSevenCalendar.scrollToDate(startDate) // Scroll to start date
    }
}
