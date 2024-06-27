package com.andc4.terbangaja.presentation.detailticket

import android.os.Bundle
import androidx.lifecycle.ViewModel
import com.andc4.terbangaja.data.model.Flight
import com.andc4.terbangaja.data.model.Ticket
import com.andc4.terbangaja.data.repository.AuthRepository

@Deprecated("this code is not used")
class DetailTicketViewModel(
    private val extras: Bundle,
    private val authRepository: AuthRepository,
) : ViewModel() {
    fun getData() = extras.getParcelable<Flight>(DetailTicketActivity.EXTRAS_ITEM_FLIGHT)

    fun getTicket() = extras.getParcelable<Ticket>(DetailTicketActivity.EXTRAS_ITEM_TICKET)

    fun isLogin() = authRepository.isLogin()
}
