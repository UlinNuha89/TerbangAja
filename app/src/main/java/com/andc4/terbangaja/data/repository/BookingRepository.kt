package com.andc4.terbangaja.data.repository

import com.andc4.terbangaja.data.datasource.BookingDataSource
import com.andc4.terbangaja.data.model.UserTicket
import com.andc4.terbangaja.utils.ResultWrapper
import com.andc4.terbangaja.utils.proceedFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

interface BookingRepository {
    fun doBooking(
        userTicket: UserTicket,
        price: Int,
    ): Flow<ResultWrapper<String>>
}

class BookingRepositoryImpl(private val dataSource: BookingDataSource) : BookingRepository {
    override fun doBooking(
        userTicket: UserTicket,
        price: Int,
    ): Flow<ResultWrapper<String>> {
        return proceedFlow {
            dataSource.doBooking(userTicket, price).data!!.redirectUrl
        }.catch {
            emit(ResultWrapper.Error(Exception(it)))
        }
    }
}
