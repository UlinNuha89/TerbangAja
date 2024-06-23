package com.andc4.terbangaja.data.datasource

import com.andc4.terbangaja.data.source.network.model.BasePaging
import com.andc4.terbangaja.data.source.network.model.BaseResponse
import com.andc4.terbangaja.data.source.network.model.data.FlightsData
import com.andc4.terbangaja.data.source.network.model.data.FlightsTicket
import com.andc4.terbangaja.data.source.network.service.TerbangAjaApiService
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody

interface FlightDataSource {
    suspend fun getFlights(page: Int): BaseResponse<BasePaging<List<FlightsData>>>

    suspend fun getFlightsTicket(
        page: Int,
        from: String,
        to: String,
        departureDate: String,
        totalPassengers: String,
        seatClass: String,
        filter: String? = null,
    ): BaseResponse<FlightsTicket<BasePaging<List<FlightsData>>>>
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

    override suspend fun getFlightsTicket(
        page: Int,
        from: String,
        to: String,
        departureDate: String,
        totalPassengers: String,
        seatClass: String,
        filter: String?,
    ): BaseResponse<FlightsTicket<BasePaging<List<FlightsData>>>> {
        val fromBody = RequestBody.create("text/plain".toMediaTypeOrNull(), from)
        val toBody = RequestBody.create("text/plain".toMediaTypeOrNull(), to)
        val departureDateBody = RequestBody.create("text/plain".toMediaTypeOrNull(), departureDate)
        val totalPassengersBody =
            RequestBody.create("text/plain".toMediaTypeOrNull(), totalPassengers)
        val seatClassBody = RequestBody.create("text/plain".toMediaTypeOrNull(), seatClass)
        val filterBody = RequestBody.create("text/plain".toMediaTypeOrNull(), filter.orEmpty())
        try {
            val response =
                service.getFlightsTicket(
                    page = page,
                    from = fromBody,
                    to = toBody,
                    departureDate = departureDateBody,
                    totalPassengers = totalPassengersBody,
                    seatClass = seatClassBody,
                    filter = filterBody,
                )
            if (response.isSuccessful) {
                return response.body() ?: throw Exception("Empty response body")
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
