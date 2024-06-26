package com.andc4.terbangaja.presentation.detailhistory

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.andc4.terbangaja.data.model.BookingHistoryModel
import com.andc4.terbangaja.databinding.ActivityDetailHistoryBinding
import com.andc4.terbangaja.utils.DateUtils.formatDate
import com.andc4.terbangaja.utils.DateUtils.formatTime

class DetailHistoryActivity : AppCompatActivity() {
    private val binding: ActivityDetailHistoryBinding by lazy {
        ActivityDetailHistoryBinding.inflate(layoutInflater)
    }

    companion object {
        private const val EXTRA_HISTORY = "extra_history"

        fun createIntent(
            context: Context,
            history: BookingHistoryModel,
        ): Intent {
            return Intent(context, DetailHistoryActivity::class.java).apply {
                putExtra(EXTRA_HISTORY, history)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        getIntentData()
    }

    private fun getIntentData() {
        val history: BookingHistoryModel? = intent.getParcelableExtra(EXTRA_HISTORY)
        bindData(history)
    }

    private fun bindData(history: BookingHistoryModel?) {
        binding.apply {
            btnPaymentStatus.text = history?.payment?.status
            tvAdultPassenger.text = history?.adultCount.toString()
            tvBabyPassenger.text = history?.babyCount.toString()
            tvTotalPrice.text = history?.priceAmount.toString()
            tvChildPassenger.text = history?.childCount.toString()
            itemSearchDetail.tvDepartureDate.text = formatDate(history?.departureFlight?.departureTime)
            itemSearchDetail.tvDepartureTime.text = formatTime(history?.departureFlight?.departureTime)
            itemSearchDetail.tvDepartureAirport.text = history?.departureFlight?.departureAirportData?.name
            itemSearchDetail.tvArrivalDate.text = formatDate(history?.departureFlight?.arrivalTime)
            itemSearchDetail.tvArrivalTime.text = formatTime(history?.departureFlight?.arrivalTime)
            itemSearchDetail.tvArrivalAirport.text = history?.departureFlight?.arrivalAirportData?.name
//            itemSearchDetail.tvFlightClass.text = history?.bookingSeats?.first()?.seat?.seatClass.orEmpty()
            itemSearchDetail.tvBookingCodeNumber.text = history?.code
            itemSearchDetail.tvFlightCode.text = history?.departureFlight?.airline?.code
            itemSearchDetail.ivFlightLogo.load(history?.departureFlight?.airline?.imgUrl)
            tvAdultPassenger.text = history?.adultCount.toString()
            tvChildPassenger.text = history?.childCount.toString()
            tvBabyPassenger.text = history?.babyCount.toString()
            tvTotalPrice.text = history?.priceAmount.toString()
        }
    }
}
