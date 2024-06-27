package com.andc4.terbangaja.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.andc4.terbangaja.R
import com.andc4.terbangaja.data.model.Airport
import com.andc4.terbangaja.databinding.ItemSearchDestinationBinding

class RecentSearchAdapter(
    private val onDeleteClick: (Airport) -> Unit,
    private val onClick: (Airport) -> Unit,
) : RecyclerView.Adapter<RecentSearchAdapter.RecentSearchViewHolder>() {
    private val dataDiffer =
        AsyncListDiffer(
            this,
            object : DiffUtil.ItemCallback<Airport>() {
                override fun areItemsTheSame(
                    oldItem: Airport,
                    newItem: Airport,
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: Airport,
                    newItem: Airport,
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
        return RecentSearchViewHolder(binding, onDeleteClick, onClick)
    }

    fun submitData(data: List<Airport>) {
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
        val onDeleteClick: (Airport) -> Unit,
        val onClick: (Airport) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cityName: Airport) {
            with(cityName) {
                binding.tvSearchResult.text =
                    binding.root.context.getString(
                        R.string.text_search_result,
                        this.name,
                        this.city,
                        this.country,
                    )
                binding.ivDelete.setOnClickListener {
                    onDeleteClick(this)
                }
                itemView.setOnClickListener {
                    onClick(this)
                }
            }
        }
    }
}
