package com.andc4.terbangaja.presentation.seat

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.andc4.terbangaja.data.model.Seats
import com.andc4.terbangaja.data.model.UserTicket
import com.andc4.terbangaja.data.repository.SeatRepository
import kotlinx.coroutines.Dispatchers

class SeatViewModel(
    private val extras: Bundle,
    private val seatRepository: SeatRepository,
) : ViewModel() {
    private val departureSeats = mutableListOf<Seats>()

    fun setDepartureSeatsData(seats: List<Seats>) {
        this.departureSeats.clear()
        this.departureSeats.addAll(seats)
    }

    fun getDepartureSelectedSeatObject(selectedSeatIdx: List<Int>): List<Seats> {
        val newSelectedIdx = selectedSeatIdx.map { it - 1 }
        return departureSeats.filterIndexed { index, _ -> newSelectedIdx.contains(index) }
    }

    private val returnSeats = mutableListOf<Seats>()

    fun setReturnSeatsData(seats: List<Seats>) {
        this.returnSeats.clear()
        this.returnSeats.addAll(seats)
    }

    fun getReturnSelectedSeatObject(selectedSeatIdx: List<Int>): List<Seats> {
        val newSelectedIdx = selectedSeatIdx.map { it - 1 }
        return returnSeats.filterIndexed { index, _ -> newSelectedIdx.contains(index) }
    }

    fun isRoundTrip() = extras.getBoolean(SeatActivity.EXTRAS_IS_ROUND_TRIP)

    fun getDataUserTicket() = extras.getParcelable<UserTicket>(SeatActivity.EXTRAS_USER_TICKET)

    fun getSeatList(
        flightId: Int,
        seatClass: String,
    ) = seatRepository.getSeats(flightId, seatClass).asLiveData(Dispatchers.IO)

    fun addDataSeatsDepartureOnly(
        userTicket: UserTicket,
        seats: List<Seats>,
    ): UserTicket {
        return UserTicket(
            departureFlight = userTicket.departureFlight,
            departureSeats = seats,
            returnFlight = null,
            returnSeats = null,
            seatClass = userTicket.seatClass,
            passenger = userTicket.passenger,
            dataPassenger = userTicket.dataPassenger,
        )
    }

    fun addDataSeatsRoundTrip(
        userTicket: UserTicket,
        seatsDeparture: List<Seats>,
        seatsReturn: List<Seats>,
    ): UserTicket {
        return UserTicket(
            departureFlight = userTicket.departureFlight,
            departureSeats = seatsDeparture,
            returnFlight = userTicket.returnFlight,
            returnSeats = seatsReturn,
            seatClass = userTicket.seatClass,
            passenger = userTicket.passenger,
            dataPassenger = userTicket.dataPassenger,
        )
    }
}
