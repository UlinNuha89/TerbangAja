package com.andc4.terbangaja.presentation.detailticket

import android.os.Bundle
import androidx.lifecycle.ViewModel
import com.andc4.terbangaja.data.model.Flight
import com.andc4.terbangaja.data.model.SeatClass
import com.andc4.terbangaja.data.repository.AuthRepository

class DetailTicketViewModel(
    private val extras: Bundle,
    private val authRepository: AuthRepository,
) : ViewModel() {
    fun getData() = extras.getParcelable<Flight>(DetailTicketActivity.EXTRAS_ITEM_FLIGHT)

    fun getSeatClass() = extras.getParcelable<SeatClass>(DetailTicketActivity.EXTRAS_ITEM_SEAT_CLASS)

    fun isLogin() = authRepository.isLogin()
}
