package com.andc4.terbangaja.data.datasource

import com.andc4.terbangaja.data.source.network.model.BasePaging
import com.andc4.terbangaja.data.source.network.model.BaseResponse
import com.andc4.terbangaja.data.source.network.model.data.FlightsData
import com.andc4.terbangaja.data.source.network.service.TerbangAjaApiService
import com.google.gson.Gson

interface FlightDataSource {
    suspend fun getFlights(page: Int): BaseResponse<BasePaging<List<FlightsData>>>
}

class FlightDataSourceImpl(private val service: TerbangAjaApiService) : FlightDataSource {
    override suspend fun getFlights(page: Int): BaseResponse<BasePaging<List<FlightsData>>> {
        return try {
            val response = service.getFlights(page)
            if (response.isSuccessful) {
                response.body() ?: throw Exception("Empty response body")
            } else {
                val errorBody = response.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, BaseResponse::class.java)
                throw Exception(errorResponse.message)
            }
        } catch (e: Exception) {
            throw e
        }
    }
}
