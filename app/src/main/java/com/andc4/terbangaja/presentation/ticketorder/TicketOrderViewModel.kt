package com.andc4.terbangaja.presentation.ticketorder

import android.os.Bundle
import androidx.lifecycle.ViewModel
import com.andc4.terbangaja.data.model.Ticket

class TicketOrderViewModel(
    private val extras: Bundle,
) : ViewModel() {
    fun getData() = extras.getParcelable<Ticket>(TicketOrderActivity.EXTRAS_ITEM)
}
