package com.andc4.terbangaja.presentation.ticketorder

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.andc4.terbangaja.data.model.Filter
import com.andc4.terbangaja.data.model.Ticket
import com.andc4.terbangaja.data.repository.FlightRepository
import kotlinx.coroutines.Dispatchers

class TicketOrderViewModel(
    private val extras: Bundle,
    private val flightRepository: FlightRepository,
) : ViewModel() {
    fun getData() = extras.getParcelable<Ticket>(TicketOrderActivity.EXTRAS_ITEM)

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
}
