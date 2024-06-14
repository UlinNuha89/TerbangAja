package com.andc4.terbangaja.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.andc4.terbangaja.data.datasource.FlightDataSource
import com.andc4.terbangaja.data.mapper.toFlight
import com.andc4.terbangaja.data.model.Flight
import com.andc4.terbangaja.data.paging.FlightsPagingSource
import com.andc4.terbangaja.utils.ResultWrapper
import com.andc4.terbangaja.utils.proceedFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

interface FlightRepository {
    fun getFLights(): Flow<ResultWrapper<List<Flight>>>

    fun getFLightsPaging(): Flow<PagingData<Flight>>
}

class FlightRepositoryImpl(private val dataSource: FlightDataSource) : FlightRepository {
    override fun getFLights(): Flow<ResultWrapper<List<Flight>>> {
       /* val dataFlight =
            listOf(
                FlightsData(
                    id = 2,
                    airlineId = 221,
                    departureAirport = 7,
                    arrivalAirport = 6,
                    discount = 10,
                    economyPrice = 74900000,
                    premiumPrice = 74900000,
                    businessPrice = 74900000,
                    firstClassPrice = 74900000,
                    numberOfEconomySeatsLeft = 72,
                    numberOfPremiumSeatsLeft = 72,
                    numberOfBusinessSeatsLeft = 36,
                    numberOfFirstClassSeatsLeft = 36,
                    departureTime = "2024-07-01T07:00:00.000Z",
                    arrivalTime = "2024-07-01T07:00:00.000Z",
                ),
                FlightsData(
                    id = 2,
                    airlineId = 221,
                    departureAirport = 7,
                    arrivalAirport = 6,
                    discount = 10,
                    economyPrice = 74900000,
                    premiumPrice = 74900000,
                    businessPrice = 74900000,
                    firstClassPrice = 74900000,
                    numberOfEconomySeatsLeft = 72,
                    numberOfPremiumSeatsLeft = 72,
                    numberOfBusinessSeatsLeft = 36,
                    numberOfFirstClassSeatsLeft = 36,
                    departureTime = "2024-07-01T07:00:00.000Z",
                    arrivalTime = "2024-07-01T07:00:00.000Z",
                ),
            )
        val result =
            BasePaging<List<FlightsData>>(
                next = PageData(1, 2),
                previous = null,
                result = dataFlight,
            )
        val data: BaseResponse<BasePaging<List<FlightsData>>> =
            BaseResponse(
                message = "GET DATA SUCCESS",
                data = result,
            )*/
        return proceedFlow {
            // data.toFlight()
            dataSource.getFlights(1).toFlight()
           /* val result = dataSource.getFlights()
            if (result.data?.size!! > 30) {
                result.data.subList(0, 30)
            }
            result.toFlight()*/
        }.catch {
            emit(ResultWrapper.Error(Exception(it)))
        }
    }

    override fun getFLightsPaging(): Flow<PagingData<Flight>> {
        return Pager(
            pagingSourceFactory = { FlightsPagingSource(dataSource) },
            config = PagingConfig(pageSize = 10),
        ).flow
    }
}
