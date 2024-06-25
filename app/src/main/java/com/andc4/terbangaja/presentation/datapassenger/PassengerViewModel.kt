package com.andc4.terbangaja.presentation.datapassenger

import android.os.Bundle
import androidx.lifecycle.ViewModel
import com.andc4.terbangaja.data.model.FormPassenger
import com.andc4.terbangaja.data.model.UserTicket

class PassengerViewModel(
    private val extras: Bundle,
) : ViewModel() {
    fun getDataUserTicket() = extras.getParcelable<UserTicket>(PassengerActivity.EXTRAS_USER_TICKET)

    fun getIsRoundTrip() = extras.getBoolean(PassengerActivity.EXTRAS_IS_ROUND_TRIP)

    fun addDataPassenger(
        userTicket: UserTicket,
        formPassenger: List<FormPassenger>,
    ) = UserTicket(
        departureFlight = userTicket.departureFlight,
        returnFlight = userTicket.returnFlight,
        departureSeats = userTicket.departureSeats,
        returnSeats = userTicket.returnSeats,
        dataPassenger = formPassenger,
        seatClass = userTicket.seatClass,
        passenger = userTicket.passenger,
    )
}
