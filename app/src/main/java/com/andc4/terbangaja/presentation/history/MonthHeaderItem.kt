package com.andc4.terbangaja.presentation.history

import android.widget.TextView
import com.andc4.terbangaja.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class MonthHeaderItem(private val month: String) : Item<GroupieViewHolder>() {
    override fun getLayout() = R.layout.item_month_header

    override fun bind(
        viewHolder: GroupieViewHolder,
        position: Int,
    ) {
        viewHolder.itemView.findViewById<TextView>(R.id.tv_month).text = month
    }
}
