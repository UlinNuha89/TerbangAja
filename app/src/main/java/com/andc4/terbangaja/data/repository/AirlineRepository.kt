package com.andc4.terbangaja.data.repository

import com.andc4.terbangaja.data.datasource.AirlineDataSource
import com.andc4.terbangaja.data.mapper.toAirline
import com.andc4.terbangaja.data.model.Airline
import com.andc4.terbangaja.utils.ResultWrapper
import com.andc4.terbangaja.utils.proceedFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

interface AirlineRepository {
    fun getAirlines(): Flow<ResultWrapper<List<Airline>>>
}

class AirlineRepositoryImpl(private val dataSource: AirlineDataSource) : AirlineRepository {
    override fun getAirlines(): Flow<ResultWrapper<List<Airline>>> {
        return proceedFlow {
            dataSource.getAirlines().toAirline()
        }.catch {
            emit(ResultWrapper.Error(Exception(it)))
        }
    }
}
