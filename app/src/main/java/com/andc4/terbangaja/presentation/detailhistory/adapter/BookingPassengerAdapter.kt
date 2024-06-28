package com.andc4.terbangaja.presentation.detailhistory.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.andc4.terbangaja.data.model.BookingPassengerModel
import com.andc4.terbangaja.databinding.ItemPassengerBinding

class BookingPassengerModelAdapter :
    RecyclerView.Adapter<BookingPassengerModelAdapter.BookingPassengerModelItemViewHolder>() {
    private val differ =
        AsyncListDiffer(
            this,
            object : DiffUtil.ItemCallback<BookingPassengerModel>() {
                override fun areItemsTheSame(
                    oldItem: BookingPassengerModel,
                    newItem: BookingPassengerModel,
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: BookingPassengerModel,
                    newItem: BookingPassengerModel,
                ): Boolean {
                    return oldItem == newItem
                }
            },
        )

    fun submitDataBookingPassengerModel(data: List<BookingPassengerModel>) {
        differ.submitList(data)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): BookingPassengerModelItemViewHolder {
        val binding = ItemPassengerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookingPassengerModelItemViewHolder(binding)
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(
        holder: BookingPassengerModelItemViewHolder,
        position: Int,
    ) {
        holder.bind(differ.currentList[position])
    }

    inner class BookingPassengerModelItemViewHolder(private val binding: ItemPassengerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: BookingPassengerModel) {
            binding.tvPassengerName.text = data.passenger?.name
            binding.tvPassengerId.text = data.passenger?.id.toString()
        }
    }
}
