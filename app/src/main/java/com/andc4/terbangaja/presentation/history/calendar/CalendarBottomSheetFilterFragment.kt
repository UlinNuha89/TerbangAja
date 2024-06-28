package com.andc4.terbangaja.presentation.history.calendar

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

interface CalendarBottomSheetFilterListener {
    fun onStartDateSelected(selectedDate: LocalDate)

    fun onEndDateSelected(selectedDate: LocalDate)

    fun onBothDatesSelected(
        startDate: LocalDate,
        endDate: LocalDate,
    )
}

class CalendarBottomSheetFilterFragment : BottomSheetDialogFragment() {
    private var _binding: LayoutSheetCalendarBinding? = null
    private val binding get() = _binding!!
    private var isSelectingStartDate: Boolean = true
    private var startDate: LocalDate? = null
    private var endDate: LocalDate? = null
    private var calendarListener: CalendarBottomSheetFilterListener? = null
    private lateinit var dateFormatter: DateTimeFormatter

    private var originalReturnDateText: String? = null

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
        dateFormatter = DateTimeFormatter.ofPattern(getString(R.string.eee_d_mmm_yyyy))
        getData()
        setupCalendarView()
        setupSelectButton()
        originalReturnDateText = binding.tvDateReturn.text.toString()

        binding.ivCross.setOnClickListener {
            dismiss()
        }

        binding.tvDateDeparture.setOnClickListener {
            isSelectingStartDate = true
        }

        binding.tvDateReturn.setOnClickListener {
            isSelectingStartDate = false
        }
    }

    private fun getData() {
        val startDateString = arguments?.getString("startDate")
        startDate = startDateString?.let { LocalDate.parse(it, DateTimeFormatter.ofPattern(getString(R.string.format_date))) }
        binding.tvDateDeparture.text = startDate?.format(DateTimeFormatter.ofPattern(getString(R.string.format_date)))

        val endDateString = arguments?.getString("endDate")
        endDate = endDateString?.let { LocalDate.parse(it, DateTimeFormatter.ofPattern(getString(R.string.format_date))) }
        updateDateTextViews()
    }

    private fun setupCalendarView() {
        val daysOfWeek = daysOfWeek()
        val dayViews = binding.layoutWeek.root.children.filterIsInstance<TextView>().toList()
        dayViews.forEachIndexed { index, textView ->
            textView.text = daysOfWeek[index].displayText()
        }
        val currentMonth = startDate?.yearMonth ?: LocalDate.now().yearMonth
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
                    if (isSelectingStartDate) {
                        if (startDate != day.date) {
                            val oldStartDate = startDate
                            startDate = day.date
                            oldStartDate?.let { binding.rvDate.notifyDateChanged(it) }
                            binding.rvDate.notifyDateChanged(day.date)
                        }
                    } else {
                        if (endDate != day.date) {
                            val oldEndDate = endDate
                            endDate = day.date
                            oldEndDate?.let { binding.rvDate.notifyDateChanged(it) }
                            binding.rvDate.notifyDateChanged(day.date)
                        }
                    }
                    updateDateTextViews()
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
                        when (day.date) {
                            startDate -> {
                                container.textView.setBackgroundResource(R.drawable.bg_selected_date)
                                container.textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                            }
                            endDate -> {
                                container.textView.setBackgroundResource(R.drawable.bg_selected_date)
                                container.textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                            }
                            else -> {
                                container.textView.setBackgroundResource(R.drawable.bg_default_date)
                                container.textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorTextCalendar))
                            }
                        }
                    } else {
                        container.textView.setBackgroundColor(Color.TRANSPARENT)
                        container.textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray))
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
            when {
                startDate != null && endDate != null -> calendarListener?.onBothDatesSelected(startDate!!, endDate!!)
                startDate != null -> calendarListener?.onStartDateSelected(startDate!!)
                endDate != null -> calendarListener?.onEndDateSelected(endDate!!)
            }
            dismiss()
        }
    }

    private fun updateDateTextViews() {
        binding.tvDateDeparture.text = startDate?.format(dateFormatter)
        binding.tvDateReturn.text = endDate?.format(dateFormatter) ?: originalReturnDateText
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setCalendarBottomSheetListener(listener: CalendarBottomSheetFilterListener) {
        this.calendarListener = listener
    }
}
