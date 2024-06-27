package com.andc4.terbangaja.presentation.ticketorder.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.andc4.terbangaja.R
import com.andc4.terbangaja.data.model.Flight
import com.andc4.terbangaja.databinding.ItemTicketOrderBinding
import com.andc4.terbangaja.utils.toIndonesianFormat
import java.time.Duration
import java.time.LocalDateTime

class TicketAdapter(
    private val seatClass: String,
    private val onClick: (Flight) -> Unit,
) : RecyclerView.Adapter<TicketAdapter.TicketViewHolder>() {
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
    ): TicketViewHolder {
        val binding =
            ItemTicketOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TicketViewHolder(seatClass, binding, onClick)
    }

    fun submitData(data: List<Flight>) {
        dataDiffer.submitList(data)
    }

    override fun onBindViewHolder(
        holder: TicketViewHolder,
        position: Int,
    ) {
        holder.bind(dataDiffer.currentList[position])
    }

    override fun getItemCount(): Int {
        return dataDiffer.currentList.size
    }

    inner class TicketViewHolder(
        private val seatClass: String,
        private val binding: ItemTicketOrderBinding,
        val onClick: (Flight) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(flight: Flight) {
            with(flight) {
                binding.tvArrivalPlace.text = flight.airportArrival.city
                binding.tvArrivalTime.text =
                    String.format(binding.root.context.getString(R.string.format_time), flight.arrivalTime.hour, flight.arrivalTime.minute)
                binding.tvDeparturePlace.text = flight.airportDeparture.city
                binding.tvDepartureTime.text =
                    String.format(binding.root.context.getString(R.string.format_time), flight.departureTime.hour, flight.departureTime.minute)
                val duration = getDuration(flight.departureTime, flight.arrivalTime)
                binding.tvFlightDuration.text = duration
                binding.tvFlightPrice.text = getPrice(seatClass, flight)
                binding.tvFlightAirline.text = flight.airline.name
                binding.tvSeatClassType.text = seatClass
                binding.ivFlightLogo.load(flight.airline.imgUrl) {
                    crossfade(true)
                }
                itemView.setOnClickListener {
                    onClick(this)
                }
            }
        }

        private fun getDuration(
            departureTime: LocalDateTime,
            arrivalTime: LocalDateTime,
        ): String {
            val duration = Duration.between(departureTime, arrivalTime)
            val hours = duration.toHours()
            val minutes = duration.toMinutes() % 60
            return String.format(binding.root.context.getString(R.string.format_duration), hours, minutes)
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
