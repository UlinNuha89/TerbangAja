package com.andc4.terbangaja.presentation.history.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.andc4.terbangaja.R
import com.andc4.terbangaja.data.model.SearchHistory
import com.andc4.terbangaja.databinding.ItemSearchDestinationBinding

class SearchHistoryAdapter(
    private val onDeleteClick: (SearchHistory) -> Unit,
) : RecyclerView.Adapter<SearchHistoryAdapter.SearchHistoryViewHolder>() {
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
    ): SearchHistoryViewHolder {
        val binding =
            ItemSearchDestinationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchHistoryViewHolder(binding, onDeleteClick)
    }

    fun submitData(data: List<SearchHistory>) {
        dataDiffer.submitList(data)
    }

    override fun onBindViewHolder(
        holder: SearchHistoryViewHolder,
        position: Int,
    ) {
        holder.bind(dataDiffer.currentList[position])
    }

    override fun getItemCount(): Int {
        return dataDiffer.currentList.size
    }

    inner class SearchHistoryViewHolder(
        private val binding: ItemSearchDestinationBinding,
        val onDeleteClick: (SearchHistory) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cityName: SearchHistory) {
            with(cityName) {
                binding.ivDelete.isVisible = false
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
                itemView.setOnClickListener {
                    onDeleteClick(this)
                }
            }
        }
    }
}
