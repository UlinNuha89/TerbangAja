package com.andc4.terbangaja.presentation.ticketorder.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.andc4.terbangaja.R
import com.andc4.terbangaja.data.model.Flight
import com.andc4.terbangaja.databinding.ItemTicketOrderBinding
import com.andc4.terbangaja.utils.toIndonesianFormat
import com.facebook.shimmer.ShimmerFrameLayout
import java.time.Duration
import java.time.LocalDateTime

class TicketPagingAdapter(
    private val seatClass: String,
    private val onClick: (Flight) -> Unit,
) : PagingDataAdapter<Flight, RecyclerView.ViewHolder>(COMPARATOR) {
    private val ITEM_VIEW_TYPE_DATA = 0
    private val ITEM_VIEW_TYPE_SHIMMER = 1

    override fun getItemViewType(position: Int): Int {
        return if (itemCount == 0) ITEM_VIEW_TYPE_SHIMMER else ITEM_VIEW_TYPE_DATA
    }

    companion object {
        private val COMPARATOR =
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
            }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder {
        return if (viewType == ITEM_VIEW_TYPE_SHIMMER) {
            val view =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_ticket_order_shimmer, parent, false)
            ShimmerViewHolder(view)
        } else {
            val binding =
                ItemTicketOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            TicketViewHolder(seatClass, binding, onClick)
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        if (holder is ShimmerViewHolder) {
            holder.shimmerContainer.startShimmer()
        } else if (holder is TicketViewHolder) {
            val flight = getItem(position)
            if (flight != null) {
                holder.bind(flight)
            }
        }
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        if (holder is ShimmerViewHolder) {
            holder.shimmerContainer.stopShimmer()
        }
    }

    inner class ShimmerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val shimmerContainer: ShimmerFrameLayout =
            itemView.findViewById(R.id.shimmer_frame_layout_flight_ticket)
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
                    String.format("%02d : %02d", flight.arrivalTime.hour, flight.arrivalTime.minute)
                binding.tvDeparturePlace.text = flight.airportDeparture.city
                binding.tvDepartureTime.text =
                    String.format(
                        "%02d : %02d",
                        flight.departureTime.hour,
                        flight.departureTime.minute,
                    )
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
