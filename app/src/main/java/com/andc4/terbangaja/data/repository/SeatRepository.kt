package com.andc4.terbangaja.data.repository

import com.andc4.terbangaja.data.datasource.SeatDataSource
import com.andc4.terbangaja.data.mapper.toSeat
import com.andc4.terbangaja.data.model.Seats
import com.andc4.terbangaja.utils.ResultWrapper
import com.andc4.terbangaja.utils.proceedFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

interface SeatRepository {
    fun getSeats(
        flightId: Int,
        seatClass: String,
    ): Flow<ResultWrapper<List<Seats>>>
}

class SeatRepositoryImpl(private val dataSource: SeatDataSource) : SeatRepository {
    override fun getSeats(
        flightId: Int,
        seatClass: String,
    ): Flow<ResultWrapper<List<Seats>>> {
        return proceedFlow {
            dataSource.getSeats(flightId, seatClass).toSeat()
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
}
