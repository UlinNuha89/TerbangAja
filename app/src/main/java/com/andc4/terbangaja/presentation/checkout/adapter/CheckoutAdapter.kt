package com.andc4.terbangaja.presentation.checkout.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.andc4.terbangaja.data.model.Flight
import com.andc4.terbangaja.databinding.ItemDetailTicketBinding
import com.andc4.terbangaja.utils.toIndonesianFormat
import com.andc4.terbangaja.utils.toIndonesianTime
import java.time.Duration
import java.time.LocalDateTime

class CheckoutAdapter(
    private val seatClass: String,
    private val onClick: (Flight) -> Unit,
) : RecyclerView.Adapter<CheckoutAdapter.CheckoutViewHolder>() {
    private val dataDiffer =
        AsyncListDiffer(
            this,
            object : DiffUtil.ItemCallback<Flight>() {
                override fun areItemsTheSame(
                    oldItem: Flight,
                    newItem: Flight,
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: Flight,
                    newItem: Flight,
                ): Boolean {
                    return oldItem.hashCode() == newItem.hashCode()
                }
            },
        )

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): CheckoutViewHolder {
        val binding =
            ItemDetailTicketBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CheckoutViewHolder(seatClass, binding, onClick)
    }

    fun submitData(data: List<Flight>) {
        dataDiffer.submitList(data)
    }

    override fun onBindViewHolder(
        holder: CheckoutViewHolder,
        position: Int,
    ) {
        holder.bind(dataDiffer.currentList[position])
    }

    override fun getItemCount(): Int {
        return dataDiffer.currentList.size
    }

    inner class CheckoutViewHolder(
        private val seatClass: String,
        private val binding: ItemDetailTicketBinding,
        val onClick: (Flight) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(flight: Flight) {
            with(flight) {
                binding.tvArrivalLocation.text = flight.airportArrival.city
                binding.tvArrivalTime.text =
                    String.format("%02d : %02d", flight.arrivalTime.hour, flight.arrivalTime.minute)
                binding.tvArrivalDate.text = flight.arrivalTime.toIndonesianTime()
                binding.tvArrivalAirport.text = flight.airportArrival.name
                binding.tvDepartureLocation.text = flight.airportDeparture.city
                binding.tvDepartureTime.text =
                    String.format(
                        "%02d : %02d",
                        flight.departureTime.hour,
                        flight.departureTime.minute,
                    )
                binding.tvDepartureDate.text = flight.departureTime.toIndonesianTime()
                binding.tvDepartureAirport.text = flight.airportDeparture.name
                val duration = getDuration(flight.departureTime, flight.arrivalTime)
                binding.tvFlightDuration.text = duration
                binding.tvFlightAirline.text = flight.airline.name
                binding.tvFlightClass.text = seatClass
                binding.tvFlightCode.text = flight.airline.code
                binding.tvFlightFeature.text = getFlightFeature(flight)
                binding.ivFlightLogo.load(flight.airline.imgUrl) {
                    crossfade(true)
                }
                itemView.setOnClickListener {
                    onClick(this)
                }
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

        private fun getPrice(
            seatClass: String,
            flight: Flight,
        ): String {
            return when (seatClass) {
                "Economy" -> flight.economyPrice.toIndonesianFormat()!!
                "Premium Economy" -> flight.premiumPrice.toIndonesianFormat()!!
                "Business" -> flight.businessPrice.toIndonesianFormat()!!
                "First Class" -> flight.firstClassPrice.toIndonesianFormat()!!
                else -> ""
            }
        }
    }
}
