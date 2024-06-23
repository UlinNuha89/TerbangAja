package com.andc4.terbangaja.presentation.checkout

import android.os.Bundle
import androidx.lifecycle.ViewModel
import com.andc4.terbangaja.data.model.Flight
import com.andc4.terbangaja.data.model.UserTicket

class CheckoutViewModel(
    private val extras: Bundle,
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
}
