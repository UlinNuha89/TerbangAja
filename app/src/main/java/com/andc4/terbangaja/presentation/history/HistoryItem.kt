package com.andc4.terbangaja.presentation.history

import android.view.View
import com.andc4.terbangaja.R
import com.andc4.terbangaja.data.model.BookingHistoryModel
import com.andc4.terbangaja.databinding.ItemHistoryBinding
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
        viewBinding.tvDate.text = formatDate(bookingHistory.orderDate)
        viewBinding.tvDepartureLocation.text =
            bookingHistory.departureFlight.departureAirportData.city
        viewBinding.tvDepartureDate.text = formatDate(bookingHistory.departureFlight.departureTime)
        viewBinding.tvDepartureTime.text = formatTime(bookingHistory.departureFlight.departureTime)
        viewBinding.tvArrivalLocation.text = bookingHistory.departureFlight.arrivalAirportData.city
        viewBinding.tvArrivalDate.text = formatDate(bookingHistory.departureFlight.arrivalTime)
        viewBinding.tvArrivalTime.text = formatTime(bookingHistory.departureFlight.arrivalTime)
        viewBinding.tvBookingCodeNumber.text = bookingHistory.code
        if (bookingHistory.bookingSeats.isNotEmpty()) {
            val seatClassType = bookingHistory.bookingSeats.first().seat?.seatClass
            viewBinding.tvFlightClassType.text = seatClassType
        } else {
            viewBinding.tvFlightClassType.text = "N/A"
        }
        if (bookingHistory.returnFlight != null) {
            viewBinding.clReturnFlight.visibility = View.VISIBLE
            viewBinding.tvReturnLocation.text =
                bookingHistory.returnFlight.departureAirportData.city
            viewBinding.tvReturnDate.text = formatDate(bookingHistory.returnFlight.departureTime)
            viewBinding.tvReturnTime.text = formatTime(bookingHistory.returnFlight.departureTime)
            viewBinding.tvReturnarrivalLocation.text =
                bookingHistory.returnFlight.arrivalAirportData.city
            viewBinding.tvReturnarrivalDate.text =
                formatDate(bookingHistory.returnFlight.arrivalTime)
            viewBinding.tvReturnarrivalTime.text =
                formatTime(bookingHistory.returnFlight.arrivalTime)
        }
        if (bookingHistory.bookingSeats.isNotEmpty()) {
            viewBinding.tvFlightClassType.text = bookingHistory.bookingSeats.first().seat?.seatClass
        }

        viewBinding.btnPaymentStatus.text = bookingHistory.payment?.status
//        viewBinding.btnPaymentStatus.setBackgroundColor(
//            when (bookingHistory.payment.status) {
//                "Success" -> viewBinding.root.context.getColor(R.color.green)
//                "Failed" -> viewBinding.root.context.getColor(R.color.red)
//                "Pending" -> viewBinding.root.context.getColor(R.color.yellow)
//                "Expire" -> viewBinding.root.context.getColor(R.color.gray)
//                "Need Method" -> viewBinding.root.context.getColor(R.color.blue)
//                else -> viewBinding.root.context.getColor(R.color.default)
//            }
//        )

        viewBinding.root.setOnClickListener {
            clickListener(bookingHistory)
        }
    }
}
