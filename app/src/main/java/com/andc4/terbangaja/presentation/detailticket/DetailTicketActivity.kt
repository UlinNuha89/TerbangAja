package com.andc4.terbangaja.presentation.detailticket

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.andc4.terbangaja.R
import com.andc4.terbangaja.data.model.Flight
import com.andc4.terbangaja.data.model.SeatClass
import com.andc4.terbangaja.data.model.Ticket
import com.andc4.terbangaja.databinding.ActivityDetailTicketBinding
import com.andc4.terbangaja.presentation.common.CommonFragment
import com.andc4.terbangaja.utils.toIndonesianFormat
import com.andc4.terbangaja.utils.toIndonesianTime
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.time.Duration
import java.time.LocalDateTime

class DetailTicketActivity : AppCompatActivity() {
    private val binding: ActivityDetailTicketBinding by lazy {
        ActivityDetailTicketBinding.inflate(layoutInflater)
    }
    private var seatClass: SeatClass? = null
    private var data: Flight? = null
    private val viewModel: DetailTicketViewModel by viewModel {
        parametersOf(intent.extras)
    }

    companion object {
        const val EXTRAS_ITEM_FLIGHT = "EXTRAS_ITEM_FLIGHT"
        const val EXTRAS_ITEM_TICKET = "EXTRAS_ITEM_TICKET"

        fun startActivity(
            context: Context,
            flight: Flight,
            ticket: Ticket,
        ) {
            val intent = Intent(context, DetailTicketActivity::class.java)
            intent.putExtra(EXTRAS_ITEM_FLIGHT, flight)
            intent.putExtra(EXTRAS_ITEM_TICKET, ticket)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setUpHeader()
        setUpData()
        setOnClick()
    }

    private fun setOnClick() {
        binding.layoutHeader.ivBackHeader.setOnClickListener {
            onBackPressed()
        }
        binding.btnConfirm.setOnClickListener {
            if (isTicketAvailable(seatClass!!)) {
                if (viewModel.isLogin()) {
                    // navToSeat()
                } else {
                    showBottomSheetNoLogin()
                }
            } else {
                showBottomSheetNoTicket()
            }
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
/*
    private fun navToSeat() {
        if (viewModel.getUserTicket() == null) {
            SeatActivity.startActivity(this, data!!, viewModel.getTicket()!!)
        } else {
            SeatActivity.startActivitySecond(this, data!!, viewModel.getTicket()!!, viewModel.getUserTicket()!!)
        }
    }*/

    private fun isTicketAvailable(seatClass: SeatClass): Boolean {
        return when (seatClass.name) {
            "Economy" -> data?.numberOfEconomySeatsLeft!! > 1
            "Premium Economy" -> data?.numberOfPremiumSeatsLeft!! > 1
            "Business" -> data?.numberOfBusinessSeatsLeft!! > 1
            "First Class" -> data?.numberOfFirstClassSeatsLeft!! > 1
            else -> false
        }
    }

    private fun setUpData() {
        data = viewModel.getData()
        seatClass = viewModel.getTicket()?.seatClass
        bindData()
    }

    private fun bindData() {
        data?.let {
            binding.itemSearchDetail.tvDepartureLocation.text = it.airportDeparture.city
            binding.itemSearchDetail.tvArrivalLocation.text = it.airportArrival.city
            binding.itemSearchDetail.tvFlightDuration.text = getDuration(it.departureTime, it.arrivalTime)
            binding.itemSearchDetail.tvDepartureTime.text =
                String.format("%02d : %02d", it.departureTime.hour, it.departureTime.minute)
            binding.itemSearchDetail.tvDepartureDate.text = it.departureTime.toIndonesianTime()
            binding.itemSearchDetail.tvDepartureAirport.text = it.airportDeparture.name
            binding.itemSearchDetail.tvFlightAirline.text = it.airline.name
            binding.itemSearchDetail.tvFlightClass.text = seatClass!!.name
            binding.itemSearchDetail.tvFlightCode.text = it.airline.code
            binding.itemSearchDetail.ivFlightLogo.load(it.airline.imgUrl) {
                crossfade(true)
            }
            binding.itemSearchDetail.tvFlightFeature.text = getFlightFeature(it)
            binding.itemSearchDetail.tvArrivalTime.text =
                String.format("%02d : %02d", it.arrivalTime.hour, it.arrivalTime.minute)
            binding.itemSearchDetail.tvArrivalDate.text = it.arrivalTime.toIndonesianTime()
            binding.itemSearchDetail.tvArrivalAirport.text = it.airportArrival.name
            binding.tvPriceTag.text = getPrice(it, seatClass!!)
        }
    }

    private fun getPrice(
        flight: Flight,
        seatClass: SeatClass,
    ): String {
        return when (seatClass.name) {
            "Economy" -> flight.economyPrice.toIndonesianFormat()!!
            "Premium Economy" -> flight.premiumPrice.toIndonesianFormat()!!
            "Business" -> flight.businessPrice.toIndonesianFormat()!!
            "First Class" -> flight.firstClassPrice.toIndonesianFormat()!!
            else -> ""
        }
    }

    private fun getFlightFeature(flight: Flight): String {
        return "Baggage ${flight.airline.baggage} Kg, Cabin baggage ${flight.airline.cabinBaggage} Kg, In Flight Entertainment"
    }

    private fun getDuration(
        departureTime: LocalDateTime,
        arrivalTime: LocalDateTime,
    ): String {
        val duration = Duration.between(departureTime, arrivalTime)
        val hours = duration.toHours()
        val minutes = duration.toMinutes() % 60
        return String.format("%02dJ : %02dM", hours, minutes)
    }

    private fun setUpHeader() {
        binding.layoutHeader.tvTitle.text = getString(R.string.text_title_detail_ticket)
    }
}
