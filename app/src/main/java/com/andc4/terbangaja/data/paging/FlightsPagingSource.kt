package com.andc4.terbangaja.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.andc4.terbangaja.data.datasource.FlightDataSource
import com.andc4.terbangaja.data.mapper.toFlight
import com.andc4.terbangaja.data.model.Flight

class FlightsPagingSource(private val service: FlightDataSource) :
    PagingSource<Int, Flight>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Flight> {
        return try {
            val position = params.key ?: 1
            val response = service.getFlights(page = position)
            LoadResult.Page(
                data = response.toFlight(),
                prevKey = if (position == 1) null else (position - 1),
                nextKey = if (response.data?.next?.page == null) null else (position + 1),
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
