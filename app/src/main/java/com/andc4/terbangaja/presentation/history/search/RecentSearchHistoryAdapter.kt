package com.andc4.terbangaja.presentation.history.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.andc4.terbangaja.R
import com.andc4.terbangaja.data.model.SearchHistory
import com.andc4.terbangaja.databinding.ItemSearchDestinationBinding

class RecentSearchHistoryAdapter(
    private val onDeleteClick: (SearchHistory) -> Unit,
    private val onClick: (SearchHistory) -> Unit,
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
        return RecentSearchHistoryViewHolder(binding, onDeleteClick, onClick)
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
        val onClick: (SearchHistory) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cityName: SearchHistory) {
            with(cityName) {
                if (this.returnflight == null) {
                    binding.tvSearchResult.text =
                        binding.root.context.getString(
                            R.string.text_search_history_result_alt,
                            this.code,
                            this.departure,
                        )
                } else {
                    binding.tvSearchResult.text =
                        binding.root.context.getString(
                            R.string.text_search_history_result,
                            this.code,
                            this.departure,
                            this.returnflight,
                        )
                }
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
