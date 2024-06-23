package com.andc4.terbangaja.presentation.ticketorder.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.andc4.terbangaja.R

class LoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<com.andc4.terbangaja.presentation.ticketorder.adapter.LoadStateAdapter.LoadStateViewHolder>() {
    override fun onBindViewHolder(
        holder: LoadStateViewHolder,
        loadState: LoadState,
    ) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState,
    ): LoadStateViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.load_state_item, parent, false)
        return LoadStateViewHolder(view, retry)
    }

    class LoadStateViewHolder(itemView: View, retry: () -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private val progressBar: ProgressBar = itemView.findViewById(R.id.progress_bar)
        private val errorMsg: TextView = itemView.findViewById(R.id.error_msg)
        private val retryButton: Button = itemView.findViewById(R.id.retry_button)

        init {
            retryButton.setOnClickListener { retry.invoke() }
        }

        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                errorMsg.text = loadState.error.localizedMessage
            }
            progressBar.visibility = if (loadState is LoadState.Loading) View.VISIBLE else View.GONE
            retryButton.visibility = if (loadState is LoadState.Error) View.VISIBLE else View.GONE
            errorMsg.visibility = if (loadState is LoadState.Error) View.VISIBLE else View.GONE
        }
    }
}
