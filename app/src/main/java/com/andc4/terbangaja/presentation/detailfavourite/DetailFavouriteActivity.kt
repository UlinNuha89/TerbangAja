package com.andc4.terbangaja.presentation.detailfavourite

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.andc4.terbangaja.R
import com.andc4.terbangaja.data.model.Flight
import com.andc4.terbangaja.data.model.SeatClass
import com.andc4.terbangaja.databinding.ActivityDetailFavouriteBinding
import com.andc4.terbangaja.utils.toIndonesianTime
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.time.Duration
import java.time.LocalDateTime

class DetailFavouriteActivity : AppCompatActivity() {
    private val binding: ActivityDetailFavouriteBinding by lazy {
        ActivityDetailFavouriteBinding.inflate(layoutInflater)
    }
    private var seatClass: SeatClass? = null
    private var data: Flight? = null
    private val viewModel: DetailFavouriteViewModel by viewModel {
        parametersOf(intent.extras)
    }

    companion object {
        const val EXTRAS_ITEM_FLIGHT = "EXTRAS_ITEM_FLIGHT"

        fun startActivity(
            context: Context,
            flight: Flight,
        ) {
            val intent = Intent(context, DetailFavouriteActivity::class.java)
            intent.putExtra(EXTRAS_ITEM_FLIGHT, flight)
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
    }

    private fun setUpData() {
        data = viewModel.getData()
        seatClass = viewModel.getSeatClass()
        bindData()
    }

    private fun bindData() {
        data?.let {
            binding.ivArrivalImage.load(
                it.airportArrival.imgUrl,
            )
            binding.itemSearchDetail.tvDepartureLocation.text = it.airportDeparture.city
            binding.itemSearchDetail.tvArrivalLocation.text = it.airportArrival.city
            binding.itemSearchDetail.tvFlightDuration.text =
                getDuration(it.departureTime, it.arrivalTime)
            binding.itemSearchDetail.tvDepartureTime.text =
                String.format(getString(R.string.format_time), it.departureTime.hour, it.departureTime.minute)
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
                String.format(getString(R.string.format_time), it.arrivalTime.hour, it.arrivalTime.minute)
            binding.itemSearchDetail.tvArrivalDate.text = it.arrivalTime.toIndonesianTime()
            binding.itemSearchDetail.tvArrivalAirport.text = it.airportArrival.name
        }
    }

    private fun getFlightFeature(flight: Flight): String {
        return getString(
            R.string.text_flight_information,
            flight.airline.baggage.toString(),
            flight.airline.cabinBaggage.toString(),
        )
    }

    private fun getDuration(
        departureTime: LocalDateTime,
        arrivalTime: LocalDateTime,
    ): String {
        val duration = Duration.between(departureTime, arrivalTime)
        val hours = duration.toHours()
        val minutes = duration.toMinutes() % 60
        return String.format(getString(R.string.format_duration), hours, minutes)
    }

    private fun setUpHeader() {
        binding.layoutHeader.tvTitle.text = getString(R.string.detail_penerbangan_favorit)
    }
}
