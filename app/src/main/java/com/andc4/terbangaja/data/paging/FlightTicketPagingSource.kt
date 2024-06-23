package com.andc4.terbangaja.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.andc4.terbangaja.data.datasource.FlightDataSource
import com.andc4.terbangaja.data.mapper.toFlightTicket
import com.andc4.terbangaja.data.model.Flight

class FlightTicketPagingSource(
    private val service: FlightDataSource,
    private val from: String,
    private val to: String,
    private val departureDate: String,
    private val totalPassengers: Int,
    private val seatClass: String,
    private val filter: String?,
) : PagingSource<Int, Flight>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Flight> {
        val position = params.key ?: 1
        return try {
            val response =
                service.getFlightsTicket(
                    page = position,
                    from = from,
                    to = to,
                    departureDate = departureDate,
                    totalPassengers = totalPassengers.toString(),
                    seatClass = seatClass,
                    filter = filter,
                )
            LoadResult.Page(
                data = response.toFlightTicket(),
                prevKey = if (position == 1) null else (position - 1),
                nextKey = if (response.data?.departureFlight?.result == null) null else (position + 1),
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Flight>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }
}
