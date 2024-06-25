package com.andc4.terbangaja.presentation.checkout

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.andc4.terbangaja.data.model.Flight
import com.andc4.terbangaja.data.model.UserTicket
import com.andc4.terbangaja.data.repository.BookingRepository
import kotlinx.coroutines.Dispatchers

class CheckoutViewModel(
    private val extras: Bundle,
    private val bookingRepository: BookingRepository,
) : ViewModel() {
    fun getUserTicket() = extras.getParcelable<UserTicket>(CheckoutActivity.EXTRAS_USER_TICKET)

    fun getListFlight(): List<Flight> {
        return if (getUserTicket()!!.returnFlight == null) {
            listOf(getUserTicket()!!.departureFlight)
        } else {
            listOf(
                getUserTicket()!!.departureFlight,
                getUserTicket()!!.returnFlight!!,
            )
        }
    }

    fun doBooking(
        userTicket: UserTicket,
        price: Int,
    ) = bookingRepository.doBooking(userTicket, price).asLiveData(Dispatchers.IO)
}
