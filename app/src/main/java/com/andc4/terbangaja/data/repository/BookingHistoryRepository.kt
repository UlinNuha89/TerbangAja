package com.andc4.terbangaja.data.repository

import com.andc4.terbangaja.data.datasource.BookingHistoryDataSource
import com.andc4.terbangaja.data.mapper.toBaseBookingDataModel
import com.andc4.terbangaja.data.model.BookingHistoryModel
import com.andc4.terbangaja.utils.ResultWrapper
import com.andc4.terbangaja.utils.proceedFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

interface BookingHistoryRepository {
    fun getBookingHistories(
        startDate: String?,
        endDate: String?,
        code: String?,
    ): Flow<ResultWrapper<List<BookingHistoryModel>>>
}

class BookingHistoryRepositoryImpl(private val dataSource: BookingHistoryDataSource) :
    BookingHistoryRepository {
    override fun getBookingHistories(
        startDate: String?,
        endDate: String?,
        code: String?,
    ): Flow<ResultWrapper<List<BookingHistoryModel>>> {
        return proceedFlow {
            val response =
                dataSource.getBookingHistories(1, 15, startDate, endDate, code).toBaseBookingDataModel().data
            response.results?.sortedByDescending { it.createdAt } ?: listOf()
        }.catch {
            emit(ResultWrapper.Error(Exception(it)))
        }
    }
}
