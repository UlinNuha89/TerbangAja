package com.andc4.terbangaja.data.repository

import com.andc4.terbangaja.data.datasource.AirportDataSource
import com.andc4.terbangaja.data.mapper.toAirport
import com.andc4.terbangaja.data.mapper.toAirports
import com.andc4.terbangaja.data.model.Airport
import com.andc4.terbangaja.utils.ResultWrapper
import com.andc4.terbangaja.utils.proceedFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

interface AirportRepository {
    fun getAirports(search: String?): Flow<ResultWrapper<List<Airport>>>

    fun getAirportsById(id: Int): Flow<ResultWrapper<Airport>>
}

class AirportRepositoryImpl(private val dataSource: AirportDataSource) : AirportRepository {
    override fun getAirports(search: String?): Flow<ResultWrapper<List<Airport>>> {
        return proceedFlow {
            dataSource.getAirports(search).toAirports()
        }.map {
            if (it.payload?.isEmpty() == false) return@map it
            ResultWrapper.Empty(it.payload)
        }.catch {
            emit(ResultWrapper.Error(Exception(it)))
        }
    }

    override fun getAirportsById(id: Int): Flow<ResultWrapper<Airport>> {
        return proceedFlow {
            dataSource.getAirportsById(id).toAirport()
        }.catch {
            emit(ResultWrapper.Error(Exception(it)))
        }
    }
}
