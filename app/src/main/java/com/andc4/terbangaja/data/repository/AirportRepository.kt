package com.andc4.terbangaja.data.repository

import com.andc4.terbangaja.data.datasource.AirportDataSource
import com.andc4.terbangaja.data.mapper.toAirport
import com.andc4.terbangaja.data.model.Airport
import com.andc4.terbangaja.utils.ResultWrapper
import com.andc4.terbangaja.utils.proceedFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

interface AirportRepository {
    fun getAirports(): Flow<ResultWrapper<List<Airport>>>
}

class AirportRepositoryImpl(private val dataSource: AirportDataSource) : AirportRepository {
    override fun getAirports(): Flow<ResultWrapper<List<Airport>>> {
        return proceedFlow {
            dataSource.getAirports().toAirport()
        }.catch {
            emit(ResultWrapper.Error(Exception(it)))
        }
    }
}
