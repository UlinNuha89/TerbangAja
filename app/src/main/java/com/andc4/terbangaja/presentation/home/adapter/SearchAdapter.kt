package com.andc4.terbangaja.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.andc4.terbangaja.data.model.Airport
import com.andc4.terbangaja.databinding.ItemSearchDestinationBinding

class SearchAdapter(
    private val onDeleteClick: (Airport) -> Unit,
) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {
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
    ): SearchViewHolder {
        val binding =
            ItemSearchDestinationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding, onDeleteClick)
    }

    fun submitData(data: List<Airport>) {
        dataDiffer.submitList(data)
    }

    override fun onBindViewHolder(
        holder: SearchViewHolder,
        position: Int,
    ) {
        holder.bind(dataDiffer.currentList[position])
    }

    override fun getItemCount(): Int {
        return dataDiffer.currentList.size
    }

    inner class SearchViewHolder(
        private val binding: ItemSearchDestinationBinding,
        val onDeleteClick: (Airport) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cityName: Airport) {
            with(cityName) {
                binding.ivDelete.isVisible = false
                binding.tvSearchResult.text = this.city
                itemView.setOnClickListener {
                    onDeleteClick(this)
                }
            }
        }
    }
}
