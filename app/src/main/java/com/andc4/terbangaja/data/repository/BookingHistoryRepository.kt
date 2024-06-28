package com.andc4.terbangaja.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.andc4.terbangaja.data.datasource.BookingHistoryDataSource
import com.andc4.terbangaja.data.mapper.toBookingHistoryModel
import com.andc4.terbangaja.data.model.BookingHistoryModel
import com.andc4.terbangaja.data.paging.BookingHistoryPagingSource
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

    fun getPageBookingHistories(
        startDate: String?,
        endDate: String?,
        code: String?,
    ): Flow<PagingData<BookingHistoryModel>>
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
                dataSource.getBookingHistories(1, 20, startDate, endDate, code).toBookingHistoryModel()
            response.sortedByDescending { it.createdAt } ?: listOf()
        }.catch {
            emit(ResultWrapper.Error(Exception(it)))
        }
    }

    override fun getPageBookingHistories(
        startDate: String?,
        endDate: String?,
        code: String?,
    ): Flow<PagingData<BookingHistoryModel>> {
        return Pager(
            pagingSourceFactory = { BookingHistoryPagingSource(dataSource, 15, startDate, endDate, code) },
            config = PagingConfig(pageSize = 10),
        ).flow
    }
}
