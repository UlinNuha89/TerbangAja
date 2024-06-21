package com.andc4.terbangaja.presentation.ticketorder.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andc4.terbangaja.data.model.Filter
import com.andc4.terbangaja.databinding.ItemFilterOptionBinding

class OptionFilterAdapter(
    private val filters: List<Filter>,
    private val onClick: (Filter) -> Unit,
) : RecyclerView.Adapter<OptionFilterAdapter.OptionClassViewHolder>() {
    var selectedPosition = 0

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): OptionClassViewHolder {
        val binding =
            ItemFilterOptionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OptionClassViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(
        holder: OptionClassViewHolder,
        position: Int,
    ) {
        val filter = filters[position]
        holder.bind(filter, position == selectedPosition)
    }

    override fun getItemCount(): Int {
        return filters.size
    }

    inner class OptionClassViewHolder(
        private val binding: ItemFilterOptionBinding,
        val onClick: (Filter) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            filter: Filter,
            isSelected: Boolean,
        ) {
            with(filter) {
                binding.tvClassOptionCategory.text = this.category
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
