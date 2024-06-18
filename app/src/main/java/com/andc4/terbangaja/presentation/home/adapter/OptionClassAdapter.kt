package com.andc4.terbangaja.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andc4.terbangaja.data.model.SeatClass
import com.andc4.terbangaja.databinding.ItemClassOptionBinding

class OptionClassAdapter(
    private val options: List<SeatClass>,
    private val onClick: (SeatClass) -> Unit,
) : RecyclerView.Adapter<OptionClassAdapter.OptionClassViewHolder>() {
    var selectedPosition = 0

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): OptionClassViewHolder {
        val binding =
            ItemClassOptionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OptionClassViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(
        holder: OptionClassViewHolder,
        position: Int,
    ) {
        val option = options[position]
        holder.bind(option, position == selectedPosition)
    }

    override fun getItemCount(): Int {
        return options.size
    }

    inner class OptionClassViewHolder(
        private val binding: ItemClassOptionBinding,
        val onClick: (SeatClass) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            seatClass: SeatClass,
            isSelected: Boolean,
        ) {
            with(seatClass) {
                binding.tvClassOptionName.text = this.name
                itemView.isSelected = isSelected
                binding.ivClassOptionCheckmark.isSelected = isSelected
                itemView.setOnClickListener {
                    val previousPosition = selectedPosition
                    selectedPosition = adapterPosition
                    notifyItemChanged(previousPosition)
                    notifyItemChanged(adapterPosition)
                    onClick(this)
                }
            }
        }
    }
}
