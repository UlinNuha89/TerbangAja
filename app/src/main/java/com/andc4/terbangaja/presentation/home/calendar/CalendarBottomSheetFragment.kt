package com.andc4.terbangaja.presentation.home.calendar

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.children
import com.andc4.terbangaja.R
import com.andc4.terbangaja.databinding.LayoutSheetCalendarBinding
import com.andc4.terbangaja.utils.displayText
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.yearMonth
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.ViewContainer
import java.time.LocalDate
import java.time.format.DateTimeFormatter

interface CalendarBottomSheetListener {
    fun onDateDepartureSelected(selectedDate: LocalDate)

    fun onDateReturnSelected(selectedDate: LocalDate)
}

class CalendarBottomSheetFragment() : BottomSheetDialogFragment() {
    private var _binding: LayoutSheetCalendarBinding? = null
    private val binding get() = _binding!!
    private var isDeparture: Boolean? = true
    private var selectedDate: LocalDate? = null
    private var calendarListener: CalendarBottomSheetListener? = null

    private val dateFormatter = DateTimeFormatter.ofPattern("EEE, d MMM yyyy")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = LayoutSheetCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        getData()
        setupCalendarView()
        setupSelectButton()

        binding.ivCross.setOnClickListener {
            dismiss()
        }
    }

    private fun getData() {
        isDeparture = arguments?.getBoolean("isDeparture")
        if (isDeparture!!) {
            val isReturn = arguments?.getBoolean("isReturn")
            if (isReturn!!) {
                val selectedDateString = arguments?.getString("selectedDate")
                selectedDate = selectedDateString?.let { LocalDate.parse(it, DateTimeFormatter.ofPattern(getString(R.string.format_date))) }
                binding.tvDateDeparture.text = selectedDate?.format(DateTimeFormatter.ofPattern(getString(R.string.format_date)))
                val returnDate =
                    arguments?.getString("returnDate").let {
                        LocalDate.parse(it, DateTimeFormatter.ofPattern(getString(R.string.format_date)))
                    }
                binding.tvDateReturn.text = returnDate?.format(DateTimeFormatter.ofPattern(getString(R.string.format_date)))
            } else {
                val selectedDateString = arguments?.getString("selectedDate")
                selectedDate = selectedDateString?.let { LocalDate.parse(it, DateTimeFormatter.ofPattern(getString(R.string.format_date))) }
                binding.tvDateDeparture.text = selectedDate?.format(DateTimeFormatter.ofPattern(getString(R.string.format_date)))
            }
        } else {
            val selectedDateString = arguments?.getString("selectedDate")
            selectedDate = selectedDateString?.let { LocalDate.parse(it, DateTimeFormatter.ofPattern(getString(R.string.format_date))) }
            val departureDate =
                arguments?.getString("departureDate").let {
                    LocalDate.parse(it, DateTimeFormatter.ofPattern(getString(R.string.format_date)))
                }
            binding.tvDateDeparture.text = departureDate?.format(DateTimeFormatter.ofPattern(getString(R.string.format_date)))
            binding.tvDateReturn.text = selectedDate?.format(DateTimeFormatter.ofPattern(getString(R.string.format_date)))
        }
    }

    private fun setupCalendarView() {
        val daysOfWeek = daysOfWeek()
        val dayViews = binding.layoutWeek.root.children.filterIsInstance<TextView>().toList()
        dayViews.forEachIndexed { index, textView ->
            textView.text =
                daysOfWeek[index].displayText() // Ensure daysOfWeek[index].displayText() gives correct string for each day
        }
        val currentMonth = selectedDate!!.yearMonth
        val startMonth = currentMonth.minusMonths(10)
        val endMonth = currentMonth.plusMonths(10)
        val firstDayOfWeek = daysOfWeek.first()

        binding.rvDate.setup(startMonth, endMonth, firstDayOfWeek)
        binding.rvDate.scrollToMonth(currentMonth)

        class DayViewContainer(view: View) : ViewContainer(view) {
            val textView: TextView = view.findViewById(R.id.tv_date)
            lateinit var day: CalendarDay

            init {
                view.setOnClickListener {
                    if (isDeparture!!/* some condition to check whether it's a departure or return date */) {
                        binding.tvDateDeparture.text = day.date.format(dateFormatter)
                    } else {
                        binding.tvDateReturn.text = day.date.format(dateFormatter)
                    }
                    val date = day.date
                    if (selectedDate != date) {
                        val oldDate = selectedDate
                        selectedDate = date
                        binding.rvDate.notifyDateChanged(date)
                        oldDate?.let { binding.rvDate.notifyDateChanged(it) }
                    }
                }
            }
        }

        binding.rvDate.dayBinder =
            object : MonthDayBinder<DayViewContainer> {
                override fun create(view: View) = DayViewContainer(view)

                override fun bind(
                    container: DayViewContainer,
                    day: CalendarDay,
                ) {
                    container.day = day
                    container.textView.text = day.date.dayOfMonth.toString()
                    if (day.position == DayPosition.MonthDate) {
                        if (day.date == selectedDate) {
                            container.textView.setBackgroundResource(R.drawable.bg_selected_date)
                            container.textView.setTextColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.white,
                                ),
                            )
                        } else {
                            container.textView.setBackgroundResource(R.drawable.bg_default_date)
                            container.textView.setTextColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.colorTextCalendar,
                                ),
                            )
                        }
                    } else {
                        container.textView.setBackgroundColor(Color.TRANSPARENT)
                        container.textView.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.gray,
                            ),
                        )
                    }
                }
            }

        binding.rvDate.monthScrollListener = { month ->
            val formatter = DateTimeFormatter.ofPattern(getString(R.string.format_month_year))
            binding.tvMonthCalendar.text = month.yearMonth.format(formatter)
        }

        binding.ivBackwardDate.setOnClickListener {
            binding.rvDate.findFirstVisibleMonth()?.let {
                binding.rvDate.smoothScrollToMonth(it.yearMonth.minusMonths(1))
            }
        }

        binding.ivForwardDate.setOnClickListener {
            binding.rvDate.findFirstVisibleMonth()?.let {
                binding.rvDate.smoothScrollToMonth(it.yearMonth.plusMonths(1))
            }
        }
    }

    private fun setupSelectButton() {
        binding.btnSave.setOnClickListener {
            // Handle save button click
            if (isDeparture!!) {
                selectedDate?.let {
                    calendarListener?.onDateDepartureSelected(it)
                }
            } else {
                selectedDate?.let {
                    calendarListener?.onDateReturnSelected(it)
                }
            }
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setCalendarBottomSheetListener(listener: CalendarBottomSheetListener) {
        this.calendarListener = listener
    }
}
