package com.andc4.terbangaja.presentation.detailhistory

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.andc4.terbangaja.R
import com.andc4.terbangaja.data.model.BookingHistoryModel
import com.andc4.terbangaja.databinding.ActivityDetailHistoryBinding
import com.andc4.terbangaja.presentation.detailhistory.adapter.BookingPassengerModelAdapter
import com.andc4.terbangaja.utils.ChangeToCamelCase
import com.andc4.terbangaja.utils.DateUtils.formatDate
import com.andc4.terbangaja.utils.DateUtils.formatTime
import com.andc4.terbangaja.utils.toIndonesianFormat
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class DetailHistoryActivity : AppCompatActivity() {
    private val binding: ActivityDetailHistoryBinding by lazy {
        ActivityDetailHistoryBinding.inflate(layoutInflater)
    }

    private val viewModel: DetailHistoryViewModel by viewModel {
        parametersOf(intent.extras)
    }
    private val bookingAdapter: BookingPassengerModelAdapter = BookingPassengerModelAdapter()

    companion object {
        const val EXTRA_HISTORY = "extra_history"

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
        setAdapter()
        setHeader()
    }

    private fun setHeader() {
        binding.layoutHeader.ivBackHeader.setOnClickListener {
            finish()
        }
        binding.layoutHeader.tvTitle.text = getString(R.string.text_detail_history)
    }

    private fun setAdapter() {
        binding.itemReturnDetail.rvPassengerList.apply {
            adapter = bookingAdapter
            layoutManager = LinearLayoutManager(context)
        }
        binding.itemDepartureDetail.rvPassengerList.apply {
            adapter = bookingAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun getIntentData() {
        val history = viewModel.history
        bindData(history)
    }

    private fun bindData(history: BookingHistoryModel?) {
        binding.apply {
            btnPaymentStatus.text = history?.payment?.status
            tvAdultPassenger.text = history?.adultCount.toString()
            tvBabyPassenger.text = history?.babyCount.toString()
            tvTotalPrice.text = history?.priceAmount.toString()
            tvChildPassenger.text = history?.childCount.toString()
            tvAdultPassengerPrice.text = viewModel.calculatePassengerPrices(history).first.toIndonesianFormat()
            tvChildPassengerPrice.text = viewModel.calculatePassengerPrices(history).second.toIndonesianFormat()
            tvAdultPassenger.text = getString(R.string.adult_passenger_format, history?.adultCount ?: 0)
            tvChildPassenger.text = getString(R.string.child_passenger_format, history?.childCount ?: 0)
            tvBabyPassenger.text = getString(R.string.baby_passenger_format, history?.babyCount ?: 0)
            tvTotalPrice.text = history?.priceAmount.toIndonesianFormat()

            bindItemDeparture(history)
            bindPaymentStatus(history)
            if (history?.returnFlight != null) {
                bindItemReturn(history)
            }
            history?.bookingPassengers?.let { bookingAdapter.submitDataBookingPassengerModel(it) }
            val testing = history?.bookingPassengers?.first()?.passenger?.name
            Log.e("DetailHistoryActivity", "bindData: $testing")
        }
    }

    private fun bindPaymentStatus(history: BookingHistoryModel?) {
        when (history?.payment?.status) {
            getString(R.string.success) -> binding.btnPaymentStatus.setBackgroundColor(getColor(R.color.green))
            getString(R.string.failed) -> binding.btnPaymentStatus.setBackgroundColor(getColor(R.color.red))
            getString(R.string.pending) -> binding.btnPaymentStatus.setBackgroundColor(getColor(R.color.orange))
            getString(R.string.text_expired) -> binding.btnPaymentStatus.setBackgroundColor(getColor(R.color.light_gray))
            getString(R.string.text_need_method) -> binding.btnPaymentStatus.setBackgroundColor(getColor(R.color.light_blue))
            else -> binding.btnPaymentStatus.setBackgroundColor(getColor(R.color.purple3))
        }
    }

    private fun bindItemReturn(history: BookingHistoryModel) {
        binding.apply {
            itemReturnDetail.root.visibility = View.VISIBLE
            itemReturnDetail.tvDepartureDate.text =
                formatDate(history.returnFlight?.departureTime)
            itemReturnDetail.tvDepartureTime.text =
                formatTime(history.returnFlight?.departureTime)
            itemReturnDetail.tvDepartureAirport.text =
                history.returnFlight?.departureAirportData?.name
            itemReturnDetail.tvArrivalDate.text = formatDate(history.returnFlight?.arrivalTime)
            itemReturnDetail.tvArrivalTime.text = formatTime(history.returnFlight?.arrivalTime)
            itemReturnDetail.tvArrivalAirport.text =
                history.returnFlight?.arrivalAirportData?.name
            if (history.bookingSeats.isNotEmpty()) {
                val seatClassType = history.bookingSeats.first().seat?.seatClass
                itemReturnDetail.tvFlightClass.text = seatClassType
            } else {
                itemReturnDetail.tvFlightClass.text = getString(R.string.n_a)
            }
            itemReturnDetail.tvFlightAirline.text = history.returnFlight?.airline?.name
            itemReturnDetail.tvBookingCodeNumber.text = history.code
            itemReturnDetail.tvFlightCode.text = history.returnFlight?.airline?.code
            itemReturnDetail.ivFlightLogo.load(history.returnFlight?.airline?.imgUrl)
        }
    }

    private fun bindItemDeparture(history: BookingHistoryModel?) {
        binding.apply {
            itemDepartureDetail.tvDepartureDate.text =
                formatDate(history?.departureFlight?.departureTime)
            itemDepartureDetail.tvDepartureTime.text =
                formatTime(history?.departureFlight?.departureTime)
            itemDepartureDetail.tvDepartureAirport.text =
                history?.departureFlight?.departureAirportData?.name
            itemDepartureDetail.tvArrivalDate.text =
                formatDate(history?.departureFlight?.arrivalTime)
            itemDepartureDetail.tvArrivalTime.text =
                formatTime(history?.departureFlight?.arrivalTime)
            itemDepartureDetail.tvArrivalAirport.text =
                history?.departureFlight?.arrivalAirportData?.name
            if (history?.bookingSeats?.isNotEmpty() == true) {
                val seatClassType = history.bookingSeats.first().seat?.seatClass
                itemDepartureDetail.tvFlightClass.text = ChangeToCamelCase(seatClassType)
            } else {
                itemDepartureDetail.tvFlightClass.text = getString(R.string.n_a)
            }
            itemDepartureDetail.tvFlightAirline.text = history?.departureFlight?.airline?.name
            itemDepartureDetail.tvBookingCodeNumber.text = history?.code
            itemDepartureDetail.tvFlightCode.text = history?.departureFlight?.airline?.code
            itemDepartureDetail.ivFlightLogo.load(history?.departureFlight?.airline?.imgUrl)
        }
    }
}
