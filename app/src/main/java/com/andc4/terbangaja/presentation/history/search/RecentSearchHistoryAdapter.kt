package com.andc4.terbangaja.presentation.history.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.andc4.terbangaja.data.model.SearchHistory
import com.andc4.terbangaja.databinding.ItemSearchDestinationBinding

class RecentSearchHistoryAdapter(
    private val onDeleteClick: (SearchHistory) -> Unit,
) : RecyclerView.Adapter<RecentSearchHistoryAdapter.RecentSearchHistoryViewHolder>() {
    private val dataDiffer =
        AsyncListDiffer(
            this,
            object : DiffUtil.ItemCallback<SearchHistory>() {
                override fun areItemsTheSame(
                    oldItem: SearchHistory,
                    newItem: SearchHistory,
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: SearchHistory,
                    newItem: SearchHistory,
                ): Boolean {
                    return oldItem.hashCode() == newItem.hashCode()
                }
            },
        )

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecentSearchHistoryViewHolder {
        val binding =
            ItemSearchDestinationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecentSearchHistoryViewHolder(binding, onDeleteClick)
    }

    fun submitData(data: List<SearchHistory>) {
        dataDiffer.submitList(data)
    }

    override fun onBindViewHolder(
        holder: RecentSearchHistoryViewHolder,
        position: Int,
    ) {
        holder.bind(dataDiffer.currentList[position])
    }

    override fun getItemCount(): Int {
        return dataDiffer.currentList.size
    }

    inner class RecentSearchHistoryViewHolder(
        private val binding: ItemSearchDestinationBinding,
        val onDeleteClick: (SearchHistory) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cityName: SearchHistory) {
            with(cityName) {
                binding.tvSearchResult.text = this.code
                binding.ivDelete.setOnClickListener {
                    onDeleteClick(this)
                }
            }
        }
    }
}
