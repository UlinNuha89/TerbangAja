package com.andc4.terbangaja.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.andc4.terbangaja.R
import com.andc4.terbangaja.data.model.Flight
import com.andc4.terbangaja.databinding.ItemDestinationBinding
import java.time.format.TextStyle
import java.util.Locale

@Deprecated("this code is not used")
class FavouriteDestinationPagingAdapter(
    private val onClick: (Flight) -> Unit,
) : PagingDataAdapter<Flight, FavouriteDestinationPagingAdapter.FavouriteDestinationViewHolder>(COMPARATOR) {
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
    ): FavouriteDestinationViewHolder {
        val binding =
            ItemDestinationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavouriteDestinationViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(
        holder: FavouriteDestinationViewHolder,
        position: Int,
    ) {
        val flight = getItem(position)
        if (flight != null) {
            holder.bind(flight)
        }
    }

    inner class FavouriteDestinationViewHolder(
        private val binding: ItemDestinationBinding,
        val onClick: (Flight) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cityName: Flight) {
            with(cityName) {
                binding.ivProductImage.setImageResource(R.drawable.img_eiffel)
                binding.tvDepartureLocation.text = this.airportDeparture.name
                binding.destinationLocation.text = this.airportArrival.name
                binding.tvAirline.text = this.airline.name
                binding.tvPrice.text = "IDR " + this.economyPrice.toString()
                val departureTime = this.departureTime.dayOfMonth.toString()
                val arrivalTime =
                    this.arrivalTime.dayOfMonth.toString() + " " +
                        this.arrivalTime.month.getDisplayName(
                            TextStyle.FULL, Locale.forLanguageTag("id"),
                        ) + " " + this.arrivalTime.year
                binding.tvDate.text = departureTime + " - " + arrivalTime
                itemView.setOnClickListener {
                    onClick(this)
                }
            }
        }
    }
}
