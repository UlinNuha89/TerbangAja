package com.andc4.terbangaja.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.andc4.terbangaja.data.repository.AirportRepository
import com.andc4.terbangaja.data.repository.FlightRepository
import com.andc4.terbangaja.data.repository.SearchRepository
import kotlinx.coroutines.Dispatchers

class HomeViewModel(
    private val flightRepository: FlightRepository,
    private val airlineRepository: AirportRepository,
    private val searchRepository: SearchRepository,
) : ViewModel() {
    fun getFLight() = flightRepository.getFLights().asLiveData(Dispatchers.IO)

    fun getAirport(item: String?) = airlineRepository.getAirports(item).asLiveData(Dispatchers.IO)

    fun getRecentSearch() = searchRepository.getAllSearch().asLiveData(Dispatchers.IO)
}
