package com.andc4.terbangaja.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.andc4.terbangaja.data.model.Airport
import com.andc4.terbangaja.data.model.Passenger
import com.andc4.terbangaja.data.model.SeatClass
import com.andc4.terbangaja.data.repository.AirportRepository
import com.andc4.terbangaja.data.repository.FlightRepository
import com.andc4.terbangaja.data.repository.SearchRepository
import com.andc4.terbangaja.utils.ResultWrapper
import kotlinx.coroutines.Dispatchers

class HomeViewModel(
    private val flightRepository: FlightRepository,
    private val airlineRepository: AirportRepository,
    private val searchRepository: SearchRepository,
) : ViewModel() {
    fun getFLight() = flightRepository.getFLights().asLiveData(Dispatchers.IO)

    fun initialPassenger() = Passenger(1, 0, 0)

    fun initialOption() = SeatClass("Economy", "economy")

    fun getOption() =
        listOf(
            SeatClass("Economy", "economy"),
            SeatClass("Premium Economy", "premium"),
            SeatClass("Business", "business"),
            SeatClass("First Class", "first_class"),
        )

    fun getAirport(item: String?) = airlineRepository.getAirports(item).asLiveData(Dispatchers.IO)

    fun getRecentSearch() = searchRepository.getAllSearch().asLiveData(Dispatchers.IO)

    fun insertRecentSearch(item: Airport): LiveData<ResultWrapper<Boolean>> {
        return item.let {
            searchRepository.insertSearch(it).asLiveData(Dispatchers.IO)
        }
    }

    fun deleteRecentSearch(item: Airport): LiveData<ResultWrapper<Boolean>> {
        return item.let {
            searchRepository.deleteSearch(it).asLiveData(Dispatchers.IO)
        }
    }
}
