package com.andc4.terbangaja.data.datasource

import com.andc4.terbangaja.data.mapper.toListInt
import com.andc4.terbangaja.data.model.UserTicket
import com.andc4.terbangaja.data.source.network.model.BaseResponse
import com.andc4.terbangaja.data.source.network.model.data.dobooking.BookingPassengerPayload
import com.andc4.terbangaja.data.source.network.model.data.dobooking.BookingRequestPayload
import com.andc4.terbangaja.data.source.network.model.data.dobooking.BookingResponse
import com.andc4.terbangaja.data.source.network.service.TerbangAjaApiService
import com.google.gson.Gson

interface BookingDataSource {
    suspend fun doBooking(
        userTicket: UserTicket,
        price: Int,
    ): BaseResponse<BookingResponse>
}

class BookingDataSourceImpl(private val service: TerbangAjaApiService) : BookingDataSource {
    override suspend fun doBooking(
        userTicket: UserTicket,
        price: Int,
    ): BaseResponse<BookingResponse> {
        return try {
            val combineSeat = (userTicket.departureSeats ?: emptyList()) + (userTicket.returnSeats ?: emptyList())
            val response =
                service.doBooking(
                    BookingRequestPayload(
                        userTicket.departureFlight.id,
                        userTicket.returnFlight?.id,
                        price,
                        combineSeat.toListInt(),
                        userTicket.seatClass.value,
                        listOf(
                            BookingPassengerPayload(
                                "Ms.",
                                "Mary Joe",
                                "1992-05-10",
                                "Indonesia",
                                "123123123",
                                "Indonesia",
                                "2030-01-01",
                            ),
                        ),
                        userTicket.passenger.adult,
                        userTicket.passenger.children,
                        userTicket.passenger.baby,
                    ),
                )
            if (response.isSuccessful) {
                response.body() ?: throw Exception("Empty Response body")
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
