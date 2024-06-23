package com.andc4.terbangaja.data.datasource

import com.andc4.terbangaja.data.source.network.model.BaseResponse
import com.andc4.terbangaja.data.source.network.model.data.SeatData
import com.andc4.terbangaja.data.source.network.service.TerbangAjaApiService
import com.google.gson.Gson

interface SeatDataSource {
    suspend fun getSeats(
        flightId: Int,
        seatClass: String,
    ): BaseResponse<List<SeatData>>
}

class SeatDataSourceImpl(private val service: TerbangAjaApiService) : SeatDataSource {
    override suspend fun getSeats(
        flightId: Int,
        seatClass: String,
    ): BaseResponse<List<SeatData>> {
        return try {
            val response = service.getSeat(flightId, seatClass)
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
