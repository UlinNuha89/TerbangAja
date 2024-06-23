package com.andc4.terbangaja.presentation.ticketorder.detailticket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.load
import com.andc4.terbangaja.data.model.Flight
import com.andc4.terbangaja.data.model.SeatClass
import com.andc4.terbangaja.databinding.FragmentDetailTicketBinding
import com.andc4.terbangaja.utils.toIndonesianFormat
import com.andc4.terbangaja.utils.toIndonesianTime
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.time.Duration
import java.time.LocalDateTime

interface DetailTicketBottomSheetListener {
    fun onFlightDepartureSelected(flight: Flight)

    fun onFlightReturnSelected(flight: Flight)
}

class DetailTicketBottomSheetFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentDetailTicketBinding? = null
    private val binding get() = _binding!!
    private var dataFlight: Flight? = null
    private var seatClass: SeatClass? = null
    private var detailTicketListener: DetailTicketBottomSheetListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDetailTicketBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        getData()
        setupSelectButton()
    }

    private fun getData() {
        dataFlight = arguments?.getParcelable<Flight>("dataFlight")
        seatClass = arguments?.getParcelable<SeatClass>("dataSeat")

        bindData()
    }

    private fun bindData() {
        dataFlight?.let {
            binding.itemSearchDetail.tvDepartureLocation.text = it.airportDeparture.city
            binding.itemSearchDetail.tvArrivalLocation.text = it.airportArrival.city
            binding.itemSearchDetail.tvFlightDuration.text =
                getDuration(it.departureTime, it.arrivalTime)
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

    private fun getDuration(
        departureTime: LocalDateTime,
        arrivalTime: LocalDateTime,
    ): String {
        val duration = Duration.between(departureTime, arrivalTime)
        val hours = duration.toHours()
        val minutes = duration.toMinutes() % 60
        return String.format("%02dJ : %02dM", hours, minutes)
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

    private fun setupSelectButton() {
        binding.ivCross.setOnClickListener {
            dismiss()
        }
        binding.btnConfirm.setOnClickListener {
            val isDeparture = arguments?.getBoolean("isDeparture")
            if (isDeparture!!) {
                dataFlight?.let {
                    detailTicketListener?.onFlightDepartureSelected(it)
                }
                dismiss()
            } else {
                dataFlight?.let {
                    detailTicketListener?.onFlightReturnSelected(it)
                }
                dismiss()
            }
        }
    }

    fun setDetailTicketBottomSheetListener(listener: DetailTicketBottomSheetListener) {
        this.detailTicketListener = listener
    }
}
