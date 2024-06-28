package com.andc4.terbangaja.presentation.detailhistory

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.andc4.terbangaja.R
import com.andc4.terbangaja.data.model.BookingHistoryModel
import com.andc4.terbangaja.databinding.ActivityDetailHistoryBinding
import com.andc4.terbangaja.presentation.detailhistory.adapter.BookingPassengerModelAdapter
import com.andc4.terbangaja.presentation.main.MainActivity
import com.andc4.terbangaja.presentation.payment.PaymentActivity
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
            btnPaymentStatus.text = history?.payment?.status.orEmpty()
            tvAdultPassenger.text = history?.adultCount.toString()
            tvBabyPassenger.text = history?.babyCount.toString()
            tvTotalPrice.text = history?.priceAmount.toString()
            tvChildPassenger.text = history?.childCount.toString()
            tvAdultPassengerPrice.text =
                viewModel.calculatePassengerPrices(history).first.toIndonesianFormat()
            tvChildPassengerPrice.text =
                viewModel.calculatePassengerPrices(history).second.toIndonesianFormat()
            tvBabyPassengerPrice.text =
                viewModel.calculatePassengerPrices(history).third.toIndonesianFormat()
            tvAdultPassenger.text =
                getString(R.string.adult_passenger_format, history?.adultCount ?: 0)
            tvChildPassenger.text =
                getString(R.string.child_passenger_format, history?.childCount ?: 0)
            tvBabyPassenger.text =
                getString(R.string.baby_passenger_format, history?.babyCount ?: 0)
            tvTotalPrice.text = history?.priceAmount.toIndonesianFormat()

            bindItemDeparture(history)
            bindPaymentStatus(history)
            if (history?.returnFlight != null) {
                bindItemReturn(history)
            }
            history?.bookingPassengers?.let { bookingAdapter.submitDataBookingPassengerModel(it) }
        }
    }

    private fun bindPaymentStatus(history: BookingHistoryModel?) {
        when (history?.payment?.status) {
            getString(R.string.success) -> {
                binding.btnPaymentStatus.setBackgroundColor(getColor(R.color.green))
                binding.btnConfirm.text = getString(R.string.text_confirm)
                setButtonClick(1, "")
            }

            getString(R.string.failed) -> {
                binding.btnPaymentStatus.setBackgroundColor(getColor(R.color.red))
                binding.btnConfirm.text = getString(R.string.text_confirm)
                binding.clDetailPrice.isVisible = false
                binding.tvTotalPrice.isVisible = false
                binding.tvTotal.isVisible = false
                setButtonClick(1, "")
            }

            getString(R.string.pending) -> {
                binding.btnPaymentStatus.setBackgroundColor(getColor(R.color.orange))
                binding.btnConfirm.text = getString(R.string.text_go_to_payment)
                setButtonClick(2, history.payment.redirectUrl)
            }
            getString(R.string.text_expired) -> {
                binding.btnPaymentStatus.setBackgroundColor(
                    getColor(
                        R.color.light_gray,
                    ),
                )
                binding.btnConfirm.text = getString(R.string.text_confirm)
                binding.clDetailPrice.isVisible = false
                binding.tvTotalPrice.isVisible = false
                binding.tvTotal.isVisible = false
                setButtonClick(1, "")
            }

            getString(R.string.text_need_method) -> {
                binding.btnPaymentStatus.setBackgroundColor(
                    getColor(R.color.light_blue),
                )
                binding.btnConfirm.text = getString(R.string.text_go_to_payment)
                setButtonClick(2, history.payment.redirectUrl)
            }

            else -> binding.btnPaymentStatus.setBackgroundColor(getColor(R.color.purple3))
        }
    }

    private fun setButtonClick(
        mode: Int,
        link: String,
    ) {
        when (mode) {
            1 -> {
                binding.btnConfirm.setOnClickListener {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
            }

            2 -> {
                binding.btnConfirm.setOnClickListener {
                    navToPayment(link)
                }
            }
        }
    }

    private fun navToPayment(link: String) {
        PaymentActivity.startActivity(this, link)
    }

    private fun bindItemReturn(history: BookingHistoryModel) {
        binding.apply {
            itemReturnDetail.root.visibility = View.VISIBLE
            itemReturnDetail.tvDepartureDate.text =
                formatDate(history.returnFlight?.departureTime)
            itemReturnDetail.tvDepartureTime.text =
                formatTime(history.returnFlight?.departureTime)
            itemReturnDetail.tvDepartureAirport.text =
                history.returnFlight?.departureAirportData?.name.orEmpty()
            itemReturnDetail.tvArrivalDate.text = formatDate(history.returnFlight?.arrivalTime)
            itemReturnDetail.tvArrivalTime.text = formatTime(history.returnFlight?.arrivalTime)
            itemReturnDetail.tvArrivalAirport.text =
                history.returnFlight?.arrivalAirportData?.name.orEmpty()
            if (history.bookingSeats.isNotEmpty()) {
                val seatClassType = history.bookingSeats.first().seat?.seatClass.orEmpty()
                itemReturnDetail.tvFlightClass.text = seatClassType
            } else {
                itemReturnDetail.tvFlightClass.text = getString(R.string.n_a)
            }
            itemReturnDetail.tvFlightAirline.text =
                history.returnFlight?.airline?.name.orEmpty()
            itemReturnDetail.tvBookingCodeNumber.text = history.code
            itemReturnDetail.tvFlightCode.text = history.returnFlight?.airline?.code.orEmpty()
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
                history?.departureFlight?.arrivalAirportData?.name.orEmpty()
            if (history?.bookingSeats?.isNotEmpty() == true) {
                val seatClassType = history.bookingSeats.first().seat?.seatClass.orEmpty()
                itemDepartureDetail.tvFlightClass.text = ChangeToCamelCase(seatClassType)
            } else {
                itemDepartureDetail.tvFlightClass.text = getString(R.string.n_a)
            }
            itemDepartureDetail.tvFlightAirline.text =
                history?.departureFlight?.airline?.name.orEmpty()
            itemDepartureDetail.tvBookingCodeNumber.text = history?.code
            itemDepartureDetail.tvFlightCode.text =
                history?.departureFlight?.airline?.code.orEmpty()
            itemDepartureDetail.ivFlightLogo.load(history?.departureFlight?.airline?.imgUrl)
        }
    }
}
