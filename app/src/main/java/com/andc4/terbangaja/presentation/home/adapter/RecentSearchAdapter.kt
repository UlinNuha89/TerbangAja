package com.andc4.terbangaja.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.andc4.terbangaja.data.model.Destination
import com.andc4.terbangaja.databinding.ItemSearchDestinationBinding

class RecentSearchAdapter(
    private val onDeleteClick: (Destination) -> Unit,
) : RecyclerView.Adapter<RecentSearchAdapter.RecentSearchViewHolder>() {
    private val dataDiffer =
        AsyncListDiffer(
            this,
            object : DiffUtil.ItemCallback<Destination>() {
                override fun areItemsTheSame(
                    oldItem: Destination,
                    newItem: Destination,
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: Destination,
                    newItem: Destination,
                ): Boolean {
                    return oldItem.hashCode() == newItem.hashCode()
                }
            },
        )

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecentSearchViewHolder {
        val binding =
            ItemSearchDestinationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecentSearchViewHolder(binding, onDeleteClick)
    }

    fun submitData(data: List<Destination>) {
        dataDiffer.submitList(data)
    }

    override fun onBindViewHolder(
        holder: RecentSearchViewHolder,
        position: Int,
    ) {
        holder.bind(dataDiffer.currentList[position])
    }

    override fun getItemCount(): Int {
        return dataDiffer.currentList.size
    }

    inner class RecentSearchViewHolder(
        private val binding: ItemSearchDestinationBinding,
        val onDeleteClick: (Destination) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cityName: Destination) {
            with(cityName) {
                binding.tvSearchResult.text = this.item
                binding.ivDelete.setOnClickListener {
                    onDeleteClick(this)
                }
            }
        }
    }
}
