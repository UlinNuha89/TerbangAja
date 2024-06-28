package com.andc4.terbangaja.presentation.history

import android.view.View
import com.andc4.terbangaja.R
import com.andc4.terbangaja.data.model.BookingHistoryModel
import com.andc4.terbangaja.databinding.ItemHistoryBinding
import com.andc4.terbangaja.utils.ChangeToCamelCase
import com.andc4.terbangaja.utils.DateUtils.calculateDuration
import com.andc4.terbangaja.utils.DateUtils.formatDate
import com.andc4.terbangaja.utils.DateUtils.formatTime
import com.xwray.groupie.databinding.BindableItem

class HistoryItem(
    private val bookingHistory: BookingHistoryModel,
    private val clickListener: (BookingHistoryModel) -> Unit,
) : BindableItem<ItemHistoryBinding>() {
    override fun getLayout(): Int = R.layout.item_history

    override fun bind(
        viewBinding: ItemHistoryBinding,
        position: Int,
    ) {
        viewBinding.tvFlightDuration.text = calculateDuration(bookingHistory.departureFlight.departureTime, bookingHistory.departureFlight.arrivalTime)
        viewBinding.tvDate.text = formatDate(bookingHistory.orderDate)
        viewBinding.tvDepartureLocation.text =
            bookingHistory.departureFlight.departureAirportData.city
        viewBinding.tvDepartureDate.text = formatDate(bookingHistory.departureFlight.departureTime)
        viewBinding.tvDepartureTime.text = formatTime(bookingHistory.departureFlight.departureTime)
        viewBinding.tvArrivalLocation.text = bookingHistory.departureFlight.arrivalAirportData.city
        viewBinding.tvArrivalDate.text = formatDate(bookingHistory.departureFlight.arrivalTime)
        viewBinding.tvArrivalTime.text = formatTime(bookingHistory.departureFlight.arrivalTime)
        viewBinding.tvBookingCodeNumber.text = bookingHistory.code
        val context = viewBinding.root.context
        if (bookingHistory.bookingSeats.isNotEmpty()) {
            val flightCLass = bookingHistory.bookingSeats.first().seat?.seatClass
            viewBinding.tvFlightClassType.text = ChangeToCamelCase(flightCLass)
        } else {
            viewBinding.tvFlightClassType.text = context.getString(R.string.n_a)
        }
        if (bookingHistory.returnFlight != null) {
            viewBinding.clReturnFlight.visibility = View.VISIBLE
            viewBinding.tvReturnLocation.text =
                bookingHistory.returnFlight.departureAirportData.city
            viewBinding.tvReturnDate.text = formatDate(bookingHistory.returnFlight.departureTime)
            viewBinding.tvReturnTime.text = formatTime(bookingHistory.returnFlight.departureTime)
            viewBinding.tvFlightDurationReturn.text = calculateDuration(bookingHistory.returnFlight.departureTime, bookingHistory.returnFlight.arrivalTime)
            viewBinding.tvReturnarrivalLocation.text =
                bookingHistory.returnFlight.arrivalAirportData.city
            viewBinding.tvReturnarrivalDate.text =
                formatDate(bookingHistory.returnFlight.arrivalTime)
            viewBinding.tvReturnarrivalTime.text =
                formatTime(bookingHistory.returnFlight.arrivalTime)
        }

        viewBinding.btnPaymentStatus.text = bookingHistory.payment?.status
        viewBinding.btnPaymentStatus.setBackgroundColor(
            when (bookingHistory.payment?.status) {
                context.getString(R.string.success) -> viewBinding.root.context.getColor(R.color.green)
                context.getString(R.string.failed) -> viewBinding.root.context.getColor(R.color.red)
                context.getString(R.string.pending) -> viewBinding.root.context.getColor(R.color.orange)
                context.getString(R.string.text_expired) -> viewBinding.root.context.getColor(R.color.light_gray)
                context.getString(R.string.text_need_method) -> viewBinding.root.context.getColor(R.color.light_blue)
                else -> viewBinding.root.context.getColor(R.color.purple3)
            },
        )

        viewBinding.root.setOnClickListener {
            clickListener(bookingHistory)
        }
    }
}
