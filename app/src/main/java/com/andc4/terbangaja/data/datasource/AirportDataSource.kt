package com.andc4.terbangaja.data.datasource

import com.andc4.terbangaja.data.source.network.model.BaseResponse
import com.andc4.terbangaja.data.source.network.model.data.AirportsData
import com.andc4.terbangaja.data.source.network.service.TerbangAjaApiService
import com.google.gson.Gson

interface AirportDataSource {
    suspend fun getAirports(search: String?): BaseResponse<List<AirportsData>>

    suspend fun getAirportsById(id: Int): BaseResponse<AirportsData>
}

class AirportDataSourceImpl(private val service: TerbangAjaApiService) : AirportDataSource {
    override suspend fun getAirports(search: String?): BaseResponse<List<AirportsData>> {
        return try {
            val response =
                service.getAirports(search = search)
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

    override suspend fun getAirportsById(id: Int): BaseResponse<AirportsData> {
        return try {
            val response =
                service.getAirportsById(id)
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
