package com.andc4.terbangaja.data.datasource

import com.andc4.terbangaja.data.source.network.model.BaseResponse
import com.andc4.terbangaja.data.source.network.model.data.AirlinesData
import com.andc4.terbangaja.data.source.network.service.TerbangAjaApiService
import com.google.gson.Gson

interface AirlineDataSource {
    suspend fun getAirlines(): BaseResponse<List<AirlinesData>>
}

class AirlineDataSourceImpl(private val service: TerbangAjaApiService) : AirlineDataSource {
    override suspend fun getAirlines(): BaseResponse<List<AirlinesData>> {
        return try {
            val response =
                service.getAirlines()
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
