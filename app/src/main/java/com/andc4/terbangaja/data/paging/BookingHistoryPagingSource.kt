package com.andc4.terbangaja.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.andc4.terbangaja.data.datasource.BookingHistoryDataSource
import com.andc4.terbangaja.data.mapper.toBookingHistoryModel
import com.andc4.terbangaja.data.model.BookingHistoryModel

class BookingHistoryPagingSource(
    private val service: BookingHistoryDataSource,
    private val limit: Int,
    private val startDate: String?,
    private val endDate: String?,
    private val code: String?,
) :
    PagingSource<Int, BookingHistoryModel>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BookingHistoryModel> {
        val position = params.key ?: 1
        return try {
            val response =
                service.getBookingHistories(
                    page = position,
                    limit = limit,
                    startDate = startDate,
                    endDate = endDate,
                    code = code,
                )
            LoadResult.Page(
                data = response.toBookingHistoryModel(),
                prevKey = if (position == 1) null else (position - 1),
                nextKey = if (response.data?.result == null) null else (position + 1),
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, BookingHistoryModel>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }
}
