package com.andc4.terbangaja.presentation.ticketorder

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.andc4.terbangaja.data.model.Filter
import com.andc4.terbangaja.data.model.Flight
import com.andc4.terbangaja.data.model.Ticket
import com.andc4.terbangaja.data.model.UserTicket
import com.andc4.terbangaja.data.repository.AuthRepository
import com.andc4.terbangaja.data.repository.FlightRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class TicketOrderViewModel(
    private val extras: Bundle,
    private val flightRepository: FlightRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {
    fun getData() = extras.getParcelable<Ticket>(TicketOrderActivity.EXTRAS_TICKET)

    fun isRoundTrip() = extras.getBoolean(TicketOrderActivity.EXTRAS_IS_ROUND_TRIP)

    fun initialFilter() = Filter("Harga", "Termurah", "harga_termurah")

    fun dataOption() =
        listOf(
            Filter("Harga", "Termurah", "harga_termurah"),
            Filter("Durasi", "Terpendek", "durasi_terpendek"),
            Filter("Keberangkatan", "Paling Awal", "keberangkatan_paling_awal"),
            Filter("Keberangkatan", "Paling Akhir", "keberangkatan_paling_akhir"),
            Filter("Kedatangan", "Paling Awal", "kedatangan_paling_awal"),
            Filter("Kedatangan", "Paling Akhir", "kedatangan_paling_akhir"),
        )

    fun getFLightTicket(
        from: String,
        to: String,
        departureDate: String,
        totalPassengers: Int,
        seatClass: String,
        filter: String? = null,
    ) = flightRepository.getFlightsTicket(
        from,
        to,
        departureDate,
        totalPassengers,
        seatClass,
        filter,
    ).asLiveData(Dispatchers.IO)

    fun getFLightTicketPaging(
        from: String,
        to: String,
        departureDate: String,
        totalPassengers: Int,
        seatClass: String,
        filter: String? = null,
    ): Flow<PagingData<Flight>> {
        return flightRepository.getFlightsTicketPaging(
            from,
            to,
            departureDate,
            totalPassengers,
            seatClass,
            filter,
        ).cachedIn(viewModelScope)
    }

    fun changeToUserTicketDepartureOnly(
        flight: Flight,
        dataTicket: Ticket,
    ): UserTicket {
        return UserTicket(
            departureFlight = flight,
            departureSeats = null,
            returnFlight = null,
            returnSeats = null,
            seatClass = dataTicket.seatClass,
            passenger = dataTicket.passenger,
            dataPassenger = null,
        )
    }

    fun changeToUserTicketRoundTrip(
        departureFlight: Flight,
        returnFlight: Flight,
        dataTicket: Ticket,
    ): UserTicket {
        return UserTicket(
            departureFlight = departureFlight,
            departureSeats = null,
            returnFlight = returnFlight,
            returnSeats = null,
            seatClass = dataTicket.seatClass,
            passenger = dataTicket.passenger,
            dataPassenger = null,
        )
    }

    fun isLogin() = authRepository.isLogin()
}
