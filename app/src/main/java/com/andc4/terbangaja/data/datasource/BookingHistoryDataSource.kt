package com.andc4.terbangaja.data.datasource

import com.andc4.terbangaja.data.source.network.model.BasePaging
import com.andc4.terbangaja.data.source.network.model.BaseResponse
import com.andc4.terbangaja.data.source.network.model.history.BookingHistory
import com.andc4.terbangaja.data.source.network.service.TerbangAjaApiService
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody

interface BookingHistoryDataSource {
    suspend fun getBookingHistories(
        page: Int,
        limit: Int,
        startDate: String?,
        endDate: String?,
        code: String?,
    ): BaseResponse<BasePaging<List<BookingHistory>>>
}

class BookingHistoryDataSourceImpl(private val service: TerbangAjaApiService) :
    BookingHistoryDataSource {
    override suspend fun getBookingHistories(
        page: Int,
        limit: Int,
        startDate: String?,
        endDate: String?,
        code: String?,
    ): BaseResponse<BasePaging<List<BookingHistory>>> {
        val startDateBody =
            RequestBody.create("text/plain".toMediaTypeOrNull(), startDate.orEmpty())
        val endDateBody = RequestBody.create("text/plain".toMediaTypeOrNull(), endDate.orEmpty())
        val codeBody = RequestBody.create("text/plain".toMediaTypeOrNull(), code.orEmpty())
        try {
            val response =
                service.getBookingHistories(
                    page,
                    limit,
                    startDate = startDateBody,
                    endDate = endDateBody,
                    code = codeBody,
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
