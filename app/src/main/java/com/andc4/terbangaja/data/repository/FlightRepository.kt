package com.andc4.terbangaja.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.andc4.terbangaja.data.datasource.FlightDataSource
import com.andc4.terbangaja.data.mapper.toFlight
import com.andc4.terbangaja.data.mapper.toFlightTicket
import com.andc4.terbangaja.data.model.Flight
import com.andc4.terbangaja.data.paging.FlightTicketPagingSource
import com.andc4.terbangaja.data.paging.FlightsPagingSource
import com.andc4.terbangaja.utils.ResultWrapper
import com.andc4.terbangaja.utils.proceedFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

interface FlightRepository {
    fun getFLights(): Flow<ResultWrapper<List<Flight>>>

    fun getFLightsPaging(): Flow<PagingData<Flight>>

    fun getFlightsTicket(
        from: String,
        to: String,
        departureDate: String,
        totalPassengers: Int,
        seatClass: String,
        filter: String?,
    ): Flow<ResultWrapper<List<Flight>>>

    fun getFlightsTicketPaging(
        from: String,
        to: String,
        departureDate: String,
        totalPassengers: Int,
        seatClass: String,
        filter: String?,
    ): Flow<PagingData<Flight>>
}

class FlightRepositoryImpl(private val dataSource: FlightDataSource) : FlightRepository {
    override fun getFLights(): Flow<ResultWrapper<List<Flight>>> {
        return proceedFlow {
            dataSource.getFlights(1).toFlight()
        }.map {
            if (it.payload?.isEmpty() == false) return@map it
            ResultWrapper.Empty(it.payload)
        }.catch {
            emit(ResultWrapper.Error(Exception(it)))
        }.onStart {
            emit(ResultWrapper.Loading())
            delay(1000)
        }
    }

    override fun getFLightsPaging(): Flow<PagingData<Flight>> {
        return Pager(
            pagingSourceFactory = { FlightsPagingSource(dataSource) },
            config = PagingConfig(pageSize = 10),
        ).flow
    }

    override fun getFlightsTicket(
        from: String,
        to: String,
        departureDate: String,
        totalPassengers: Int,
        seatClass: String,
        filter: String?,
    ): Flow<ResultWrapper<List<Flight>>> {
        return proceedFlow {
            dataSource.getFlightsTicket(
                1,
                from,
                to,
                departureDate,
                totalPassengers.toString(),
                seatClass,
                filter,
            ).toFlightTicket()
        }.map {
            if (it.payload?.isEmpty() == false) return@map it
            ResultWrapper.Empty(it.payload)
        }.catch {
            emit(ResultWrapper.Error(Exception(it)))
        }.onStart {
            emit(ResultWrapper.Loading())
            delay(1000)
        }
    }

    override fun getFlightsTicketPaging(
        from: String,
        to: String,
        departureDate: String,
        totalPassengers: Int,
        seatClass: String,
        filter: String?,
    ): Flow<PagingData<Flight>> {
        return Pager(
            pagingSourceFactory = { FlightTicketPagingSource(dataSource, from, to, departureDate, totalPassengers, seatClass, filter) },
            config = PagingConfig(pageSize = 10),
        ).flow
    }
}
